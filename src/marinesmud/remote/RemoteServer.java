/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.remote;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;

/**
 *
 * @author jblew
 */
public class RemoteServer implements Shutdownable {
    public final Server server;
    public final int tcpPort;
    public final int udpPort;
    private final HashMap<Connection, Client> clients = new HashMap<Connection, Client>();

    private RemoteServer() {
        tcpPort = Config.getInt("remote tcp port");
        udpPort = Config.getInt("remote udp port");

        server = new Server();
        server.start();
        try {
            server.bind(tcpPort, udpPort);
            for(Class<?> cls : RemoteMethodsDispatcher.getInstance().getClassesToRegister()) {
                server.getKryo().register(cls);
            }
            RemoteMethodsDispatcher.getInstance().registerClasses(server.getKryo());
            server.addListener(RemoteMethodsDispatcher.getInstance());
        } catch (IOException ex) {
            Logger.getLogger(RemoteServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        MudShutdown.registerShutdownable(this);

    }

    public boolean isClient(Connection c) {
        return clients.containsKey(c);
    }

    public Client getClient(Connection c) {
        return clients.get(c);
    }

    public void shutdown() {
        server.stop();
    }

    public void start() {
    }

    public static RemoteServer getInstance() {
        return SCLUnitHolderHolder.INSTANCE;
    }

    private static class SCLUnitHolderHolder {
        private static final RemoteServer INSTANCE = new RemoteServer();
    }
}
