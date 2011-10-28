/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import marinesmud.web.pages.MasterPanel;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import marinesmud.system.Config;
import marinesmud.web.pages.PlayPage;
import marinesmud.web.utils.BufferWriter;
import marinesmud.web.utils.URLUtils;
import marinesmud.world.beings.Player;
import pl.jblew.code.jutils.data.containers.tuples.TwoTuple;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class Website {

    public final BufferWriter out;
    public final String url;
    public final Map<String, String> cookies;
    public final Map<String, String> getVars;
    public final boolean loggedIn;
    public final String[] urlParts;
    public final LayoutTemplate layout = WebServerConstants.DEFAULT_TEMPLATE;
    private static final String COOKIES_MARKER = "C335345357OCOwewgKwet88jnthgCIECweewtyjjhhedh88990wweggweggegS";
    private Map<String, String> cookiesToSend = new HashMap<String, String>();
    private String rawCookies;
    private int status = 200;
    private boolean notFound;
    private String mimeType = "text/html";
    private final Player loggedInPlayer;

    public Website(PrintWriter out_, String url_, String rawCookies_, boolean showNotFoundPage) {
        out = new BufferWriter();
        String url = url_.trim().split("\\?")[0].trim();
        rawCookies = rawCookies_;
        getVars = URLUtils.parseGetVars(url_);
        cookies = parseCookies(rawCookies);

        String cookiesStr = "Cookies: ";
        for(String k : cookies.keySet()) {
            String v = cookies.get(k);
            cookiesStr += k+"='"+v+"'; ";
        }
        if(WebServerConstants.DEBUG) System.out.println(cookiesStr);

        boolean loggedIn_ = false;
        Player loggedInPlayer_ = null;
        TwoTuple<Map<String, String>, Player> loginData = (TwoTuple<Map<String, String>, Player>) Auth.checkLoggedIn(cookies);
        if (loginData.second != null) {
            loggedInPlayer_ = loginData.second;
            loggedIn_ = true;
        }
        cookiesToSend.putAll(loginData.first);

        loggedIn = loggedIn_;
        loggedInPlayer = loggedInPlayer_;

        String[] urlParts = url.split("\\/");
        if (urlParts.length > 0 && urlParts[0].trim().isEmpty()) {
            urlParts = Arrays.copyOfRange(urlParts, 1, urlParts.length);
        }

        if(getVars.containsKey("logout")) {
            Auth.logout(loggedInPlayer);
            url = "/";
        }

        //Place routing here, because there is still possibility to change url and utlParts!
        if (url.equals("/") || url.isEmpty()) {
            url = Config.get("web.front page url");
        }

        urlParts = url.split("\\/");
        if (urlParts.length > 0 && urlParts[0].trim().isEmpty()) {
            urlParts = Arrays.copyOfRange(urlParts, 1, urlParts.length);
        }
        this.urlParts = urlParts;
        this.url = url;

        notFound = showNotFoundPage;

        if (!showNotFoundPage && urlParts.length > 0) {
            if (urlParts[0].equalsIgnoreCase("css")) {
                boolean found = false;
                for (LayoutTemplate t : LayoutTemplate.values()) {
                    if (urlParts.length > 1 && t.name().equalsIgnoreCase(urlParts[1])) {
                        out.println(t.getCss());
                        mimeType = "text/css";
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    notFound = true;
                }
            } else if (urlParts[0].equalsIgnoreCase(Config.get("web.login panel url name"))) {
                Auth.loginPanel(this, layout, "/login", "/");
            } else if (urlParts[0].equalsIgnoreCase("page")) {
                if (StaticWebPage.Manager.getInstance().pages.containsKey(urlParts[1])) {
                    StaticWebPage page = StaticWebPage.Manager.getInstance().pages.get(urlParts[1]);
                    if (page.forAdmins && !(loggedIn && loggedInPlayer.isAdmin())) {
                        notFound = true;
                    } else {
                        out.println(page.layout.renderStaticPage(page));
                    }
                } else {
                    notFound = true;
                }
            } else if (urlParts[0].equalsIgnoreCase(MasterPanel.URL)) {
                MasterPanel.show(this);
            } else if (urlParts[0].equalsIgnoreCase(PlayPage.URL)) {
                PlayPage.show(this);
            } else {
                notFound = true;
            }
        } else {
            notFound = true;
        }

        if (notFound) {
            status = 404;
            String notFoundUrlName = Config.get("web.not found page url name");
            if (!StaticWebPage.Manager.getInstance().pages.containsKey(notFoundUrlName)) {
                out.println("Page not found!\nError page also not found!");
            } else {
                out.println(layout.renderStaticPage(StaticWebPage.Manager.getInstance().pages.get(notFoundUrlName)));
            }
        }


        String output = "";

        if (status == 200) {
            output += WebServerConstants.getInstance().HTTP_OK_HEADER + "\n";
        } else if (status == 404) {
            output += WebServerConstants.getInstance().HTTP_NOT_FOUND_HEADER + "\n";
        } else {
            throw new RuntimeException("Unknown status: " + status);
        }

        output += "Content-Type: "+mimeType+"\n";

        for (String k : cookiesToSend.keySet()) {
            String v = cookiesToSend.get(k);
            output += "Set-cookie: " + k + "=" + v + "\n";
        }
        output += "\n";

        output += out.getBuffer();
        if (WebServerConstants.DEBUG) {
            System.out.println(output);
        }
        out_.println(output);
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized void forceNotFoundError() {
        notFound = true;
    }

    public void setCookie(String k, String v) {
        cookiesToSend.put(k, v);
    }

    private void webpageHeader() {
        out.println(WebServerConstants.getInstance().HTTP_OK_HEADER);
        out.println(COOKIES_MARKER);
        //blank line signals the end of the headers
        out.println("");
    }

    private static Map<String, String> parseCookies(String sRawCookies) {
        Map<String, String> out = new HashMap<String, String>();
        String[] rawParts = sRawCookies.split(";");
        for (String rwp : rawParts) {
            String rawPart = rwp.trim();
            String[] kvPair = rawPart.split("=");
            if (kvPair.length > 1) {
                out.put(kvPair[0], kvPair[1]);
            } else {
                out.put(kvPair[0], "");
            }
        }
        return out;
    }

    public Player getLoggedInPlayer() {
        if(!loggedIn) throw new UnsupportedOperationException("Nobody is logged in. Cannot getLogedInPlayer.");
        return loggedInPlayer;
    }
}
