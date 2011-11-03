/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.test;

/**
 *
 * @author jblew
 */
public class TestException extends Exception {
    public TestException(String msg) {
        super(msg);
    }

    public TestException(String msg, Exception ex) {
        super(msg, ex);
    }
}
