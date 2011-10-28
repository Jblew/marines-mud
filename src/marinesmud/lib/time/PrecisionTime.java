/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.time;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import marinesmud.lib.logging.Level;
import marinesmud.system.Config;
import marinesmud.system.threadmanagers.Tickers;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class PrecisionTime {
    private static final boolean SYNCHRONIZATION_ENABLED = Config.getBool("time.synchronize");
    private static final AtomicLong MS_CORRECTION = new AtomicLong(0);
    
    private PrecisionTime() {
        synchronize();
        if (SYNCHRONIZATION_ENABLED) {
            TimeValue synchronizationPeriod = TimeValue.valueOf(Config.get("time.synchronization period"));
            Tickers.registerTask(new Runnable() {
                public void run() {
                    synchronize();
                }
            }, synchronizationPeriod.value, synchronizationPeriod.unit);
        }
    }

    private void synchronize() {
        if (SYNCHRONIZATION_ENABLED) {
            Logger.getLogger("PrecisionTime").log(Level.INFO, "Performing time synchronization with Atomic Clock NTP Server.");
            NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(Config.getInt("time.ntp timeout ms"));

            List<String> ntpServers = Config.getStringList("time.ntp servers");

            try {
                client.open();
                for (String addr : ntpServers) {
                    InetAddress hostAddr = InetAddress.getByName(addr);
                    TimeInfo info = client.getTime(hostAddr);
                    info.computeDetails();
                    Long offsetValue = info.getOffset();
                    MS_CORRECTION.set(offsetValue);
                    break;
                }
                Logger.getLogger("PrecisionTime").log(Level.INFO, "Successfully synchronized time with Atomic Clock NTP Server. Current time correction: {0}ms.", MS_CORRECTION.get());
            } catch (SocketException e) {
                Logger.getLogger("PrecisionTime").log(Level.WARNING, "Could not synchronize time with Atomic Clock NTP Server .", e);
            } catch (IOException e) {
                Logger.getLogger("PrecisionTime").log(Level.WARNING, "Could not synchronize time with Atomic Clock NTP Server.", e);
            }
        } else {
            Logger.getLogger("PrecisionTime").log(Level.INFO, "Time synchronization with Atomic Clock NTP Server is DISABLED.");
        }
    }

    public static void init() {
        getInstance();
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis() + MS_CORRECTION.get();
    }

    public static long getCorrectionMs() {
        return MS_CORRECTION.get();
    }
    
    public static String getStringRealTime() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(getCurrentTimeMillis());
        return calendar.get(GregorianCalendar.HOUR)+":"+calendar.get(GregorianCalendar.MINUTE)+":"+calendar.get(GregorianCalendar.SECOND)+"."+calendar.get(GregorianCalendar.MILLISECOND);
    }
    
    public static boolean isSynchronizationEnabled() {
        return SYNCHRONIZATION_ENABLED;
    }

    public static PrecisionTime getInstance() {
        return PrecisionTimeHolder.INSTANCE;
    }

    private static class PrecisionTimeHolder {

        private static final PrecisionTime INSTANCE = new PrecisionTime();
    }
}
