/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import marinesmud.world.beings.Player;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author jblew
 */
public abstract class AbstractAuthorizedServlet extends HttpServlet {
    protected final static Map<String, Player> SESSION_IDENTIFIERS = Collections.synchronizedMap(new HashMap<String, Player>());
    protected Player authorizedPlayer = null;

    public AbstractAuthorizedServlet() {

    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(!checkLogin(req)) {
            res.sendRedirect("/auth?redirect="+Base64.encodeBase64String(getAuthRedirectPath().getBytes()));
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        if(!checkLogin(req)) {
            res.sendRedirect("/auth?redirect="+Base64.encodeBase64String(getAuthRedirectPath().getBytes()));
        }
    }

    private boolean checkLogin(HttpServletRequest req) {
        HttpSession session = req.getSession();

        if(session.getAttribute("ssid") != null && session.getAttribute("loggedin") != null && session.getAttribute("login") != null) {
            String ssid = (String) session.getAttribute("ssid");
            if (SESSION_IDENTIFIERS.containsKey(ssid)) {
                authorizedPlayer = SESSION_IDENTIFIERS.get(ssid);
                if (authorizedPlayer.getName().equals(session.getAttribute("login"))) {
                    session.setAttribute("loggedin", "true");
                    session.setAttribute("login", authorizedPlayer.getName());
                    session.setAttribute("ssid", ssid);
                    return true;
                }
            }
        }
        return false;
    }

    public abstract String getAuthRedirectPath();
}
