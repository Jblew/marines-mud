/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Handler;
import java.util.logging.Logger;
import marinesmud.system.shutdown.MudShutdown;

/**
 *
 * @author jblew
 */
public class InitLogger {

    private InitLogger() {
    }

    public static void initLogger() {
        for(Handler h : Logger.getLogger("").getHandlers()) {
            h.setFormatter(new SingleLineFormatter(true));
        }
    }

    public static void initFileLogger() {
        try {
            File dir = new File("./data/logs/");
            if(!dir.exists()) {
                System.err.println("./data/logs/ directory doesn't exist! Please make this directory and allow server add files, read, and modify them.");
                MudShutdown.panicShutdown();
            }
            Handler handler = new DailyFileHandler(dir, true);
            handler.setFormatter(new SingleLineFormatter(true));
            Logger.getLogger("").addHandler(handler);
        } catch (SecurityException ex) {
            System.err.println("CANNOT SET LOGGER HANDLER: SecurityException: "+ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.err.println("CANNOT SET LOGGER HANDLER: FileNotFoundException: "+ex.getMessage());
        }
    }

    public static void initStringLogger() {
        try {
            Handler handler = new StringLogger(new SingleLineFormatter(false));
            Logger.getLogger("").addHandler(handler);
        } catch (SecurityException ex) {
            System.err.println("CANNOT SET LOGGER HANDLER: SecurityException: "+ex.getMessage());
        } catch (FileNotFoundException ex) {
            System.err.println("CANNOT SET LOGGER HANDLER: FileNotFoundException: "+ex.getMessage());
        }
    }
 }
