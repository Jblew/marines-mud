/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.threadmanagers;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import marinesmud.lib.logging.Level;
import marinesmud.system.bootstrap.Bootstrap;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.jutils.concurrent.SynchronizedDouble;
import pl.jblew.code.jutils.utils.IdGenerator;


/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public class Tickers implements Shutdownable {
    private static final Tickers ourInstance = new Tickers();
    private static final int MAX_TICK_ERROR_MS = Config.getInt("time.max tick error ms");
    private static final boolean INCREASE_SCHEDULED_POOL_SIZE = Config.getBool("time.increase scheduled pool size");
    private static final boolean DISPLAY_ALERTS = Config.getBool("time.display tickers alerts");
    private static final int MAX_SCHEDULED_THREADS = Config.getInt("time.max scheduled threads");
    private static final float INCREASE_CORE_POOL_SIZE_IF_ERROR_BALANCE_EXCEEDES = Config.getFloat("time.increase core pool size if error balance exceedes");
    private final Map<Runnable, Long> tickables = Collections.synchronizedMap(new HashMap<Runnable, Long>());
    private final Map<Runnable, Long> miniTickables = Collections.synchronizedMap(new HashMap<Runnable, Long>());
    private final Map<Runnable, Long> microTickables = Collections.synchronizedMap(new HashMap<Runnable, Long>());
    private final AtomicLong tickNumber = new AtomicLong(0);
    private final AtomicLong miniTickNumber = new AtomicLong(0);
    private final AtomicLong microTickNumber = new AtomicLong(0);
    private final SynchronizedDouble errorBalance = new SynchronizedDouble(0);
    private int scheduledExecutorCorePoolSize = Config.getInt("time.scheduled threads");
    private final ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(scheduledExecutorCorePoolSize, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "Tickers-scheduled-" + IdGenerator.generate());
        }
    });

    private Tickers() {
        MudShutdown.registerShutdownable(this);

        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Runnable r : tickables.keySet()) {
                    long frequency = tickables.get(r);
                    if (tickNumber.get() % frequency == 0) {
                        ThreadsManager.getGlobal().executeImmediately(r);
                    }
                }
                tickNumber.incrementAndGet();
            }
        }, Unit.TICK.periodMs, Unit.TICK.periodMs, TimeUnit.MILLISECONDS);

        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Runnable r : miniTickables.keySet()) {
                    long frequency = miniTickables.get(r);
                    if (miniTickNumber.get() % frequency == 0) {
                        ThreadsManager.getGlobal().executeImmediately(r);
                    }
                }
                miniTickNumber.incrementAndGet();
            }
        }, Unit.MINITICK.periodMs, Unit.MINITICK.periodMs, TimeUnit.MILLISECONDS);

        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (Runnable r : microTickables.keySet()) {
                    long frequency = microTickables.get(r);
                    if (microTickNumber.get() % frequency == 0) {
                        ThreadsManager.getGlobal().executeImmediately(r);
                    }
                }
                microTickNumber.incrementAndGet();
            }
        }, Unit.MICROTICK.periodMs, Unit.MICROTICK.periodMs, TimeUnit.MILLISECONDS);
    }

    public static synchronized Tickers getInstance() {
        return ourInstance;
    }

    public synchronized static void init() {
        getInstance()._init();
    }

    private synchronized void _init() {
        Controller tickController = new Controller(Unit.TICK);
        registerTickable(tickController, Unit.TICK, 1);

        Controller minitickController = new Controller(Unit.MINITICK);
        registerTickable(minitickController, Unit.MINITICK, 1);

        Controller microtickController = new Controller(Unit.MICROTICK);
        registerTickable(microtickController, Unit.MICROTICK, 1);

    }

    @Override
    public synchronized void shutdown() {
        scheduledExecutor.shutdown();
        try {
            scheduledExecutor.awaitTermination(50, TimeUnit.MILLISECONDS);
            scheduledExecutor.shutdownNow();
        } catch (InterruptedException ex) {
        }
    }

    public synchronized static void registerTickable(Runnable t, TickersTime time) {
        if (getInstance().tickables.containsKey(t) || getInstance().miniTickables.containsKey(t) || getInstance().microTickables.containsKey(t)) {
            throw new RuntimeException("This tickable (" + t.getClass().getCanonicalName() + ") has been already registered.");
        }

        if (time.unit == Unit.TICK) {
            getInstance().tickables.put(t, time.value);
        } else if (time.unit == Unit.MINITICK) {
            getInstance().miniTickables.put(t, time.value);
        } else if (time.unit == Unit.MICROTICK) {
            getInstance().microTickables.put(t, time.value);
        } else {
            throw new RuntimeException("Unrecognized type of Tickers.UNIT.");
        }
    }

    public synchronized static void registerTickable(Runnable t, Tickers.Unit unit, long frequency) {
        if (getInstance().tickables.containsKey(t) || getInstance().miniTickables.containsKey(t) || getInstance().microTickables.containsKey(t)) {
            throw new RuntimeException("This tickable (" + t.getClass().getCanonicalName() + ") has been already registered.");
        }

        if (unit == Unit.TICK) {
            getInstance().tickables.put(t, frequency);
        } else if (unit == Unit.MINITICK) {
            getInstance().miniTickables.put(t, frequency);
        } else if (unit == Unit.MICROTICK) {
            getInstance().microTickables.put(t, frequency);
        } else {
            throw new RuntimeException("Unrecognized type of Tickers.UNIT.");
        }
    }

    public synchronized static void unregisterTickable(Runnable t) {
        if (!getInstance().tickables.containsKey(t)) {
            if (!getInstance().miniTickables.containsKey(t)) {
                if (!getInstance().microTickables.containsKey(t)) {
                    throw new NoSuchElementException("No such tickable (" + t.getClass().getCanonicalName() + ")!");
                } else {
                    getInstance().microTickables.remove(t);
                }
            } else {
                getInstance().miniTickables.remove(t);
            }
        } else {
            getInstance().tickables.remove(t);
        }
    }

    public synchronized static void registerTask(final Runnable r, long delay, TimeUnit unit) {
        getInstance().scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ThreadsManager.getGlobal().executeImmediately(r);
            }
        }, delay, 1, unit);
    }

    public static enum Unit {
        TICK(Config.getInt("time.miniticks in tick") * Config.getInt("time.microticks in minitick") * Config.getInt("time.milliseconds in microtick")),
        MINITICK(Config.getInt("time.microticks in minitick") * Config.getInt("time.milliseconds in microtick")),
        MICROTICK(Config.getInt("time.milliseconds in microtick"));
        public final int periodMs;

        private Unit(int periodMs) {
            this.periodMs = periodMs;
        }
    }

    private class Controller implements Runnable {
        private final AtomicLong timeMillis = new AtomicLong(System.currentTimeMillis());
        private final Unit unit;

        public Controller(Unit unit) {
            this.unit = unit;
        }

        @Override
        public void run() {
            long lastTimeMillis = timeMillis.getAndSet(System.currentTimeMillis());
            int difference = (int) (timeMillis.get() - lastTimeMillis);

            int error = unit.periodMs - difference;
            double percentageError = (double) error / (double) unit.periodMs;
            double errorBalance_ = -1;
            synchronized (errorBalance) {
                errorBalance_ = errorBalance.incrementAndGet(percentageError);
            }
            if (Math.abs(error) > MAX_TICK_ERROR_MS && Bootstrap.isStarted() && DISPLAY_ALERTS) {
                Logger.getLogger("Tickers.check" + unit.name()).log(Level.NOTICE, "{0} period: {1}ms; Suspected: {2}ms; Error: {3}ms. Percentage error: {4}. Error balance: {5}.", new Object[]{unit.name(), difference, unit.periodMs, error, percentageError, errorBalance_});
            }

            if (INCREASE_SCHEDULED_POOL_SIZE) {
                synchronized (errorBalance) {
                    if (errorBalance.get() > INCREASE_CORE_POOL_SIZE_IF_ERROR_BALANCE_EXCEEDES && scheduledExecutorCorePoolSize < MAX_SCHEDULED_THREADS) {
                        errorBalance.set(0);
                        scheduledExecutorCorePoolSize++;
                        scheduledExecutor.setCorePoolSize(scheduledExecutorCorePoolSize);
                        Logger.getLogger("Tickers").log(Level.NOTICE, "Tickers: Increased scheduledExecutor core pool size from {0} to {1}.", new Object[]{scheduledExecutorCorePoolSize - 1, scheduledExecutorCorePoolSize});
                    }
                }
            }
        }
    }
}
