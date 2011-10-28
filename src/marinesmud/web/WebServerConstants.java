/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.web;

import marinesmud.system.Config;

/**
 *
 * @author jblew
 */
public class WebServerConstants {
    public final String HTTP_OK_HEADER;
    public final String HTTP_NOT_FOUND_HEADER;
    public static final boolean DEBUG = Config.getBool("web.debug server");
    public static final LayoutTemplate DEFAULT_TEMPLATE = LayoutTemplate.admin;

    private WebServerConstants() {
        String httpOkHeader = Config.get("web.http ok response header");
        String httpNotFoundHeader = Config.get("web.http not found response header");

        for(String header : Config.getStringList("web.http headers")) {
            httpOkHeader += "\n"+header;
            httpNotFoundHeader += "\n"+header;
        }
        HTTP_OK_HEADER = httpOkHeader;
        HTTP_NOT_FOUND_HEADER = httpNotFoundHeader;
    }

    public static WebServerConstants getInstance() {
        return WebServerConstantsHolder.INSTANCE;
    }

    private static class WebServerConstantsHolder {
        private static final WebServerConstants INSTANCE = new WebServerConstants();
    }
 }
