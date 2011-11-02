/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import marinesmud.web.servlets.templates.Templates;
import net.sf.jtpl.Template;

/**
 *
 * @author jblew
 */
public class PlayServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        try {
            out.println(generatePage());
        } catch (Exception ex) {
            ex.printStackTrace(out);
        }
        out.close();
    }

    private String generatePage() throws Exception {
                Template tpl = Templates.get("admin");
                tpl.assign("pageTitle", "Play MarinesMUD");
                tpl.assign("scripts", "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/css/terminal-colors.css\" />"
                        + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/decafmud/src/css/decafmud.css\" />"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/decafmud/src/css/decafmud-dark.css\" />"
                + "<script type=\"text/javascript\" src=\"/resources/decafmud/src/js/decafmud.js\"></script>"
                + "<script type=\"text/javascript\" src=\"/resources/js/play.js\"></script>");

                tpl.assign("pageContent", "<h2>Play</h2><p>&nbsp;</p><div id=\"mud\" role=\"application\"></div>");
                tpl.parse("main");
                return (tpl.out());
        }
}
