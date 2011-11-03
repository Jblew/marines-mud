/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.test.list;

import marinesmud.test.Test;
import marinesmud.test.TestException;

/**
 *
 * @author jblew
 */
public enum Tests {
    REMOTE(new RemoteTest()),
    WWW(new WWWTest());

    public final Test test;

    private Tests(Test test) {
        this.test = test;
    }

    public static void runAll() throws TestException {
        for(Tests ts : values()) {
            ts.test.invoke();
        }
    }
}
