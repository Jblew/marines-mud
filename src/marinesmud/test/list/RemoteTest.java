/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.test.list;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import marinesmud.lib.HelloObject;
import marinesmud.remote.MethodRequest;
import marinesmud.remote.RemoteMethodsDispatcher;
import marinesmud.remote.RemoteServer;
import marinesmud.test.Test;
import marinesmud.test.TestException;
import pl.jblew.code.ccutils.WaitFor;
import pl.jblew.code.timeutils.TimeValue;

/**
 *
 * @author jblew
 */
public class RemoteTest implements Test {
    public void invoke() throws TestException {
        try {
            Client client = new Client();
            for(Class<?> cls : RemoteMethodsDispatcher.getInstance().getClassesToRegister()) {
                client.getKryo().register(cls);
            }
            client.start();
            client.connect(5000, "127.0.0.1", RemoteServer.getInstance().tcpPort, RemoteServer.getInstance().udpPort);
            final WaitFor<Object> waitForO = new WaitFor<Object>();
            client.addListener(new Listener() {
                @Override
                public void received(Connection conn, Object o) {
                    waitForO.object(o);
                }
            });
            client.sendTCP(new MethodRequest("hello", new Class<?>[]{}, new Object[]{}));
            Object o = waitForO.await(TimeValue.valueOf("5s"));
            if(!(o instanceof HelloObject)) {
                throw new TestException("Response should be of type HelloObject. "+o.getClass().getName()+" found.");
            }
        } catch (InterruptedException ex) {
            throw new TestException("Exceeded time limit of 5 seconds on trying to execute hello method.");
        } catch (IOException ex) {
            throw new TestException("Cannot connect to RemoteServer on ports(tcp=" + RemoteServer.getInstance().tcpPort + ", udp=" + RemoteServer.getInstance().udpPort + "). IOException: ", ex);
        }
    }
}
