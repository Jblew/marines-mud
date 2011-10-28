/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web.pages;

import marinesmud.system.Config;
import marinesmud.web.LayoutTemplate;
import marinesmud.web.Website;

/**
 *
 * @author jblew
 */
public class PlayPage {

    private static final LayoutTemplate LAYOUT = LayoutTemplate.admin;
    public static final String URL = Config.get("web.play page url name");

    private PlayPage() {
    }

    public static void show(Website w) {
        String content = LAYOUT.getHeader("Administration Panel", ""
                //+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"resources/decafmud/src/css/main.css\" />" NO SUCH FILE
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/css/terminal-colors.css\" />"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/decafmud/src/css/decafmud.css\" />"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"/resources/decafmud/src/css/decafmud-dark.css\" />"
                + "<script type=\"text/javascript\" src=\"/resources/decafmud/src/js/decafmud.js\"></script>\n"
                + "<script type=\"text/javascript\" src=\"/resources/js/play.js\"></script>\n");
        content += "<h2>Graj</h2>"
                + "<p>&nbsp;</p>"
                + "<div id=\"mud\" role=\"application\"></div>";
        content += LAYOUT.getFooter();
        w.out.println(content);
    }
}
