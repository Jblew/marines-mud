/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import marinesmud.lib.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.security.MudSecurityManager;
import marinesmud.system.Config;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
class WebServerUserThread {

    private static final int SMUD_CMD_LENGTH = "/smud".length();
    private static final int SO_TIMEOUT = Config.getInt("web.server so timeout");
    private static final boolean DEBUG = Config.getBool("web.debug server");
    private static final File RESOURCES_FILE = new File(Config.get("main dir") + "/resources");
    private static final String BANNED_RESPONSE = "TMP BAN, TIME: "+MudSecurityManager.BAN_HOST_FOR_TIME+"\n";

    private WebServerUserThread() {
    }

    public static void process(SocketChannel remote) {
        try {
            remote.socket().setSoTimeout(SO_TIMEOUT);
            BufferedReader in = new BufferedReader(new InputStreamReader(remote.socket().getInputStream()));
            OutputStream os = remote.socket().getOutputStream();
            String str = ".";
            String strToLower = ".";
            String url = "";
            String cookie = "";
            boolean keepAlive = false;

            if (DEBUG) {
                System.out.println("------");
            }

            while (!str.isEmpty()) {
                str = in.readLine();
                strToLower = str.toLowerCase();

                if (DEBUG) {
                    System.out.println(str);
                }

                if (str.startsWith("GET")) {
                    String[] reqParts = str.split(" ");
                    if (reqParts.length > 1) {
                        url = reqParts[1];
                        if(url.startsWith("http://") || url.startsWith("/w00tw00")) {
                            MudSecurityManager.temporarilyBan(remote.socket().getInetAddress(), "For bad url: '"+url+"'.");
                            PrintWriter pw = new PrintWriter(os);
                            pw.println(BANNED_RESPONSE);
                            remote.close();
                            return;
                        }
                    }
                }

                if (strToLower.startsWith("connection:")) {
                    String[] reqParts = str.split(" ");
                    if (reqParts.length > 1) {
                        if (reqParts[1].trim().equalsIgnoreCase("keep-alive")) {
                            keepAlive = true;
                        }
                    }
                }

                if (strToLower.startsWith("cookie: ")) {
                    cookie = str.replaceFirst("cookie: ", "").replaceFirst("Cookie:", "");
                }
            }
            if (DEBUG) {
                System.out.println("------");
            }
            boolean notFound = false;
            if (!url.isEmpty()) {
                if (!url.equalsIgnoreCase("/favicon.ico")) {
                    if (url.startsWith("/resources")) {
                        url = url.trim().split("\\?")[0].trim();
                        File f = new File(Config.get("main dir") + url);
                        boolean secure = false;
                        File parent = f.getParentFile();
                        while (true) {
                            if (parent.equals(RESOURCES_FILE)) {
                                secure = true;
                                break;
                            } else if(parent.getParentFile() == null || parent.getParentFile().equals(parent)) {
                                secure = false;
                                break;
                            } else {
                                parent = parent.getParentFile();
                            }
                        }
                        if(!secure) {
                            throw new SecurityException("Client tried to access file outside /resources dir!");
                        }
                        if (f.exists() && !f.isDirectory()) {
                            InputStream fin = new BufferedInputStream(new FileInputStream(f));
                            byte[] buffer = new byte[1000];

                            while (true) {
                                int nBytes = fin.read(buffer, 0, 1000);
                                if (nBytes < 0) {
                                    break;
                                }
                                os.write(buffer, 0, nBytes);
                            }
                            os.flush();
                            fin.close();
                            remote.close();
                            return;
                        } else {
                            notFound = true;
                        }
                    } else if (url.startsWith("/content-generator/")) {
                        String subUrl = url.replaceFirst("/content-generator/", "");
                        String[] urlParts = url.split("\\/");
                        if (urlParts.length > 0 && urlParts[0].trim().isEmpty()) {
                            urlParts = Arrays.copyOfRange(urlParts, 1, urlParts.length);
                        }
                        if (urlParts.length > 0) {
                            urlParts = Arrays.copyOfRange(urlParts, 1, urlParts.length);
                            if (urlParts.length > 0) {
                                try {
                                    Class<? extends StreamContentGenerator> c = (Class<? extends StreamContentGenerator>) Class.forName("marinesmud.web.contentgenerators." + urlParts[0].replace(".", ""));
                                    ((StreamContentGenerator) c.newInstance()).generate(urlParts, os);
                                    os.flush();
                                    remote.close();
                                    return;
                                } catch (ClassNotFoundException ex) {
                                    notFound = true;
                                } catch (ClassCastException ex) {
                                    notFound = true;
                                } catch (InstantiationException ex) {
                                    Logger.getLogger(WebServerUserThread.class.getName()).log(Level.SEVERE, null, ex);
                                    notFound = true;
                                } catch (IllegalAccessException ex) {
                                    Logger.getLogger(WebServerUserThread.class.getName()).log(Level.SEVERE, null, ex);
                                    notFound = true;
                                }
                            } else {
                                notFound = true;
                            }
                        } else {
                            notFound = true;
                        }
                    } /*else if (url.startsWith("/smud")) {
                        SmudProxy.process(url.substring(SMUD_CMD_LENGTH), remote, keepAlive, in);
                        return;
                    }*/ else {
                        Logger.getLogger("WebServerUserThread").log(Level.INFO, "[www]{0}: {1}", new Object [] {remote.socket().getInetAddress().getHostAddress(), url});
                    }
                    PrintWriter out = new PrintWriter(os);
                    new Website(out, url, cookie, notFound);
                    out.println("");
                    out.flush();
                }
            }
            remote.close();
        } catch (AsynchronousCloseException ace) {
            if (Thread.currentThread().isInterrupted()) {
                close(remote);
                return;
            } else {
                throw new RuntimeException(ace); //other exceptions are uncatchable
            }
        } catch (SocketTimeoutException ex) {
            Logger.getLogger("WebServerUserThread").log(Level.NOTICE, "Web server connection timeout");
        } catch (SocketException ex) {
            Logger.getLogger(WebServerUserThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebServerUserThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(remote);
        }
    }

    private static void close(SocketChannel remote) {
        if (remote != null) {
            try {
                remote.close();
            } catch (IOException ex) {
                Logger.getLogger(WebServerUserThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            remote = null;
        }
    }
}
