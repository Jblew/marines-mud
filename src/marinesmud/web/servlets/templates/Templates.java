/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web.servlets.templates;

import marinesmud.MARINESMUD;
import marinesmud.Main;
import net.sf.jtpl.Template;
import pl.jblew.code.jutils.utils.ResourceUtils;

/**
 *
 * @author jblew
 */
public class Templates {
    private Templates() {
    }

    public static Template get(String name) {
        Template t = new Template(ResourceUtils.getContents(Templates.class, name+".html"));
        t.assign("pageTitle", "MarinesMUD");
        t.assign("scripts", "");
        t.assign("runModeName", Main.getRunMode().name());
        t.assign("marinesMudWebsite", MARINESMUD.WEBSITE);
        t.assign("pageContent", "<p>Sorry, page content is empty.</p>");
        return t;
    }
}
