/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import marinesmud.MARINESMUD;
import marinesmud.Main;
import marinesmud.system.Config;

/**
 *
 * @author jblew
 */
public enum LayoutTemplate {

    main {

        @Override
        public String getHeader(String titleOfPage, String scripts) {
            return Config.get("web.head")
                    + "   <title>"+titleOfPage+"</title>"
                    + "   <link rel=\"stylesheet\" href=\"/css/main/stylesheet.css\" type=\"text/css\"/>"
                    + scripts
                    + "</head>"
                    + "<body>"
                    + "   <h1>BlazeMud</h1>";
        }

        @Override
        public String getFooter() {
            return "</body></html>";
        }

        @Override
        public String getCss() {
            return "\n"
                    + "body {\n"
                    + "  color: white;\n"
                    + "  background: black;\n"
                    + "}\n";
        }

        @Override
        public String renderStaticPage(StaticWebPage page) {
            return getHeader(page.title, "")+"<h2>"+page.title+"</h2>"
                    + "<p>"+page.content+"</p>"
                    + getFooter();
        }
    },
    admin {

        @Override
        public String getHeader(String titleOfPage, String scripts) {
            return Config.get("web.head")
                    + "   <title>"+titleOfPage+"</title>"
                    + "   <link rel=\"stylesheet\" href=\"/resources/blueprint/screen.css\" type=\"text/css\" media=\"screen, projection\">"
                    + "   <link rel=\"stylesheet\" href=\"/resources/blueprint/print.css\" type=\"text/css\" media=\"print\">"
                    + "   <!--[if lt IE 8]><link rel=\"stylesheet\" href=\"/resources/blueprint/ie.css\" type=\"text/css\" media=\"screen, projection\"><![endif]--> "
                    + "   <link rel=\"stylesheet\" href=\"/css/admin/stylesheet.css\" type=\"text/css\"/>"
                    + scripts
                    + "</head>"
                    + "<body>"
                    + "   <div id=\"header\">&nbsp;</div>"
                    + ""
                    + "   <div class=\"container\"><h1>MarinesMUD4 Backend ("+Main.getRunMode().name()+")</h1><hr />";
        }

        @Override
        public String getFooter() {
            return "<hr /><p style=\"text-align: center;\">Powered by <a href=\""+MARINESMUD.WEBSITE+"\">MarinesMUD4</a></p>"
                    + "<hr />&nbsp;<br />&nbsp;<br />&nbsp;<br />&nbsp;</div></body></html>";
        }

        @Override
        public String getCss() {
            return "\n"
                    + "body {"
                    + "  margin: 0 !important;"
                    + "  padding: 0 !important;"
                    + "  background: white url(/resources/marines_panel_line.png) repeat-x !important;"
                    + "}"
                    + "#header {"
                    + "  width: 616px  !important;"
                    + "  height: 175px  !important;"
                    + "  background: url(/resources/marines_panel_header.png) no-repeat top left !important;"
                    + "  margin-left: 10px !important;"
                    + "}";
        }

        @Override
        public String renderStaticPage(StaticWebPage page) {
            return getHeader(page.title, "")+"<h2>"+page.title+"</h2>"
                    + "<p>"+page.content+"</p>"
                    + getFooter();
        }
    };

    public abstract String getHeader(String titleOfPage, String scripts);

    public abstract String getFooter();

    public abstract String getCss();

    public abstract String renderStaticPage(StaticWebPage page);
}
