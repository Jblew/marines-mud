/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.test.list;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import java.io.IOException;
import marinesmud.remote.RemoteMethodsDispatcher;
import marinesmud.remote.RemoteServer;
import marinesmud.test.Test;
import marinesmud.test.TestException;
import marinesmud.world.World;
import marinesmud.world.WorldInterface;
import marinesmud.world.area.room.Room;

/**
 *
 * @author jblew
 */
public class RMITest implements Test {
    public void invoke() throws TestException {
        try {
            Client client = new Client();
            for (Class<?> cls : RemoteMethodsDispatcher.getInstance().getClassesToRegister()) {
                client.getKryo().register(cls);
            }
            client.start();
            client.connect(5000, "127.0.0.1", RemoteServer.getInstance().tcpPort, RemoteServer.getInstance().udpPort);

            System.out.println("Getting remote world(remoteId: "+World.getInstance().getId()+")");
            WorldInterface remoteWorld = ObjectSpace.getRemoteObject(client, World.getInstance().getId(), WorldInterface.class);
            if(remoteWorld.getSSID() != World.getInstance().getSSID()) {
                throw new TestException("Different SSIDs of World and RemoteWorld.");
            }
        } catch (IOException ex) {
            throw new TestException("Cannot connect to RemoteServer on ports(tcp=" + RemoteServer.getInstance().tcpPort + ", udp=" + RemoteServer.getInstance().udpPort + "). IOException: ", ex);
        }
    }
}
