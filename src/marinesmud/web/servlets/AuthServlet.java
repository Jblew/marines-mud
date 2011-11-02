/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import marinesmud.lib.security.SecurityUtils;
import marinesmud.system.Config;
import marinesmud.web.servlets.templates.Templates;
import marinesmud.world.beings.Player;
import net.sf.jtpl.Template;
import org.apache.commons.codec.binary.Base64;
import pl.jblew.code.globallogger.GlobalLogger;
import pl.jblew.code.jutils.utils.IdGenerator;
import pl.jblew.code.jutils.utils.RandomUtils;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class AuthServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        generateResponse(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        generateResponse(req, res);
    }

    private void generateResponse(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String redirect = "/";
        if (req.getParameter("redirect") != null) {
            redirect = Charset.defaultCharset().decode(ByteBuffer.wrap(Base64.decodeBase64(req.getParameter("redirect")))).toString();
        }
        boolean loginTry = false;
        boolean loginOk = false;
        if (req.getParameter("login") != null && req.getParameter("password") != null) {
            loginTry = true;
            if (Player.exists(req.getParameter("login"))) {
                Player p = Player.getByName(req.getParameter("login"));
                if (p.checkPassword(req.getParameter("password"))) {
                    HttpSession session = req.getSession();
                    session.setAttribute("loggedin", "true");
                    session.setAttribute("login", p.getName());
                    String ssid = SecurityUtils.getCtrls(p.getName() + IdGenerator.generate() + "c") + IdGenerator.generate() + "" + RandomUtils.generateRandomString(Config.getInt("salt length"), Config.get("salt characters"));
                    session.setAttribute("ssid", ssid);
                    AbstractAuthorizedServlet.SESSION_IDENTIFIERS.put(ssid, p);
                    res.sendRedirect(redirect);
                    loginOk = true;
                    GlobalLogger.getLogger("www").log(Level.INFO, "{0} logged in.", TextUtils.ucfirst(p.getName()));
                    return;
                } else {
                    loginOk = false;
                }
            } else {
                loginOk = false;
            }
        }

        if (req.getParameter("logout") != null) {
            HttpSession session = req.getSession();

            Player p = null;

            if (session.getAttribute("ssid") != null) {
                p = AbstractAuthorizedServlet.SESSION_IDENTIFIERS.get(session.getAttribute("ssid"));
                AbstractAuthorizedServlet.SESSION_IDENTIFIERS.remove(session.getAttribute("ssid"));
            }

            session.setAttribute("loggedin", "false");
            session.setAttribute("login", "");
            session.setAttribute("ssid", "");

            GlobalLogger.getLogger("www").log(Level.INFO, "{0} logged out.", (p == null? "Unknown" : TextUtils.ucfirst(p.getName())));

            res.sendRedirect(redirect);
        } else if(checkLogin(req)){
            res.sendRedirect(redirect);
        } else {
            PrintWriter out = res.getWriter();
            try {
                Template tpl = Templates.get("admin");
                tpl.assign("pageTitle", "Please log in");
                tpl.assign("scripts", "");
                //System.out.println("login");
                tpl.assign("pageContent", "<div class=\"login-form-container\">"
                        + (loginTry ? "<p class=\"login-text info\">Wrong login or password</p>" : "<p class=\"login-text info\">You have to log in</p>")
                        + "<form class=\"login-form\" method=\"post\" action=\"/auth\">"
                        + "<table class=\"login-form-table\">"
                        + "<tr><td class=\"login-field-label\">Login: </td><td><input class=\"login-field\" type=\"text\" name=\"login\" /></td></tr>"
                        + "<tr><td class=\"login-field-label\">Password: </td><td><input class=\"login-field\" type=\"password\" name=\"password\" /></td></tr>"
                        + "<tr><td colspan=\"2\"><input class=\"login-button\" type=\"submit\" value=\"Log in &gt;\" /></td></tr>"
                        + "</table><input type=\"hidden\" name=\"redirect\" value=\"" + redirect + "\" />"
                        + "</form>");
                tpl.parse("main");
                out.println(tpl.out());
            } catch (Exception ex) {
                ex.printStackTrace(out);
            }
            out.close();
        }
    }

    private boolean checkLogin(HttpServletRequest req) {
        HttpSession session = req.getSession();

        if(session.getAttribute("ssid") != null && session.getAttribute("loggedin") != null && session.getAttribute("login") != null) {
            String ssid = (String) session.getAttribute("ssid");
            if (AbstractAuthorizedServlet.SESSION_IDENTIFIERS.containsKey(ssid)) {
                Player authorizedPlayer = AbstractAuthorizedServlet.SESSION_IDENTIFIERS.get(ssid);
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
}
