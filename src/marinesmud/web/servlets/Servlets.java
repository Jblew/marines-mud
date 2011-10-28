/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.servlets;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;

/**
 *
 * @author jblew
 */
public class Servlets {
    public static final Map<Class<? extends Servlet>, String> servlets = new HashMap<Class<? extends Servlet>, String>();
    static {
        servlets.put(HelloServlet.class, "/hello");
    }

    private Servlets() {
    }
 }
