/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.web;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import marinesmud.Main;
import marinesmud.RunMode;
import marinesmud.lib.logging.Level;
import marinesmud.lib.security.MudSecurityManager;
import marinesmud.system.Config;
import marinesmud.system.shutdown.MudShutdown;
import marinesmud.system.shutdown.Shutdownable;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.jutils.utils.IdGenerator;

/**
 *
 * @author jblew
 */
public class FlashPolicyServer /*extends AbstractEventManager<ConnectionAccepted>*/ {
    public final String POLICY = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE cross-domain-policy SYSTEM \"/xml/dtds/cross-domain-policy.dtd\">\n"
            + "\n"
            + "<!-- Policy file for xmlsocket://socks.example.com -->\n"
            + "<cross-domain-policy> \n"
            + "\n"
            + "   <!-- This is a master socket policy file -->\n"
            + "   <!-- No other socket policies on the host will be permitted -->\n"
            + "   <site-control permitted-cross-domain-policies=\"master-only\"/>\n"
            + "\n"
            + "   <!-- Instead of setting to-ports=\"*\", administrator's can use ranges and commas -->\n"
            + "   <allow-access-from domain=\"" + (Main.getRunMode() == RunMode.TEST ? "127.0.0.1" : Config.get("host name")) + "\" to-ports=\"" + Main.MUD_PORT + "\" />\n"
            + "\n"
            + "</cross-domain-policy>\n"
            + "";

    public static void start(ServerSocketChannel flashPolicySocketChannel) {
        getInstance()._start(flashPolicySocketChannel);
    }

    private void _start(ServerSocketChannel flashPolicySocketChannel) {
        new Thread(new Runner(flashPolicySocketChannel), "flash-policy-server-thread-" + IdGenerator.generate()).start();
    }

    private FlashPolicyServer() {
    }

    public static FlashPolicyServer getInstance() {
        return FlashPolicySenderHolder.INSTANCE;
    }

    /*public class ConnectionAccepted implements Event {

    public final SocketChannel socketChannel;

    public ConnectionAccepted(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
    }
    }*/
    private static class FlashPolicySenderHolder {
        private static final FlashPolicyServer INSTANCE = new FlashPolicyServer();
    }

    private class Runner implements Runnable, Shutdownable {
        private AtomicBoolean running = new AtomicBoolean(true);
        private ServerSocketChannel channel = null;
        private Thread serverThread = null;

        public Runner(ServerSocketChannel flashPolicySocketChannel) {
            channel = flashPolicySocketChannel;
            //System.out.println("Flash policy: '" + POLICY + "'");
            MudShutdown.registerShutdownable(this);
        }

        @Override
        public void run() {
            serverThread = Thread.currentThread();

            try {
                channel.configureBlocking(true);
                //Logger.getLogger("PolicyServer").log(Level.INFO, "Waiting for connection to flash policy server...");
                while (running.get()) {
                    try {
                        SocketChannel socket = channel.accept();
                        try {
                            MudSecurityManager.checkSocket(socket.socket());
                            //getInstance().fireEvent(new ConnectionAccepted(socket));
                            this.sendPolicy(socket);
                        } catch (SecurityException e) {
                            Logger.getLogger("PolicyServer").log(Level.NOTICE, "Security problem: {0}. Closing socket.", e.getMessage());
                            try {
                                socket.close();
                            } catch (IOException ex) {
                                Logger.getLogger("PolicyServer").log(java.util.logging.Level.SEVERE, null, ex);
                            }
                        }
                    } catch (AsynchronousCloseException e) {
                        break;
                    }
                }
                quit();
                Logger.getLogger("PolicyServer").log(Level.INFO, "Quit Flash policy server.");
            } catch (IOException ex) {
                Logger.getLogger("PolicyServer").log(Level.SEVERE, null, ex);
            }
        }

        private synchronized void quit() {
            try {
                channel.close();
            } catch (IOException ex) {
                Logger.getLogger("PolicyServer").log(Level.SEVERE, null, ex);
            }
            Logger.getLogger("PolicyServer").log(Level.INFO, "Stop Flash policy server..");
        }

        private synchronized void sendPolicy(final SocketChannel channel) {
            ThreadsManager.getGlobal().executeImmediately(new Runnable() {
                @Override
                public void run() {
                    try {
                        channel.write(Charset.defaultCharset().encode(CharBuffer.wrap(POLICY)));
                        Logger.getLogger("PolicyServer").log(Level.INFO, "Flash policy sent to {0}.", channel.socket().getInetAddress().getHostAddress());
                        channel.close();
                    } catch (IOException ex) {
                        Logger.getLogger("PolicyServer").log(java.util.logging.Level.SEVERE, null, ex);
                    }
                }
            });
        }

        @Override
        public void shutdown() {
            running.set(false);
            quit();
            this.serverThread.interrupt(); //stop server
        }
    }
}
