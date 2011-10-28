/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web.pages;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.logging.Logger;
import marinesmud.MARINESMUD;
import marinesmud.Main;
import marinesmud.lib.logging.Level;
import marinesmud.lib.logging.StringLogger;
import marinesmud.lib.security.MudSecurityManager;
import marinesmud.lib.time.MudCalendar;
import marinesmud.lib.time.MudDate;
import marinesmud.lib.time.PrecisionTime;
import marinesmud.system.UptimeKeeper;
import marinesmud.system.bootstrap.License;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.web.Auth;
import marinesmud.web.LayoutTemplate;
import marinesmud.web.Website;
import marinesmud.world.World;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import pl.jblew.code.jutils.helpers.ListBuilder;
import pl.jblew.code.jutils.utils.FileUtils;
import pl.jblew.code.jutils.utils.RuntimeUtils;
import pl.jblew.code.jutils.utils.TypeUtils;

/**
 *
 * @author jblew
 */
public class MasterPanel {
    private static final LayoutTemplate LAYOUT = LayoutTemplate.admin;
    public static final String URL = Config.get("web.admin panel url name");

    public static void show(Website w) {
        if (w.loggedIn) {
            if (w.getLoggedInPlayer().isAdmin()) {
                String content = LAYOUT.getHeader("Administration panel", "");
                content += "<div class=\"span-17 prepend-1 colborder\">";

                String action = "main";
                String method = "main";
                String parameter = "";
                String parameter2 = "";
                boolean confirmation = false;
                if (w.getVars.containsKey("action")) {
                    action = w.getVars.get("action");
                }
                if (w.getVars.containsKey("method")) {
                    method = w.getVars.get("method");
                }
                if (w.getVars.containsKey("parameter")) {
                    parameter = w.getVars.get("parameter");
                }
                if (w.getVars.containsKey("parameter2")) {
                    parameter2 = w.getVars.get("parameter2");
                }
                if (w.getVars.containsKey("confirmation") && w.getVars.get("confirmation").trim().equalsIgnoreCase("true")) {
                    confirmation = true;
                }

                if (action.equals("main")) {
                    content += "<h2>" + "Welcome in MarinesMUD Administration Panel" + ".</h2>"
                            + "<p>" + "Welcome in MarinesMUD Administration Panel. Software version is: " + MARINESMUD.version + "." + "</p>";
                    content += "<h2>Time</h2><p>Time synchronization with atomic clock is <strong>" + (PrecisionTime.isSynchronizationEnabled() ? "Enabled" : "Disabled") + "</strong>."
                            + "Current time correction: <strong>" + PrecisionTime.getCorrectionMs() + "ms</strong>. Actual time: <strong>" + PrecisionTime.getStringRealTime() + "</strong>.</p>"
                            + "<p>Server's uptime: <strong>" + UptimeKeeper.getTextUptime() + "</strong></p>";

                    //content += "<h2>Having problems?</h2>";
                    //content += "<p>Server's uptime: <strong>" + UptimeKeeper.getTextUptime() + "</strong></p><p>If you have some problems with building areas and editing world, please contact"
                    //        + "   <b>World Supervisor (" + ChuckNorris.WORLD_SUPERVISOR_NAME + ", " + ChuckNorris.WORLD_SUPERVISOR_ADDRESS + ")</b>."
                    //        + "   If you have other problems with this instance of marinesmud, you can also write to <b>Main Administrator (" + ChuckNorris.MAIN_ADMINISTRATOR_NAME + ", " + ChuckNorris.MAIN_ADMINISTRATOR_ADDRESS + ")</b>."
                    //        + "   If you have some problems or ideas refering to marinesmud Software, please write to author and programmist of marinesmud (" + ChuckNorris.MARINESMUD_AUTHOR_ADDRESS + ").";
                    content += "</p>"
                            + "<h2>License</h2>"
                            + "<p>" + License.LICENSE + "</p>";
                } else if (action.equals("power")) {
                    if (method.equals("main")) {
                        content += "<h2>Power</h2>"
                                + "<p>Here, you can restart or shut down the mud server, but be carefoul. To restart server it must be run using the ./run.sh script."
                                + " Otherwise, restart will also shutdown the server, so you will have to start it manually.</p>"
                                + ""
                                + "<ul>"
                                + "   <li><a href=\"" + URL + "?action=power&method=restart\">Restart</a></li>"
                                + "   <li><a href=\"" + URL + "?action=power&method=shutdown\">Shutdown</a></li>"
                                + "</ul>";
                    } else if (method.equals("restart")) {
                        if (confirmation) {
                            MudShutdown.restart();
                        } else {
                            content += confirmationFormHtml("Restart server", "<strong>Are you sure, you want restart marinesmud server?</strong> Remember that,"
                                    + " if server hasn't been run using run.sh script, this action will shutdown mud, and you will have to start it manually.",
                                    "power", "restart", "", "");
                        }
                    } else if (method.equals("shutdown")) {
                        if (confirmation) {
                            MudShutdown.shutdown();
                        } else {
                            content += confirmationFormHtml("Shutdown server", "<strong>Are you sure, you want shutdown marinesmud server?</strong>"
                                    + "Remember that you will have to start server manually after shutdown.", "power", "shutdown", "", "");
                        }
                    } else {
                        content += notFoundHtml();
                    }
                } else if (action.equals("logs")) {
                    File dir = new File(Config.get("logs dir"));
                    File[] files = dir.listFiles((FileFilter) FileFileFilter.FILE);
                    Arrays.sort(files, NameFileComparator.NAME_COMPARATOR);
                    if (method.equals("main")) {
                        content += "<h2>Logs</h2>"
                                + "<p>Here, you can view logs generated by whole mud server.</p>"
                                + "<p class=\"info\">Do you want to <a href=\"" + URL + "?action=logs&method=current\">view current log</a>?</p>"
                                + "<ul>";

                        for (File file : files) {
                            content += "<li><a href=\"" + URL + "?action=logs&method=view&parameter=" + file.getName() + "\">" + file.getName() + "</a></li>";
                        }
                        content += "</ul>";
                    } else if (method.equals("view")) {
                        content += "<h2>Log file: '" + parameter + "'</h2><p>" + "Do you want to go back to <a href=\"" + URL + "?action=logs\">log file list</a>?</p>";
                        boolean found = false;
                        for (File file : files) {
                            if (file.getName().equals(parameter)) {
                                try {
                                    content += "<div style=\"overflow: auto;\"><pre>" + FileUtils.getFileContents(file).replace("&", "&amp;") + "</pre></div>";
                                } catch (FileNotFoundException ex) {
                                    content += "<p class=\"error\">Sorry. There is no uch file in log file list.</p>";
                                } catch (IOException ex) {
                                    content += "<p class=\"error\">Sorry. IOException: " + ex.getMessage() + ".</p>";
                                }
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            content += "<p class=\"error\">Sorry. There is no such file in log file list.</p>";
                        }
                    } else if (method.equals("current")) {
                        content += "<h2>Current log</h2>Do you want to go back to <a href=\"" + URL + "?action=logs\">log file list</a>?</p>";
                        content += "<div style=\"overflow: auto;\"><pre>" + StringLogger.getContent().replace("&", "&amp;") + "</pre></div>";
                    } else {
                        content += notFoundHtml();
                    }
                } else if (action.equals("reports")) {
                    List<String> reports = World.getInstance().getReports();
                    boolean showReportsList = false;
                    if (method.equals("add")) {
                        if (confirmation && !parameter.isEmpty() && !parameter2.isEmpty()) {
                            World.getInstance().report(parameter.toUpperCase() + "[" + w.getLoggedInPlayer().getName() + "] " + parameter2);
                            reports = World.getInstance().getReports();
                            showReportsList = true;
                        } else {
                            content += "<h2>Add new report</h2>"
                                    + "<form action=\"" + URL + "\" method=\"get\">"
                                    + "   <p class=\"info\">In this form you can add new report. Please choose type of report: idea, bug or todo. Report will be signed with you username.</p>";
                            if (confirmation) {
                                content += "<p class=\"error\">You must fill all fields.</p>";
                            }
                            content += "   <input type=\"hidden\" name=\"action\" value=\"reports\" />"
                                    + "   <input type=\"hidden\" name=\"method\" value=\"add\" />"
                                    + "   <input type=\"hidden\" name=\"confirmation\" value=\"true\" />"
                                    + "   <table>"
                                    + "      <tr><td>Type of report: </td>"
                                    + "         <td><select id=\"parameter\" name=\"parameter\">"
                                    + "         <option value=\"todo\" selected>Todo</option>"
                                    + "         <option value=\"idea\">Idea</option>"
                                    + "         <option value=\"bug\">Bug</option>"
                                    + "      </select></td></tr>"
                                    + "      <tr><td>Content:</td><td><textarea id=\"parameter2\" name=\"parameter2\" style=\"width: 100%; height: 120px;\"></textarea></td></tr>"
                                    + "   <tr><td style=\"text-align: right;\"><a href=\"" + URL + "?action=reports\">Cancel</a>&nbsp;</td><td style=\"text-align: left;\">&nbsp;<input type=\"submit\" value=\"Send report\" /></td></tr>"
                                    + "   </table>"
                                    + "</form>";
                        }
                    } else if (method.equals("delete") && !parameter.isEmpty()) {
                        if (confirmation) {
                            if (TypeUtils.isInteger(parameter)) {
                                World.getInstance().removeReport(Integer.valueOf(parameter));
                            }
                            reports = World.getInstance().getReports();
                            showReportsList = true;
                        } else {
                            if (TypeUtils.isInteger(parameter) && Integer.valueOf(parameter) < reports.size()) {
                                int id = Integer.valueOf(parameter);
                                content += confirmationFormHtml("Delete report", "<strong>Are you sure, you want delete report (id: " + id + ")?</strong>"
                                        + " Content of this report: <i>" + reports.get(id) + "</i>.", "reports", "delete", id + "", "");
                            } else {
                                content += "<h2>Delete report</h2><p class=\"error\">There is no such report.</p>";
                            }
                        }
                    } else {
                        content += notFoundHtml();
                    }
                    if (method.equals("main") || showReportsList) {
                        content += "<h2>Reports</h2>"
                                + "<p>Here you can see list of idea/bug reports reported by all users of mud and todo reports reported by other admin users.</p>";
                        content += "<p class=\"info\">Do you want to <a href=\"" + URL + "?action=reports&method=add\">add new report</a>?</p>"
                                + "<table>"
                                + "<thead>"
                                + "   <tr><th>#</th><th>Content</th><th>Actions</th></tr>"
                                + "</thead>"
                                + "<tbody>";

                        ListIterator<String> it = reports.listIterator();
                        while (it.hasNext()) {
                            int id = it.nextIndex();
                            String description = it.next();
                            content += "<tr><td>" + id + "</td><td>" + description + "</td><td><a href=\"" + URL + "?action=reports&method=delete&parameter=" + id + "\">Delete</a></td></tr>";
                        }

                        content += "</tbody>"
                                + "</table>";
                    }
                } else if (action.equals("runtime")) {
                    if (method.equalsIgnoreCase("gc")) {
                        System.gc();
                        Logger.getLogger("MasterPanel").log(Level.INFO, "Asked java to perform garbage collection.");
                    }
                    content += "<h2>Runtime</h2>"
                            + "<p>This page contains information about running threads, used memory, and other useful stats.</p>"
                            + "<h3>RunMode: " + Main.getRunMode().name() + "</h3>"
                            + "<p>" + Main.getRunMode().getInfo() + "</p>"
                            + "<p>" + (Main.isInDaemonMode() ? "Application is now started as <strong>daemon</strong>." : "Application is now started <strong>without daemon mode</strong>.</p>")
                            + "<h3>Memory</h3>"
                            + "<p>Free memory: <strong>" + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + "MB</strong>;"
                            + " Used memory: <strong>" + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024 / 1024) + "MB</strong>;"
                            + " Total memory: <strong>" + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + "MB</strong>;"
                            + " Memory limit: <strong>" + (Runtime.getRuntime().maxMemory() == Long.MAX_VALUE ? "Unlimited" : (Runtime.getRuntime().maxMemory() / 1024 / 1024)) + "MB</strong>.</p>";
                    content += "<p class=\"notice\">If you want, you can ask java to <a href=\"" + URL + "\"?action=runtime&method=gc\">perform Garbage Collection</a>."
                            + " It may delete unused objects and freely some memory, but it may also increase total amount of memory, so be carefoul."
                            + " If something would go bad, you can always restart the server in <a href=\"" + URL + "?action=power\">Power</a> section.</p>";
                    content += "<h3>Threads</h3>"
                            + "<table>"
                            + "<thead><tr><th>Id</th><th>Name</th><th>Priority</th><th>State</th><th>Group</th></tr></thead>"
                            + "<tbody>";
                    List<Thread> threads = RuntimeUtils.getAllThreads();
                    for (Thread thread : threads) {
                        long id = thread.getId();
                        String name = thread.getName();
                        int priority = thread.getPriority();
                        Thread.State state = thread.getState();
                        String groupName = thread.getThreadGroup().getName();
                        content += "<tr><td>" + id + "</td><td>" + name + "</td><td>" + priority + "</td><td>" + state.name() + "</td><td>" + groupName + "</td></tr>";
                    }
                    content += "</tbody></table><p>Threads count: <strong>" + threads.size() + "</strong>.</p>";
                } else if (action.equals("currentsession")) {
                    MudDate dateTime = MudCalendar.getInstance().getDateTime();
                    content += "<h2>Current session data</h2>";
                    content += "<p>This section provides data about current session, for example count of running user threads, or current security params.</p>";
                    content += "<h3>Time</h3>"
                            + "<p>Output of the 'time' command: <hr />"
                            + "<pre>" + dateTime.getTimeCommandResponse() + "</pre>"
                            + "<hr /> Precision time data: <strong>" + dateTime.getStringTime() + "</strong>.</p>"
                            + "<h3>Security</h3>"
                            //+ "<p>" + (LANGUAGE == Language.PL ? "UID bieżącej sesji" : "Current session UID") + ": <strong>" + MudSecurityManager.getSessionUID() + "</strong>."
                            + " Hosts blacklist: <strong>[" + ListBuilder.createSimpleList(MudSecurityManager.IP_BLACKLIST, ", ") + "]</strong>."
                            + " Temporarily banned hosts: <strong>" + MudSecurityManager.getTemporarilyBannedHostsCount() + "</strong>.</p>";
                } else if (action.equals("notifications")) {
                    content += "<h2>Notifications</h2>";
                    content += "<p>In this section you can send notifications to various channels.</p>";
                } else if (action.equals("channels")) {
                } else if (action.equals("changepassword")) {
                    if (confirmation && !parameter.isEmpty() && !parameter2.isEmpty()) {
                    } else {
                        content += "<h2>Change password</h2>"
                                + "<form action=\"" + URL + "\" method=\"get\">";
                            content += "   <p class=\"info\">Here, you can change your password. Think for a while, before you do this. Password should'n be same (or similar) as login."
                                    + " The longer password, the safer it will be. It is also good, to add some numbers or characters like !@#$%^&*().</p>";
                        if (confirmation) {
                            content += "<p class=\"error\">You must fill all fields.</p>";
                        }
                        content += "  <input type=\"hidden\" name=\"action\" value=\"changepassword\" />"
                                + "   <input type=\"hidden\" name=\"confirmation\" value=\"true\" />"
                                + "   <table>"
                                + "      <tr><td>New password: </td>"
                                + "         <td><input type=\"password\" name=\"parameter\" /></tr>"
                                + "   <tr><td style=\"text-align: right;\"><a href=\"" + URL + "?action=changepassword\">Cancel</a>&nbsp;</td><td style=\"text-align: left;\">&nbsp;<input type=\"submit\" value=\"Send report\" /></td></tr>"
                                + "   </table>"
                                + "</form>";
                    }
                } else {
                    content += notFoundHtml();
                }

                    content += "</div>"
                            + "<div class=\"span-4 last\">"
                            + "   <h3>Menu</h3>"
                            + "   <ul>"
                            + "      <li><a href=\"" + URL + "\"?action=main\">Main</a></li>"
                            + "      <li><a href=\"" + URL + "?action=currentsession\">Current session</a></li>"
                            + "      <li><a href=\"" + URL + "?action=runtime\">Runtime</a></li>"
                            + "      <li><a href=\"" + URL + "?action=reports\">Reports</a></li>"
                            + "      <li><a href=\"" + URL + "?action=notifications\">Notifications</a></li>"
                            + "      <li><a href=\"" + URL + "?action=channels\">Channels</a></li>"
                            + "      <li><a href=\"" + URL + "?action=logs\">Logs</a></li>"
                            + "      <li><a href=\"" + URL + "?action=power\">Power</a></li>"
                            + "      <li><a href=\"" + URL + "?action=changepassword\">Change password</a></li>"
                            + "      <li><a href=\"" + URL + "?logout=true\">Logout</a></li>"
                            + "   </ul>"
                            + "</div>";

                content += LAYOUT.getFooter();
                w.out.println(content);
            } else {
                    w.out.println(LAYOUT.getHeader("Administration Panel", "") + "<h2>You must be admin to access this page.</h2>"
                            + "<p>You must be admin to access this page. "
                            + "Do you want to <a href=\"/?logout=true\">logout</a>?</p>" + LAYOUT.getFooter());
            }
        } else {
            Auth.loginPanel(w, LAYOUT, URL, URL);
        }
    }

    private static String notFoundHtml() {
        String out = "<h2>Page not found</h2>";
            out += "<p>Przepraszamy, ale strona której szukasz nie została znaleziona. Czy chcesz przejść do <a href=\"" + URL + "\"?action=main\">strony głównej</a>?</p>";
        return out;
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

    private MasterPanel() {
    }

    public static MasterPanel getInstance() {
        return MasterPanelHolder.INSTANCE;
    }

    public static enum Language {
        PL, EN;

        public static Language getDefaultLanguage() {
            return Language.EN;
        }

        public static String[] getNames() {
            Language[] languages = Language.values();
            String[] out = new String[languages.length];
            int i = 0;
            for (Language l : languages) {
                out[i] = l.name();
                i++;
            }
            return out;
        }
    }

    private static class MasterPanelHolder {
        private static final MasterPanel INSTANCE = new MasterPanel();
    }
}
