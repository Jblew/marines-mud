/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This class saves time when start() method is invoked and returns the uptime in other methods.
 * @author jblew
 */
public class UptimeKeeper {
    private AtomicLong startTimeMillis = new AtomicLong(-1);

    private UptimeKeeper() {
    }

    /**
     * starts the uptime keeper.
     */
    public static void start() {
        if(getInstance().startTimeMillis.get() == -1) getInstance().startTimeMillis.set(System.currentTimeMillis());
    }

    
    /**
     *
     * @return uptime in miliseconds
     */
    public static long getUptimeMs() {
        return System.currentTimeMillis() - getInstance().startTimeMillis.get();
    }

    /**
     *
     * @return uptime in seconds
     */
    public static int getUptimeSeconds() {
        return (int) ((System.currentTimeMillis() - getInstance().startTimeMillis.get()) / 1000);
    }

    /**
     *
     * @return uptime in days and hours in text representation.
     */
    public static String getTextUptime() {
        String out = "Uptime: ";

        int seconds = getUptimeSeconds();
        int days = seconds / 60 / 60 / 24;
        int hours = (seconds / 60 / 60) - (days * 24);

        out += days + " days and ";
        out += hours + " hours.";

        return out;
    }

    /**
     *
     * @return
     */
    public static UptimeKeeper getInstance() {
        return UptimeKeeperHolder.INSTANCE;
    }

    private static class UptimeKeeperHolder {
        private static final UptimeKeeper INSTANCE = new UptimeKeeper();
    }
 }
