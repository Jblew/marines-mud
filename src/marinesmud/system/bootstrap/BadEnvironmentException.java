/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.system.bootstrap;

/**
 *
 * @author jblew
 */
public class BadEnvironmentException extends Exception {
    private static final long serialVersionUID = 1L;

    public BadEnvironmentException(String msg) {
        super(msg);
    }

}