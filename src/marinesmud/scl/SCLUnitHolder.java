/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.scl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import marinesmud.world.World;
import marinesmud.world.beings.Player;
import org.jboss.netty.channel.Channel;
import pl.jblew.code.scl.ConnectionsListener;
import pl.jblew.code.scl.Request;
import pl.jblew.code.scl.RequestMethod;
import pl.jblew.code.scl.SCLUnit;
import pl.jblew.code.scl.SCLUnitTicket;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class SCLUnitHolder implements ConnectionsListener, Shutdownable {

    public final SCLUnit unit;
    private final HashMap<Channel, Client> clients = new HashMap<Channel, Client>();

    static {
        SCLMethodsRegister.getInstance().reqisterMethod("marines_game_title", new RequestMethod() {

            public Object invokeMethod(String methodName, Object[] parameters, SCLUnit unit, Channel channel) throws Exception {
                return new String [] {Config.get("game title"), Config.get("game subtitle")};
            }

            public Class[] getParameterTypes() {
                return new Class[]{};
            }
        });

        SCLMethodsRegister.getInstance().reqisterMethod("marines_about_game", new RequestMethod() {

            public Object invokeMethod(String methodName, Object[] parameters, SCLUnit unit, Channel channel) throws Exception {
                return Config.get("about");
            }

            public Class[] getParameterTypes() {
                return new Class[]{};
            }
        });

        SCLMethodsRegister.getInstance().reqisterMethod("marines_get_gamezip", new RequestMethod() {

            public Object invokeMethod(String methodName, Object[] parameters, SCLUnit unit, Channel c) throws Exception {
                File file = new File(Config.get("gamezip path"));
                InputStream is = new FileInputStream(file);

                // Get the size of the file
                long length = file.length();

                if (length > 1024 * 1024 * 2) {//2mb max
                    throw new IOException("File is too big");
                }

                // Create the byte array to hold the data
                byte[] bytes = new byte[(int) length];

                // Read in the bytes
                int offset = 0;
                int numRead = 0;
                while (offset < bytes.length
                        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                    offset += numRead;
                }

                // Ensure all the bytes have been read in
                if (offset < bytes.length) {
                    throw new IOException("Could not completely read file " + file.getName());
                }

                // Close the input stream and return bytes
                is.close();

                return bytes;
            }

            public Class[] getParameterTypes() {
                return new Class[]{};
            }
        });

        SCLMethodsRegister.getInstance().reqisterMethod("marines_login_client", new RequestMethod() {

            public Object invokeMethod(String methodName, Object[] parameters, SCLUnit unit, Channel channel) throws Exception {
                if (getInstance().clients.containsKey(channel)) {
                    Client c = getInstance().clients.get(channel);
                    if (!c.isLoggedIn()) {
                        String login = (String) parameters[0];
                        String password = (String) parameters[1];
                        if (Player.exists(login)) {
                            Player player = Player.getByName(login);
                            if (player.checkPassword(password)) {
                                if (player.isLoggedIn()) {
                                    throw new RuntimeException("User already in game.");
                                } else {
                                    player.login(login, password);
                                    Logger.getLogger("LoginPanel").log(Level.INFO, "{0} logged in using marines-client...", login);
                                    return true;
                                }
                            } else {
                                throw new RuntimeException("Bad login or password.");
                            }
                        } else {
                            throw new RuntimeException("Bad login or password.");
                        }
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }

            public Class[] getParameterTypes() {
                return new Class[]{String.class, String.class};
            }
        });

        SCLMethodsRegister.getInstance().reqisterMethod("marines_logout_client", new RequestMethod() {

            public Object invokeMethod(String methodName, Object[] parameters, SCLUnit unit, Channel channel) throws Exception {
                if (getInstance().clients.containsKey(channel)) {
                    Client c = getInstance().clients.get(channel);
                    String login = c.getUser().getLogin();
                    if (c.isLoggedIn()) {
                        c.logOut();
                    }
                    Logger.getLogger("LoginPanel").log(Level.INFO, "{0} logged out using marines-client...", login);
                    return true;
                } else {
                    return false;
                }
            }

            public Class[] getParameterTypes() {
                return new Class[]{};
            }
        });
    }

    private SCLUnitHolder() {
        unit = new SCLUnit(
                new SocketAddress[]{new InetSocketAddress("0.0.0.0", Config.getInt("scl port"))},
                new Object(),
                new SocketAddress[]{}, true);
        MudShutdown.registerShutdownable(this);
        SCLMethodsRegister.getInstance().bindUnit(unit);
        unit.addConnectionsListener(this);

    }

    public void connectionAccepted(Channel c) {
        try {
            SCLUnitTicket ticket = (SCLUnitTicket) unit.sendRequest(c, TimeValue.valueOf("3s"), new Request("get_ticket", new Object[]{})).awaitResponse().getResponse();
            if (ticket.attachment instanceof String && ((String) ticket.attachment).equals("marines-client")) {
                Client client = new Client(c);
                clients.put(c, client);
            }
        } catch (Exception ex) {
            Logger.getLogger(SCLUnitHolder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connectionClosed(Channel c) {
        if (clients.containsKey(c)) {
            clients.remove(c);
        }
    }

    public boolean isClient(Channel c) {
        return clients.containsKey(c);
    }

    public Client getClient(Channel c) {
        return clients.get(c);
    }

    public void shutdown() {
        unit.close();
    }

    public void start() {
    }

    public static SCLUnitHolder getInstance() {
        return SCLUnitHolderHolder.INSTANCE;
    }

    private static class SCLUnitHolderHolder {

        private static final SCLUnitHolder INSTANCE = new SCLUnitHolder();
    }
}
