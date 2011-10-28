/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;
import marinesmud.lib.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.security.MudSecurityManager;
import pl.jblew.code.jutils.utils.IdGenerator;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import marinesmud.system.threadmanagers.MudThreadManager;

/**
 *
 * @author jblew
 */
public class OldWebServer /*extends AbstractEventManager<ConnectionAccepted>*/ {

    /**
     * WebServer constructor.
     *
    private OldWebServer() {
    }

    public static void init(ServerSocketChannel webSocketChannel) {
        getInstance()._init(webSocketChannel);
    }

    private void _init(ServerSocketChannel webSocketChannel) {
        new Runner(webSocketChannel).start();
    }

    public static OldWebServer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    *public class ConnectionAccepted implements Event {
        public final SocketChannel socketChannel;

        public ConnectionAccepted(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }
    }*

    private static class InstanceHolder {
        public static final OldWebServer INSTANCE = new OldWebServer();
    }

    private class Runner implements Shutdownable, Runnable {
        private final AtomicBoolean running = new AtomicBoolean(true);
        private final ServerSocketChannel channel;
        private Thread thread = null;

        public Runner(ServerSocketChannel webSocketChannel) {
            channel = webSocketChannel;
        }

        public void start() {
            thread = new Thread(this, "Web-server-" + IdGenerator.generate());
            thread.start();
        }

        @Override
        public void run() {
            MudShutdown.registerShutdownable(this);

            while (running.get()) {
                try {
                    final SocketChannel remote = channel.accept();
                    //getInstance().fireEvent(new ConnectionAccepted(remote));
                    MudThreadManager.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                MudSecurityManager.checkSocket(remote.socket());
                                WebServerUserThread.process(remote);
                            } catch(SecurityException e) {
                                Logger.getLogger(OldWebServer.class.getName()).log(Level.NOTICE, "Security problem: {0}. Closing socket.", e.getMessage());
                                try {
                                    remote.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(OldWebServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                                }
                            }
                        }
                    });
                } catch (AsynchronousCloseException ace) {
                    if (Thread.currentThread().isInterrupted() || !running.get()) {
                        close();
                        break;
                    } else {
                        throw new RuntimeException(ace); //other exceptions are uncatchable
                    }
                } catch (SocketTimeoutException ex) {
                    Logger.getLogger("WebServer").log(Level.NOTICE, "Web server connection timeout");
                } catch (IOException ex) {
                    Logger.getLogger(OldWebServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            close();
        }

        public void close() {
            
        }

        @Override
        public void shutdown() {
            running.set(false);
            thread.interrupt();
            close();
        }
    }
*/
}