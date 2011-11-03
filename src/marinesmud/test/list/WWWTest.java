/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.test.list;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import marinesmud.test.Test;
import marinesmud.test.TestException;
import marinesmud.web.WebServer;

/**
 *
 * @author jblew
 */
class WWWTest implements Test {
    public void invoke() throws TestException {
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    new URL("http://" + WebServer.getInstance().getAddress().getHostName() + ":" + WebServer.getInstance().getAddress().getPort() + "/test")
                    .openStream()));
            if(!r.readLine().equals("OK")) throw new TestException("WWWTest did not receive string 'OK' from WebServer.");
            r.close();
        } catch (IOException ex) {
            throw new TestException("IOException in WWWTest", ex);
        }
    }
}
