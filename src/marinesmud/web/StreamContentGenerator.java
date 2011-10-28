/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web;

import java.io.OutputStream;

/**
 *
 * @author jblew
 */
public interface StreamContentGenerator {
    public void generate(String [] urlParts, OutputStream os);
}
