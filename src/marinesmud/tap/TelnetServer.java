/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import marinesmud.system.shutdown.Shutdownable;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import pl.jblew.code.ccutils.ThreadsManager;

/**
 *
 *  *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class TelnetServer implements Shutdownable {
    private final ServerBootstrap bootstrap;
    final Map<Channel, TelnetListener> listeners = Collections.synchronizedMap(new HashMap<Channel, TelnetListener>());

    private TelnetServer() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(ThreadsManager.getGlobal().executor, ThreadsManager.getGlobal().executor));
        bootstrap.setPipelineFactory(new PipelineFactory());
    }

    public void registerListener(Channel c, TelnetListener l) {
        listeners.put(c, l);
    }

    public void unregisterListener(Channel c) {
        listeners.remove(c);
    }

    public void bind(SocketAddress addr) {
        bootstrap.bind(addr);
    }

    public void shutdown() {
        bootstrap.releaseExternalResources();
    }

    public void destroy() {
        bootstrap.releaseExternalResources();
    }

    public static TelnetServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final TelnetServer INSTANCE = new TelnetServer();
    }
}
