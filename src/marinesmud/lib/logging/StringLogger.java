/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.logging;

import java.io.FileNotFoundException;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *
 * @author jblew
 */
public class StringLogger extends Handler {

    private static String content = "";
    private final Formatter formatter;

    public StringLogger(Formatter formatter) throws SecurityException, FileNotFoundException {
        this.formatter = formatter;
    }

    @Override
    public void publish(LogRecord record) {
        content += formatter.format(record);
    }

    @Override
    public void flush() {
        
    }

    @Override
    public void close() throws SecurityException {
        content = "";
    }

    public static String getContent() {
        return content;
    }
}
