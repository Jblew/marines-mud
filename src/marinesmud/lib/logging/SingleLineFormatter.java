/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.logging;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import marinesmud.lib.time.PrecisionTime;

public class SingleLineFormatter extends Formatter {

    private static SimpleDateFormat FRMT_DATE;
    private final boolean showThread;

    public SingleLineFormatter(boolean showThread) {
        this.showThread = showThread;
        // check if the date format was specified
        // by default this is used for quick and dirty runs, don't care about the year date
        String dateFormat = System.getProperty("java.util.logging.dateFormat", "yyyy.MM.dd HH:mm:ss");
        FRMT_DATE = new SimpleDateFormat(dateFormat);
    }

    public String format(LogRecord record) {
        // use the buffer for optimal string construction
        StringBuilder sb = new StringBuilder();

        // format time
        sb.append(FRMT_DATE.format(record.getMillis()+PrecisionTime.getCorrectionMs())).append(" ");

        // thread
        if (showThread) {
            sb.append("[").append(Thread.currentThread().getName()).append("] ");
        }

        // level
        sb.append(record.getLevel().toString().toLowerCase());
        sb.append("> ");

        // package/class name, logging name
        String name = record.getLoggerName();

        if (record.getLevel().intValue() > Level.INFO.intValue()) {
            sb.append(name);
            sb.append("   ");
        }
        String msg = record.getMessage();
        Object parameters = record.getParameters();
        if (parameters != null && ((Object[]) parameters).length > 0) {
            int i = 0;
            for (Object o : (Object[]) parameters) {
                msg = msg.replace("{" + i + "}", o.toString());
                i++;
            }
        }
        sb.append(msg);

        // if there was an exception thrown, log it as well
        if (record.getThrown() != null) {
            sb.append("\n").append(printThrown(record.getThrown()));
        }

        sb.append("\n");

        return sb.toString();
    }

    private String printThrown(Throwable thrown) {
        StringBuffer sb = new StringBuffer();

        sb.append("").append(thrown.getClass().getName());
        sb.append(" - ").append(thrown.getMessage());
        sb.append("\n");

        for (StackTraceElement trace : thrown.getStackTrace()) {
            sb.append("\tat ").append(trace).append("\n");
        }

        Throwable cause = thrown.getCause();
        if (cause != null) {
            sb.append("\n").append(printThrown(cause));
        }

        return sb.toString();
    }
}
