/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import marinesmud.system.Config;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class SocketStats {
    private SocketStats() {}

    public static void printStats(final SocketChannel channel) {
        ThreadsManager.getGlobal().executeImmediately(new Runnable() {
            @Override
            public void run() {
                String conData = "Connection data: (local port: " + channel.socket().getLocalPort() + ")\n";
                InetAddress address = channel.socket().getInetAddress();
                conData += "REMOTE: {\n";
                if (address instanceof Inet4Address) {
                    conData += "   IPV4: " + ((Inet4Address) address).getHostAddress() + "\n";
                } else if (address instanceof Inet6Address) {
                    conData += "   IPV6: " + ((Inet6Address) address).getHostAddress() + "\n";
                } else {
                    conData += "   Other: " + address.getHostAddress() + "\n";
                }
                conData += "   Host name: " + address.getHostName() + "\n";
                if (Config.getBool("geolocation on")) {
                    if (Config.getStringList("exclude from geolocation").contains(address.getHostAddress())) {
                        conData += "   Address " + address.getHostAddress() + " is excluded from geolocation.\n";
                    } else {
                        String url = Config.get("geolocation service url").replace("{ip}", address.getHostAddress());
                        try {
                            //String xmlGeolocationData = NetworkUtils.getContent(url);
                            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                            Document doc = builder.parse(url);
                            NodeList nodes = doc.getDocumentElement().getChildNodes();

                            String serviceName = "Unknown";
                            String city = "Unknown";
                            String country = "Unknown";
                            String countryCode = "Unknown";
                            String ip = "Unknown";

                            for (int i = 0; i < nodes.getLength(); i++) {
                                Node n = nodes.item(i);
                                if (n.getNodeName().equalsIgnoreCase("gml:name")) {
                                    serviceName = n.getTextContent();
                                } else if (n.getNodeName().equalsIgnoreCase("gml:featureMember")) {
                                    NodeList nodes2 = n.getChildNodes();
                                    for (int i2 = 0; i2 < nodes2.getLength(); i2++) {
                                        Node n2 = nodes2.item(i2);
                                        if (n2.getNodeName().equalsIgnoreCase("Hostip")) {
                                            NodeList nodes3 = n2.getChildNodes();
                                            for (int i3 = 0; i3 < nodes3.getLength(); i3++) {
                                                Node n3 = nodes3.item(i3);
                                                if (n3.getNodeName().equalsIgnoreCase("ip")) {
                                                    ip = n3.getTextContent();
                                                } else if (n3.getNodeName().equalsIgnoreCase("gml:name")) {
                                                    city = n3.getTextContent();
                                                } else if (n3.getNodeName().equalsIgnoreCase("countryName")) {
                                                    country = TextUtils.ucfirst(n3.getTextContent().toLowerCase());
                                                } else if (n3.getNodeName().equalsIgnoreCase("countryAbbrev")) {
                                                    countryCode = n3.getTextContent();
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            conData += "   Geolocation data: {\n";
                            conData += "      Service: (Name: " + serviceName + "; Url: " + url + ")\n";
                            conData += "      City: " + city + "\n";
                            conData += "      Country: " + country + " (" + countryCode + ")\n";
                            conData += "   }\n";

                        } catch (SAXException ex) {
                            conData += "   Geolocation not possible: SAXException: " + ex.getMessage() + ".\n";
                        } catch (ParserConfigurationException ex) {
                            conData += "   Geolocation not possible: ParserConfigurationException: " + ex.getMessage() + ".\n";
                        } catch (MalformedURLException ex) {
                            Logger.getLogger("SocketStats").log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            conData += "   Geolocation not possible: IOException: " + ex.getMessage() + ". Url: '" + url + "'\n";
                        }
                    }
                } else {
                    conData += "   Geolocation is off.\n";
                }
                conData += "}";
                Logger.getLogger("TelnetUserThread").log(Level.INFO, conData);
            }
        });
    }
}
