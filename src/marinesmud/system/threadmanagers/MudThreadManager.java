/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.threadmanagers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import pl.jblew.code.jutils.utils.IdGenerator;


/**
 *
 * @author jblew
 */
public class MudThreadManager implements Shutdownable {
    private ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "MudThreadManager-" + IdGenerator.generate());
        }
    });

    private MudThreadManager() {
        MudShutdown.registerShutdownable(this);
    }

    public static void execute(Runnable r) {
        getInstance().executor.execute(r);
    }

    public void shutdown() {
        executor.shutdown();

        try {
            executor.awaitTermination(50, TimeUnit.MILLISECONDS);
            executor.shutdownNow();
        } catch (InterruptedException ex) {
        }
    }

    private static MudThreadManager getInstance() {
        return RunnerHolder.INSTANCE;
    }

    private static class RunnerHolder {
        private static final MudThreadManager INSTANCE = new MudThreadManager();
    }
}
