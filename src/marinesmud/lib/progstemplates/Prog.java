/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.progstemplates;

import pl.jblew.code.dynamiccompiler.CharSequenceCompilerException;

/**
 *
 * @author jblew
 */
public interface Prog {
    public void doIt(Object [] variables);
    public void recompile(String newCode, ProgTemplate newTemplate) throws CharSequenceCompilerException;
    public String getCode();
    public ProgTemplate getTemplate();
    public int getVnum();
    public int getCodeStartLine();
}
