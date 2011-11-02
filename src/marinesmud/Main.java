package marinesmud;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.helpers.ListBuilder;
import marinesmud.lib.logging.InitLogger;
import marinesmud.system.bootstrap.Bootstrap;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.tap.TelnetServer;
import marinesmud.web.WebServer;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonController;
import org.apache.commons.daemon.DaemonInitException;

/**
 * PL: Klasa uruchomieniowa projektu.
 * EN: Główna klasa projektu.
 * @author jblew
 */
public class Main implements Daemon {
    public static final String HOST = "0.0.0.0";
    public static int MUD_PORT = 9000;
    public static int WWW_PORT = 80;
    public static int FLASH_POLICY_PORT = 843;
    private static ServerSocketChannel flashPolicyServerChannel;
    private static String[] args;
    private static boolean isTestMode = false;
    private static boolean daemonMode = false;
    private static DaemonController daemonController = null;

    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println("Initalizing...");
        args = dc.getArguments();
        daemonController = dc.getController();
        daemonMode = true;

        List<String> args_ = Arrays.asList(args);

        if (args.length < 1 || !(args_.contains("test-mode") || args_.contains("production-mode"))) {
            System.out.println("");
            System.out.println("Error: You must specify valid RunMode.");
            System.out.println("  - To run in TestMode, use 'java-jar MarinesMud.jar test-mode'.");
            System.out.println("  - To run in ProductionMode, use 'java-jar MarinesMud.jar production-mode'.");
            MudShutdown.panicShutdown();
        }

        InitLogger.initLogger();
        if (args_.contains("test-mode")) {
            isTestMode = true;
            InitLogger.initStringLogger();
            System.out.println("File logger:                         [DISABLED]");

        } else {
            InitLogger.initFileLogger();
            InitLogger.initStringLogger();
            System.out.println("File logger:                         [ENABLED]");
            isTestMode = false;
        }

        InetSocketAddress mudIsa = new InetSocketAddress(HOST, MUD_PORT);
        TelnetServer.getInstance().bind(mudIsa);

        InetSocketAddress wwwIsa = new InetSocketAddress(HOST, WWW_PORT);
        WebServer.getInstance().bind(wwwIsa);

        try {
            flashPolicyServerChannel = ServerSocketChannel.open();
            InetSocketAddress fpIsa = new InetSocketAddress(HOST, FLASH_POLICY_PORT);
            flashPolicyServerChannel.socket().bind(fpIsa);
            flashPolicyServerChannel.socket().setReuseAddress(true);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("init() user: " + System.getProperty("user.name"));
    }

    public static void main(String[] args) {
        System.out.println("Initalizing...");
        Main.args = args;
        WWW_PORT = 9001;
        FLASH_POLICY_PORT = 9004;
        daemonMode = false;

        List<String> args_ = Arrays.asList(args);

        if (args.length < 1 || !(args_.contains("test-mode") || args_.contains("production-mode"))) {
            System.out.println("");
            System.out.println("Error: You must specify valid RunMode.");
            System.out.println("  - To run in TestMode, use 'java-jar MarinesMud.jar test-mode'.");
            System.out.println("  - To run in ProductionMode, use 'java-jar MarinesMud.jar production-mode'.");
            MudShutdown.panicShutdown();
        }
        InitLogger.initLogger();
        if (args_.contains("test-mode")) {
            isTestMode = true;
            InitLogger.initStringLogger();
            System.out.println("File logger:                         [DISABLED]");

        } else {
            InitLogger.initFileLogger();
            InitLogger.initStringLogger();
            System.out.println("File logger:                         [ENABLED]");
            isTestMode = false;
        }

        InetSocketAddress mudIsa = new InetSocketAddress(HOST, MUD_PORT);
        TelnetServer.getInstance().bind(mudIsa);

        InetSocketAddress wwwIsa = new InetSocketAddress(HOST, WWW_PORT);
        WebServer.getInstance().bind(wwwIsa);

        try {
            flashPolicyServerChannel = ServerSocketChannel.open();
            InetSocketAddress fpIsa = new InetSocketAddress(HOST, FLASH_POLICY_PORT);
            flashPolicyServerChannel.socket().bind(fpIsa);
            flashPolicyServerChannel.socket().setReuseAddress(true);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        //System.out.println("main() user: " + System.getProperty("user.name"));

        new Main().start();
    }

    public void start() {
        System.out.println(" +----------------------------------+");
        System.out.println(" |     MARINESMUD5 Server  v" + MARINESMUD.version.toString() + "   |");
        System.out.println(" +----------------------------------+");
        System.out.println(" | By:   |");
        System.out.println(" |        +JBLEW (www.jblew.pl)     |");
        System.out.println(" +----------------------------------+");
        System.out.println("Credits:");
        System.out.println("   Program uses DynamicCompiler created by David J. Biesack (David.Biesack@sas.com).");

        System.out.println("Command line arguments: " + ListBuilder.createSimpleList(args, " "));

        Bootstrap.start(getRunMode(), flashPolicyServerChannel);
    }

    public void stop() {
        MudShutdown.shutdown();
    }

    public void destroy() {
        TelnetServer.getInstance().destroy();
        WebServer.getInstance().destroy();
        try {
            flashPolicyServerChannel.close();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static RunMode getRunMode() {
        if (isTestMode) {
            return RunMode.TEST;
        } else {
            return RunMode.PRODUCTION;
        }
    }

    public static boolean isInDaemonMode() {
        return daemonMode;
    }

    public static DaemonController getDaemonController() {
        return daemonController;
    }
}
