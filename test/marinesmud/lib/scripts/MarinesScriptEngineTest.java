/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.scripts;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.Reader;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jblew
 *
public class MarinesScriptEngineTest {

    public static void main(String [] args) {
        try {
            // Verify correct command-line arguments.


            // Create a ScriptEngineManager that discovers all script engine
            // factories (and their associated script engines) that are visible to
            // the current thread’s classloader.

            MarinesScriptEngine marinesse = new MarinesScriptEngine();

            // Execute the specified script, output the returned object, and prove
            // that this object is an Integer.

            Object o = marinesse.eval("(2+2)*(2-2)+1");

            System.out.println("Object value: " + o);
            System.out.println("Is integer: " + (o instanceof Integer));
        } catch (ScriptException ex) {
            Logger.getLogger(MarinesScriptEngineTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
*/