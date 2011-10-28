/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import marinesmud.Main;
import marinesmud.RunMode;
import marinesmud.system.Config;
import marinesmud.lib.time.MudCalendar;
import marinesmud.lib.NotificationProviders;
import marinesmud.lib.help.HelpManager;
import marinesmud.lib.logging.Level;
import marinesmud.lib.security.MudSecurityManager;
import marinesmud.scl.SCLUnitHolder;
import marinesmud.system.UptimeKeeper;
import marinesmud.web.FlashPolicyServer;
import marinesmud.tap.Telnet;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import marinesmud.system.threadmanagers.tickers.DayNight;
import marinesmud.system.threadmanagers.Tickers;
import marinesmud.web.OldWebServer;
import marinesmud.web.WebServer;
import marinesmud.world.area.Area;
import marinesmud.world.area.room.Room;
import marinesmud.world.beings.Player;
import marinesmud.world.persistence.WorldPersistence;
import pl.jblew.code.jutils.utils.TimeUtils;

/**
 *
 * @author jblew
 */
public class Bootstrap {
    private AtomicBoolean started = new AtomicBoolean(false);
    private Connection connection = null;

    private Bootstrap() {
    }

    public static void start(RunMode runMode, ServerSocketChannel mudSocketChannel, ServerSocketChannel flashPolicySocketChannel) {
        getInstance()._start(runMode, mudSocketChannel, flashPolicySocketChannel);
    }

    public void _start(RunMode runMode, ServerSocketChannel mudSocketChannel, ServerSocketChannel flashPolicySocketChannel) {
        Logger logger = Logger.getLogger("bootstrap");
        if (started.get()) {
            throw new RuntimeException("System has been already started!");
        }

        try {
            System.out.println("");
            logger.log(Level.INFO, "[SERVER START] RUN MODE: ***{0}***", runMode.name());
            System.out.println(runMode.getInfo());
            checkEnvironment();
            initSecurity();
            checkLicense();
            initConfig();

            //initStorage();
            initTickers();

            startFlashPolicyServer(flashPolicySocketChannel);
            loadWorld();
            initProgsEngine();
            loadHelpManager();
            registerShutdown();
            startUptimeKeeper();
            registerTickables();

            generateGamezip();

            initPersistence();

            initNewWorldIfNeeded();

            WebServer.getInstance().start();

            Map<String, String> data = new HashMap<String, String>();
            data.put("host", Config.get("host name"));
            data.put("port", Main.MUD_PORT + "");
            NotificationProviders.postMessage("start", data);
        } catch (Exception ex) {
            logger.log(Level.WARNING, "Exception in bootstrap", ex);
            MudShutdown.panicShutdown();
        }

        System.out.println("MUD TIME: " + MudCalendar.getInstance().getDateTime().getStringTime() + "; REAL TIME: " + TimeUtils.getStringTime());
        //MudLogger.log("\033[1;31mPamiętaj, aby uruchomić serwer \"smud2/flash policy/flashpolicy.py\", jeżeli chcesz korzystać z smuda!\033[0m");
        System.out.println("Session uid: " + MudSecurityManager.getSessionUID());
        System.out.println("Type quit to stop server!");
        System.out.println("");
        System.out.println("");

        started.set(true);

        //System.out.println("Test function: "+SimpleCompiler.compile(Config.get("test function")).doIt(null));

        startSCLServer();
        startMudServer(mudSocketChannel);
    }

    private void checkEnvironment() {
        try {
            EnvironmentChecker.check();
            Logger.getLogger("bootstrap").log(Level.FINE, "Checking environment and runtime:    [OK]");
        } catch (BadEnvironmentException e) {
            Logger.getLogger("bootstrap").log(Level.WARNING, "Bad environment", e);
        }
    }

    private void initSecurity() {
        MudSecurityManager.init();
        try {
            MudSecurityManager.checkSecurity();
            Logger.getLogger("bootstrap").log(Level.FINE, "Checking security:                   [OK]");
        } catch (SecurityException e) {
            Logger.getLogger("bootstrap").log(Level.WARNING, "Security problem", e);
        }
    }

    private void initConfig() {
        Config.init();
    }

