/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import marinesmud.system.Config;

/**
 *
 * @author jblew
 */
public class StaticWebPage {
    public final String urlName;
    public final String menuName;
    public final String title;
    public final LayoutTemplate layout;
    public final String content;
    public final boolean forAdmins;

    private StaticWebPage(String urlName, String menuName, String title, LayoutTemplate layout, String content, boolean forAdmins) {
        this.urlName = urlName;
        this.menuName = menuName;
        this.title = title;
        this.layout = layout;
        this.content = content;
        this.forAdmins = forAdmins;
    }

    public static class Manager {
        public final Map<String, StaticWebPage> pages;

        private Manager() {
            Map<String, StaticWebPage> pages_ = new HashMap<String, StaticWebPage>();

            List<Map<String, String>> rawPages = (List<Map<String, String>>) Config.getList("web.static web pages");
            for(Map<String, String> map : rawPages) {
                String urlName = map.get("url name");
                String menuName = map.get("menu name");
                String title = map.get("title");
                LayoutTemplate layout = WebServerConstants.DEFAULT_TEMPLATE;
                if(!map.get("layout").trim().toLowerCase().equals("default")) {
                    layout = LayoutTemplate.valueOf(map.get("layout"));
                }
                
                String content = map.get("content");
                boolean forAdmins = Boolean.valueOf(map.get("for admins"));
                pages_.put(urlName, new StaticWebPage(urlName, menuName, title, layout, content, forAdmins));
            }
            pages = Collections.unmodifiableMap(Collections.synchronizedMap(pages_));
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
