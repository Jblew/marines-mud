/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


/**
 *
 * @author jblew
 */
public class JSEngine {
    private final ScriptEngineManager manager = new ScriptEngineManager();
    private final ScriptEngine engine = manager.getEngineByName("javascript");

    private JSEngine() {
    }

    public Object eval(String code) throws ScriptException {
        Bindings scope = engine.createBindings();
        return engine.eval(code, scope);
    }

    public CompiledScript compile(String code) throws ScriptException {
        return ((Compilable)engine).compile(code);
    }

    public static JSEngine getInstance() {
        return JSEngineHolder.INSTANCE;
    }

    private static class JSEngineHolder {
        private static final JSEngine INSTANCE = new JSEngine();
    }
 }
