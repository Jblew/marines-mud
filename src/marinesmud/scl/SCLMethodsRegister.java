/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.scl;

import java.util.HashMap;
import java.util.Map;
import pl.jblew.code.scl.RequestMethod;
import pl.jblew.code.scl.SCLUnit;

/**
 *
 * @author jblew
 */
public class SCLMethodsRegister {

    private final Map<String, RequestMethod> methods = new HashMap<String, RequestMethod>();
    private SCLUnit unit = null;

    private SCLMethodsRegister() {
    }

    public synchronized void bindUnit(SCLUnit unit) {
        if (this.unit != null) {
            throw new UnsupportedOperationException("SCLUnit already binded.");
        }
        this.unit = unit;
        for (String mname : methods.keySet()) {
            RequestMethod m = methods.get(mname);
            this.unit.registerMethod(mname, m);
        }
        methods.clear();
    }

    public synchronized void reqisterMethod(String name, RequestMethod m) {
        if (this.unit == null) {
            methods.put(name, m);
        } else {
            unit.registerMethod(name, m);
        }
    }

    public static synchronized SCLMethodsRegister getInstance() {
        return SCLMethodsRegisterHolder.INSTANCE;
    }

    private static class SCLMethodsRegisterHolder {

        private static final SCLMethodsRegister INSTANCE = new SCLMethodsRegister();
    }
}
