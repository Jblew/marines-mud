/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import marinesmud.web.servlets.templates.Templates;
import marinesmud.world.area.Area;
import marinesmud.world.area.room.Room;
import marinesmud.world.beings.Being;
import marinesmud.world.items.Item;
import net.sf.jtpl.Template;
import pl.jblew.code.globallogger.GlobalLogger;

/**
 *
 * @author jblew
 */
public class EditorServlet extends AbstractAuthorizedServlet {
    public static final String URL = "/editor";

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        super.doGet(req, res);
        PrintWriter out = res.getWriter();
        try {
            out.println(generateResponse(req, res));
        } catch (Exception ex) {
            ex.printStackTrace(out);
            GlobalLogger.getLogger("www").log(Level.WARNING, "", ex);
        }
        out.close();
    }

    private String generateResponse(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Template tpl = Templates.get("admin");
        tpl.assign("pageTitle", "MarinesMUD Editor");
        tpl.assign("scripts", "");
        if (authorizedPlayer.isAdmin()) {
            String content = "<div class=\"span-17 prepend-1 colborder\">";
            content += generatePage(req, res);
            content += menu(req, res);
            tpl.assign("pageContent", content);
        } else {
            tpl.assign("pageContent", "<h2>Sorry, you must be admin to access this page.</h2>");
        }
        tpl.parse("main");
        return (tpl.out());
    }

    private String generatePage(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String action = "main";
        String method = "main";
        String parameter = "";
        String parameter2 = "";
        boolean confirmation = false;
        if (req.getParameter("action") != null) {
            action = req.getParameter("action");
        }
        if (req.getParameter("method") != null) {
            method = req.getParameter("action");
        }
        if (req.getParameter("parameter") != null) {
            parameter = req.getParameter("parameter");
        }
        if (req.getParameter("parameter2") != null) {
            parameter2 = req.getParameter("parameter2");
        }
        if (req.getParameter("confirmation") != null && req.getParameter("confirmation").trim().equalsIgnoreCase("true")) {
            confirmation = true;
        }

        if (action.equals("main") || action.isEmpty()) {
            return main(method, parameter, parameter2, confirmation, req, res);
        } else if (action.equals("areas")) {
            return areas(method, parameter, parameter2, confirmation, req, res);
        } else {
            return error404(req, res);
        }
    }

    private String menu(HttpServletRequest req, HttpServletResponse res) {
        return "</div>"
                + "<div class=\"span-4 last\">"
                + "   <h3>Menu</h3>"
                + "   <ul>"
                + "      <li><a href=\"" + URL + "\"?action=main\">Main</a></li>"
                + "      <li><a href=\"/admin\">Admin panel</a></li>"
                + "      <li><a href=\"/auth?logout=true\">Logout</a></li>"
                + "   </ul>"
                + "</div>";
    }

    private String main(String method, String parameter, String parameter2, boolean confirmation, HttpServletRequest req, HttpServletResponse res) {
        return "<h2>Welcome to MarinesMUD world Editor</h2>"
                + "<p>Here you can edit world.</p>"
                + "<h3>Quick stats</h3>"
                + "<p>"
                + "<ul>"
                + " <li>" + Area.Manager.getInstance().size() + " <a href=\"" + URL + "?action=areas\">Areas</a></li>"
                + " <li>" + Room.Manager.getInstance().size() + " <a href=\"" + URL + "?action=rooms\">Rooms</a></li>"
                + " <li>" + Item.Manager.getInstance().size() + " <a href=\"" + URL + "?action=items\">Items</a></li>"
                + " <li>" + Being.Manager.getInstance().size() + " <a href=\"" + URL + "?action=mobs\">Beings (you can only edit mobs here)</a></li>"
                + "</ul>"
                + "</p>";
    }

    private String areas(String method, String parameter, String parameter2, boolean confirmation, HttpServletRequest req, HttpServletResponse res) throws IOException {
        String out = "";
        if (method.isEmpty() || method.equals("main")) {
            out += "<h2>Areas</h2><p class=\"info\">Do you want to <a href=\"" + URL + "?action=areas&method=new\">create new area</a>?</p><p>"
                    + "<table><tr><th>Id</th><th>Name</th><th>Room range</th><th>Actions</th></tr>";
            for (Area a : Area.Manager.getInstance().getElements()) {
                out += "<tr><td>" + a.getId() + "</td><td>" + a.getName() + "</td><td>" + a.getMinRoom() + "-" + a.getMaxRoom() + "</td>"
                        + "<td><a href=\"" + URL + "?action=areas&method=edit&parameter=" + a.getId() + "\">Edit</a>&nbsp;&nbsp;&nbsp;<a href=\"" + URL + "?action=areas&method=delete&parameter=" + a.getId() + "\">Delete</a></td></tr>";
            }
            out += "</table></p>";
        } else if (method.equals("delete")) {
            if (!parameter.isEmpty() && Area.Manager.getInstance().elementExists(Integer.valueOf(parameter))) {
                Area a = Area.Manager.getInstance().getElement(Integer.valueOf(parameter));
                if (confirmation) {
                    a.destruct();
                    res.sendRedirect(URL + "?action=areas");
                } else {
                    return confirmationFormHtml("Delete area #" + a.getId() + " (" + a.getName() + ")", "<strong>Are you sure, you want to delete area #" + a.getId() + " (" + a.getName() + ")?</strong>"
                            + " Remember that, deleteing area will delete all its rooms.",
                            "areas", "delete", a.getId() + "", "");
                }
            } else {
                out += "<p class=\"error\">Sorry, this area doesn't exist.</p>";
            }
        } else if (method.equals("edit")) {
            if (!parameter.isEmpty() && Area.Manager.getInstance().elementExists(Integer.valueOf(parameter))) {
                Area a = Area.Manager.getInstance().getElement(Integer.valueOf(parameter));
                out += "<h2>Editing area #" + a.getId() + " (" + a.getName() + ")</h2>"
                        +"<table><tr><th>Name</th><th>Value</th>"
                        + "</table>";
            } else {
                out += "<p class=\"error\">Sorry, this area doesn't exist.</p>";
            }
        } else {
            out += error404(req, res);
        }
        return out;
    }

    private String error404(HttpServletRequest req, HttpServletResponse res) {
        return "<h2>Page not found</h2><p>Sorry. The page you are looking for was not found. Do you wish to go to the <a href=\"" + URL + "?action=main\">main page</a>?</p>";
    }

    private static String confirmationFormHtml(String title, String message, String action, String method, String parameter, String parameter2) {
        return "<h2>" + title + "</h2>"
                + "<form action=\"" + URL + "\" method=\"get\">"
                + "   <p class=\"notice\">" + message + "</p>"
                + "   <input type=\"hidden\" name=\"action\" value=\"" + action + "\" />"
                + "   <input type=\"hidden\" name=\"method\" value=\"" + method + "\" />"
                + "   <input type=\"hidden\" name=\"parameter\" value=\"" + parameter + "\" />"
                + "   <input type=\"hidden\" name=\"parameter2\" value=\"" + parameter2 + "\" />"
                + "   <table>"
                + "      <tr><td>Are you sure? </td><td><select id=\"confirmation\" name=\"confirmation\">"
                + "      <option value=\"false\" selected>No</option>"
                + "      <option value=\"true\">Yes</option>"
                + "   </select></td></tr>"
                + "   <tr><td style=\"text-align: right;\">"
                + "      <a href=\"" + URL + "?action=" + action + "\">Cancel</a>&nbsp;</td>"
                + "<td>&nbsp;<input type=\"submit\" value=\"Confirm\" /></td></tr>"
                + "</table></form>";
    }

    @Override
    public String getAuthRedirectPath() {
        return URL;
    }
}
