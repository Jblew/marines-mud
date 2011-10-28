/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.system.enities;

import pl.jblew.code.jutils.data.containers.tuples.TwoTuple;

/**
 *
 * @author jblew
 */
public class StringClassTuple extends TwoTuple<String, Class> {
    public StringClassTuple(String s, Class c) {
        super(s, c);
    }
}