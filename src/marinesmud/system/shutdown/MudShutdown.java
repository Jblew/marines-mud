/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.shutdown;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.Main;
import marinesmud.system.Config;
import marinesmud.lib.NotificationProviders;
import marinesmud.system.bootstrap.Bootstrap;
import marinesmud.world.World;
import marinesmud.world.communication.MessageToEverybody;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.jutils.utils.IdGenerator;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class MudShutdown {

    private AtomicBoolean registered = new AtomicBoolean(false);
    private List<Shutdownable> shutdownables = Collections.synchronizedList(new LinkedList<Shutdownable>());

    private final static int SHUTDOWN_CODE = 3;
    private final static int RESTART_CODE = 4;

    private MudShutdown() {
        shutdownables.add(new Shutdownable() {
            public void shutdown() {
                ThreadsManager.getGlobal().shutdown(TimeValue.valueOf("100ms"));
            }
        });
    }

    private static MudShutdown getInstance() {
        return ShutdownHolder.INSTANCE;
    }

    private void _registerShutdownable(Shutdownable s) {
        shutdownables.add(s);
    }

    public static void registerShutdownable(Shutdownable s) {
        getInstance()._registerShutdownable(s);
    }

    private void _register(Shutdownable[] shutdownables_) {
        if (registered.get()) {
            throw new RuntimeException("Shutdown already registered!");
        }

        shutdownables.addAll(Arrays.asList(shutdownables_));

        registerShutdownHook();

        registered.set(true);
    }

    public static void register(Shutdownable[] shutdownables_) {
        getInstance()._register(shutdownables_);
    }

    private void registerShutdownHook() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread("Shutdown-hook-thread-" + IdGenerator.generate()) {

                @Override
                public void run() {
                    try {
                        Bootstrap.closeStorage();
                    } catch (Exception e) {
                    }

                    try {
                        File f = new File(Config.get("shutdowns dir") + Config.DS + "shutdown.on." + System.currentTimeMillis());
                        try {
                            f.createNewFile();
                        } catch (IOException ex) {
                            Logger.getLogger(MudShutdown.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
            Logger.getLogger("MudShutdown").log(Level.SEVERE, "Cannot register shutdown hook", e);
        }
    }

    public static void shutdown() {
        getInstance()._shutdown();
        NotificationProviders.postMessage("shutdown");
        if(Main.isInDaemonMode()) {
            Main.getDaemonController().shutdown();
        }
        else System.exit(SHUTDOWN_CODE);
    }

    public static void panicShutdown() {
        if(Main.isInDaemonMode()) {
            Main.getDaemonController().fail("Panic shutdown");
        }
        else System.exit(SHUTDOWN_CODE);
    }

    public static void restart() {
        getInstance()._shutdown();
        NotificationProviders.postMessage("restart");

        if(Main.isInDaemonMode()) {
            Main.getDaemonController().reload();
        }
        else System.exit(RESTART_CODE);
    }

    private void _shutdown() {
        World.getInstance().sendMessage(new MessageToEverybody("{RNastępuje przeładowanie świata. Prosimy za kilka minut spróbować połączyć się ponownie.{x"));

        for (Shutdownable s : shutdownables) {
            try {
                s.shutdown();
            } catch (Exception e) {
                Logger.getLogger("MudShutdown").log(Level.SEVERE, "Exception in shutdown", e);
            }
        }

        Logger.getLogger("MudShutdown").log(Level.INFO, "Shutdown finished. Bye!");

        Thread.currentThread().interrupt();
    }



    private static class ShutdownHolder {
        private static final MudShutdown INSTANCE = new MudShutdown();
    }
}
