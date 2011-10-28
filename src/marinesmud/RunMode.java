/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud;

/**
 *
 * @author jblew
 * If you change enum types, remember to change it in code.
 */
public enum RunMode {
    TEST {
        @Override
        public String getInfo() {
            return "TEST mode should be used to test server before it is run in production mode.\n"
                    + "In this mode notifications are off, server doesn't save logs to files and flash policy allows connections to 127.0.0.1.";
        }
    }, PRODUCTION{
        @Override
        public String getInfo() {
            return "PRODUCTION mode should be run, when server is checked and is known to work stable.\n"
                    + " In this mode notifications are on, and flash policy allows on remote host.";
        }
    };

    public abstract String getInfo();
}
