/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import marinesmud.lib.time.PrecisionTime;

/**
 * @author mballesteros
 */
public class DailyFileHandler extends StreamHandler {
    private static final String LOG_EXTENSION = "log";
    private boolean append;
    private long today;
    private final File dir;

    public DailyFileHandler(File dir, boolean append) throws SecurityException, FileNotFoundException {
        this.append = append;
        this.dir = dir;
        if(!this.dir.isDirectory()) throw new IllegalArgumentException("Parameter dir must be an directory.");
    }

    @Override
    public void publish(LogRecord record) {
        if (isNewDay()) {
            updateToday();
        }
        super.publish(record);
    }

    private boolean isNewDay() {
        if (today == 0) {
            return true;
        } else {
            return (System.currentTimeMillis() - today)
                    > (1000L * 60 * 60 * 24);
        }
    }

    private void updateToday() {
        // 1. Calculate millisecond 0 of today
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.today = cal.getTimeInMillis()+PrecisionTime.getCorrectionMs();

        // 2. Today string representation
        StringBuilder sb = new StringBuilder();
        sb.append(cal.get(Calendar.YEAR)).append('-');
        int month = cal.get(Calendar.MONTH) + 1;
        if (month < 10) {
            sb.append('0').append(month);
        } else {
            sb.append(month);
        }
        sb.append('-').append(cal.get(Calendar.DAY_OF_MONTH));

        // 3. Set output stream
        try {
            setOutputStream(new FileOutputStream(dir.getAbsolutePath()+"/"+sb.toString()+"."+LOG_EXTENSION, append));
        } catch (SecurityException e) {
            System.err.println("CANNOT SET OUTPUT STREAM OF LOGGER DailyFileHandler: SecurityException: "+e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("CANNOT SET OUTPUT STREAM OF LOGGER DailyFileHandler: FileNotFoundException: "+e.getMessage());
        }
    }
}