    /*private void initStorage() {
    try {
    Class.forName(Config.get("database driver class name")).newInstance();
    try {
    if (Config.getBool("database needs login")) {
    connection = DriverManager.getConnection(Config.get("database connection url"), Config.get("database user"), Config.get("database password"));
    } else {
    connection = DriverManager.getConnection(Config.get("database connection url"));
    }
    } catch (SQLException ex) {
    Logger.getLogger("Bootstrap").log(Level.WARNING, "Cannot connect to database", ex);
    MudShutdown.panicShutdown();
    }
    Logger.getLogger("Bootstrap").log(Level.FINE, "Established database connection.");
    //if (Config.REMOTE_BACKUP) {
    //	QueriesDispatcher.start();
    //}
    //thread = new Thread(this, "database-thread-"+IdGenerator.generate());
    //thread.start();
    } catch (InstantiationException ex) {
    Logger.getLogger("Bootstrap").log(Level.WARNING, "Cannot connect to database", ex);
    MudShutdown.panicShutdown();
    } catch (IllegalAccessException ex) {
    Logger.getLogger("Bootstrap").log(Level.WARNING, "Cannot connect to database", ex);
    MudShutdown.panicShutdown();
    } catch (ClassNotFoundException ex) {
    Logger.getLogger("Bootstrap").log(Level.WARNING, "Cannot find database driver class.", ex);
    MudShutdown.panicShutdown();
    }
    DatabaseStorageSystem dss = new DatabaseStorageSystem(connection, "variables");
    this.storageSystem = dss;
    //StorageSystemDataTransformer.transferData(storageSystem, );
    }*/
    private void initTickers() {
        Tickers.init();
    }

    private void loadWorld() {
    }

    private void initProgsEngine() {
        //GameProgs.getInstance().init();
    }

    private void loadHelpManager() {
        //HelpManager.init();
    }

    private void startMudServer(ServerSocketChannel mudSocketChannel) {
        Telnet.Server.start(mudSocketChannel);
    }

    private void registerShutdown() {
        MudShutdown.register(new Shutdownable[]{});
    }

    private void startUptimeKeeper() {
        UptimeKeeper.start();
    }

    private void registerTickables() {
        Tickers.registerTask(new Runnable() {
            @Override
            public void run() {
                //Ask java to perform gc
                System.gc();
            }
        }, Config.getInt("time.gc frequency value"), TimeUnit.valueOf(Config.get("time.gc frequency unit")));

        /*Tickers.registerTask(new Runnable() {

        @Override
        public void run() {
        getStorageSystem().testAvailability();
        }
        }, Config.getInt("time.test db connection frequency value"), TimeUnit.valueOf(Config.get("time.test db connection frequency unit")));*/


        //Tickers.registerTickable(new Resets(), Tickers.Unit.TICK, 1);
        //Tickers.registerTickable(new DayNight(), Tickers.Unit.MINITICK, 2);
        //Tickers.registerTickable(new CheckUserThreadsTimeouts(), Tickers.Unit.TICK, 1);
        //Tickers.registerTickable(FlagsDecrementer.getInstance(), Tickers.Unit.TICK, 1);
    }

    public static void closeStorage() {
        try {
            if (getInstance().connection == null) {
                getInstance().connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger("bootstrap-close-storage").log(Level.SEVERE, null, ex);
        }
        Logger.getLogger("Bootstrap-closeStorage").log(Level.INFO, "Closed database connection.");
    }

    /*public static StorageSystem getStorageSystem() {
    if (getInstance().storageSystem != null) {
    return getInstance().storageSystem;
    } else {
    throw new RuntimeException("Bootstrap.getStorageSystem(): storageSystem is null!");
    }
    }*/
    public static boolean isStarted() {
        return getInstance().started.get();
    }

    private static Bootstrap getInstance() {
        return BootstrapHolder.INSTANCE;
    }

    private void checkLicense() {
        License.checkLicense();
    }

    private void startFlashPolicyServer(ServerSocketChannel flashPolicySocketChannel) {
        FlashPolicyServer.start(flashPolicySocketChannel);
    }

    private void startSCLServer() {
        SCLUnitHolder.getInstance().start();
    }

    private void generateGamezip() throws IOException {
        Logger.getLogger("ServerApp").info("Generating gamezip...");
        FileOutputStream fos = new FileOutputStream(Config.get("gamezip path"));
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (File f : new File(Config.get("game files dir")).listFiles()) {
            if (f.isFile()) {
                ZipEntry ze = new ZipEntry(f.getName());
                zos.putNextEntry(ze);

                int read;
                byte[] buffer = new byte[8192];
                FileInputStream fis = new FileInputStream(f);
                while (-1 != (read = fis.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                fis.close();
            }
        }
        zos.close();
        Logger.getLogger("ServerApp").info("Generating gamezip done.");
    }

    private void initPersistence() {
        WorldPersistence.getInstance().init();
    }

    private void initNewWorldIfNeeded() {
        if (!Area.Manager.getInstance().hasId(0)) {
            Area a = new Area(0);
            Area.Manager.getInstance().addElement(a);
            a.setRoomRange(0, 1000);
        }
        if (!Room.Manager.getInstance().hasId(0)) {
            Room.Manager.getInstance().addElement(new Room(0));
        }
        if (Player.Manager.getInstance().size() < 1) {
            System.out.println("New User(admin, test)");
            Player.Manager.getInstance().addElement(new Player("admin"));
        }
    }

    private static class BootstrapHolder {
        private static final Bootstrap INSTANCE = new Bootstrap();
    }
}
