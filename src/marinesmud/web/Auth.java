/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import marinesmud.game.gameplay.MudUser;
import marinesmud.lib.security.SecurityUtils;
import marinesmud.system.Config;
import marinesmud.world.World;
import marinesmud.world.beings.Player;
import pl.jblew.code.jutils.data.containers.tuples.TwoTuple;
import pl.jblew.code.jutils.utils.IdGenerator;
import pl.jblew.code.jutils.utils.RandomUtils;

/**
 *
 * @author jblew
 */
public class Auth {

    private final static String LOGGEDIN_COOKIE_NAME = "wloggedin";
    private final static String SSID_COOKIE_NAME = "wssid";
    private final static String LOGIN_COOKIE_NAME = "wlogin";
    private final static String LOGIN_FIELD_NAME = "wlowginfield";
    private final static String PASSWORD_FIELD_NAME = "wpaswsrtwhordfield";
    private final static String REDIRECT_FIELD_NAME = "wredirwgwefect";
    private final static Map<String, Player> sessionIdentifiers = Collections.synchronizedMap(new HashMap<String, Player>());

    public class Data {

        public final String login;
        public final String controlString;

        public Data(String login, String controlString) {
            this.login = login;
            this.controlString = controlString;
        }
    }

    private Auth() {
    }

    public static boolean adminCanBeLoggedIn(String login, String password) {
        if (Player.exists(login)) {
            Player player = Player.getByName(login);
            if (player.checkPassword(password)) {
                return player.isAdmin();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean canBeLoggedIn(String login, String password) {
        if (Player.exists(login)) {
            Player player = Player.getByName(login);
            return player.checkPassword(password);
        } else {
            return false;
        }
    }

    /**
     *
     * @param cookies - map of received cookies.
     * @return If logged in, method returns TwoTuple containing map of cookies which should be set and user object. If not logged in, method returns TwoTuple containing map of cookies which should be set and null.
     */
    public static TwoTuple<Map<String, String>, Player> checkLoggedIn(Map<String, String> cookies) {
        Player player = null;
        Map<String, String> cookiesToSet = new HashMap<String, String>();
        cookiesToSet.put(LOGGEDIN_COOKIE_NAME, "false");
        cookiesToSet.put(LOGIN_COOKIE_NAME, "");
        cookiesToSet.put(SSID_COOKIE_NAME, "");

        if (cookies.containsKey(LOGGEDIN_COOKIE_NAME)
                && cookies.get(LOGGEDIN_COOKIE_NAME).trim().equalsIgnoreCase("true")
                && cookies.containsKey(LOGIN_COOKIE_NAME)
                && cookies.containsKey(SSID_COOKIE_NAME)) {

            String ssid = cookies.get(SSID_COOKIE_NAME);
            if (sessionIdentifiers.containsKey(ssid)) {
                player = sessionIdentifiers.get(ssid);
                if (player.getName().equals(cookies.get(LOGIN_COOKIE_NAME))) {
                    cookiesToSet.put(LOGGEDIN_COOKIE_NAME, "true");
                    cookiesToSet.put(LOGIN_COOKIE_NAME, player.getName());
                    cookiesToSet.put(SSID_COOKIE_NAME, ssid);
                    return new TwoTuple<Map<String, String>, Player>(cookiesToSet, player);
                }
            }
        }
        return new TwoTuple<Map<String, String>, Player>(cookiesToSet, null);
    }

    public static Map<String, String> logout(Player player) {
        for (String ssid : sessionIdentifiers.keySet()) {
            Player p = sessionIdentifiers.get(ssid);
            if (p == player) {
                sessionIdentifiers.remove(ssid);
                Map<String, String> cookiesToSet = new HashMap<String, String>();
                cookiesToSet.put(LOGGEDIN_COOKIE_NAME, "false");
                cookiesToSet.put(LOGIN_COOKIE_NAME, "");
                cookiesToSet.put(SSID_COOKIE_NAME, "");
                return cookiesToSet;
            }
        }
        throw new NoSuchElementException("Player (name=" + player.getName() + ")");
    }

    public static void loginPanel(Website w, LayoutTemplate template, String formActionUrl, String rebirectToUrl) {
        boolean wrongLoginOrPassword = false;
        String redirectTo = Config.get("web.default login redirect");

        if (w.getVars.containsKey(LOGIN_FIELD_NAME) && w.getVars.containsKey(PASSWORD_FIELD_NAME)) {
            String login = w.getVars.get(LOGIN_FIELD_NAME);
            String password = w.getVars.get(PASSWORD_FIELD_NAME);
            if (canBeLoggedIn(login, password)) {
                String ssid = SecurityUtils.getCtrls(login + IdGenerator.generate() + "c") + IdGenerator.generate() + "" + RandomUtils.generateRandomString(Config.getInt("salt length"), Config.get("salt characters"));
                Player player = Player.getByName(login);
                sessionIdentifiers.put(ssid, player);
                w.setCookie(LOGGEDIN_COOKIE_NAME, "true");
                w.setCookie(LOGIN_COOKIE_NAME, player.getName());
                w.setCookie(SSID_COOKIE_NAME, ssid);

                if (w.getVars.containsKey(REDIRECT_FIELD_NAME)) {
                    redirectTo = w.getVars.get(REDIRECT_FIELD_NAME);
                }
                w.out.println(template.getHeader(Config.get("web.login panel title"),
                        "<script type=\"text/javascript\">"
                        + " document.location.href=\"" + redirectTo + "\";"
                        + "</script>")
                        + "<h2>Przekierowanie</h2><p>" + Config.get("web.redirect message").replace("{redirectUrl}", redirectTo) + "</p>"
                        + template.getFooter());
                return;
            } else {
                wrongLoginOrPassword = true;
            }
        }
        w.out.println(template.getHeader(Config.get("web.login panel title"), "")
                + "<div class=\"login-form-container\">"
                + "   <p class=\"login-text " + (wrongLoginOrPassword ? "error" : "info") + "\">" + (wrongLoginOrPassword ? Config.get("web.wrong password or login message") : Config.get("web.login message")) + "</p>"
                + "   <form class=\"login-form\" method=\"get\" action=\"" + formActionUrl + "\">"
                + "   <table class=\"login-form-table\">"
                + "      <tr><td class=\"login-field-label\">" + Config.get("web.login field label") + ": </td><td><input class=\"login-field\" type=\"text\" name=\"" + LOGIN_FIELD_NAME + "\" /></td></tr>"
                + "      <tr><td class=\"login-field-label\">" + Config.get("web.password field label") + ": </td><td><input class=\"login-field\" type=\"password\" name=\"" + PASSWORD_FIELD_NAME + "\" /></td></tr>"
                + "      <tr><td colspan=\"2\"><input class=\"login-button\" type=\"submit\" value=\"" + Config.get("web.login button label") + "\" /></td></tr>"
                + "   </table>"
                + "   <input type=\"hidden\" name=\"" + REDIRECT_FIELD_NAME + "\" value=\"" + rebirectToUrl + "\" />"
                + "</form>"
                + template.getFooter());
    }
}
