/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.progstemplates;

/**
 *
 * @author jblew
 */
public interface RealProg {
    public void doIt(Object [] variables);
    public String getCode();
    public ProgTemplate getTemplate();
    public int getVnum();
    public int getCodeStartLine();
}
