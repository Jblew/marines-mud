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
import marinesmud.world.beings.Being;
import marinesmud.world.beings.Player;
import net.sf.jtpl.Template;

/**
 *
 * @author jblew
 */
public class WhoServlet  extends HttpServlet {
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
                tpl.assign("pageTitle", "Who is online");

                String alllist = "";
                String onlinelist = "";
                
                for(Being b : Being.Manager.getInstance().getElements()) {
                    if(b instanceof Player) {
                        Player p = (Player)b;
                        alllist += "<li>"+p.getName()+(p.isAdmin()? " (immortal)" : "")+(p.isLoggedIn()? " (online)" : "")+(p.isPlaying()? " (playing)" : "")+"</li>";
                        if(p.isLoggedIn()) onlinelist += "<li>"+p.getName()+(p.isAdmin()? " (immortal)" : "")+(p.isPlaying()? " (playing)" : "")+"</li>";
                    }
                }

                tpl.assign("pageContent", "<h2>Who is online</h2><p><ul>"+onlinelist+"</ul></p><h2>All users</h2><p><ul>"+alllist+"</ul></p>");
                tpl.parse("main");
                return (tpl.out());
        }
}
