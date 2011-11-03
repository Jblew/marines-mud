/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.remote;

import java.io.Serializable;

/**
 *
 * @author jblew
 */
public class MethodRequest implements Serializable {
    private String methodName;
    private Class<?> [] types;
    private Object [] parameters;

    public MethodRequest() {
    }
    
    public MethodRequest(String methodName, Class<?> [] types, Object [] parameters) {
        this.methodName = methodName;
        this.types = types;
        this.parameters = parameters;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?> [] getTypes() {
        return types;
    }

    public Object [] getParameters() {
        return parameters;
    }
}
