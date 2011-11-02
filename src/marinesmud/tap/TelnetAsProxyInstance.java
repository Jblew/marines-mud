/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.system.Config;
import marinesmud.world.beings.Player;
import org.jboss.netty.channel.Channel;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.ccutils.ThreadsManager.TaskObject;

/**
 *
 * @author jblew
 */
public final class TelnetAsProxyInstance implements Runnable,TelnetListener {
    private final TaskObject taskObject;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();
    private Channel channel;
    private Player player = null;
    private final Object listenerSync = new Object();
    private TelnetListener listener = null;

    public TelnetAsProxyInstance(Channel channel) {
        this.channel = channel;
        taskObject = ThreadsManager.getGlobal().runLongTask(this);
        TelnetServer.getInstance().registerListener(channel, this);
    }

    @Override
    public void run() {
        //System.out.println(">Run");
        try {
            println(Config.get("asciiarts.intro") + "\n");
            try {
                player = Panels.login(this);
            } catch (NullPointerException e) {
                Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Not logged in!");
                return;
            }
            while (true) {
                Panels.showMenu(this, getPlayer());
                getPlayer().play();
                new TelnetGameplay(this, getPlayer()).run();
            }
        } catch (CloseUserConnectionRuntimeException ex) {
            quit(ex.getMessage());
            return;
        } catch (InterruptedException ex) {
            quit("InterruptyedException: " + ex.getMessage());
            return;
        } catch (IOException ex) {
            Logger.getLogger("TelnetAsProxy").log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex) {
            Logger.getLogger("TelnetAsProxy").log(Level.SEVERE, null, ex);
            quit("NullPointer exception in user thread!");
            return;
        }
        Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Closed user connection!");
    }

    public void quit(String reason) {
        if (player != null) {
            if (player.isPlaying()) {
                player.stopPlaying();
            }

            if (player.isLoggedIn()) {
                player.logout();
            }
        }
        if (channel != null) {
            channel.close();
        }
        if (player == null) {
            Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Stopped user (unknown) thread. Reason: {0}", reason);
        } else {
            Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Stopped user ({0}) thread. Reason: {1}", new Object[]{player.getName(), reason});
        }
    }

    public void setListener(TelnetListener l) {
        synchronized (listenerSync) {
            listener = l;
        }
        messageQueue.clear();
    }

    public void unsetListener() {
        synchronized (listenerSync) {
            listener = null;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public String blockingReadLine() throws InterruptedException {
        //System.out.println("Waiting for telnet input.");
        synchronized (listenerSync) {
            if (listener != null) {
                throw new UnsupportedOperationException("Cannot read from message queue, when Listener is enabled.");
            }
        }
        String msg = messageQueue.take();
       // try {
            return msg;
        //} finally {
        //    System.out.println("Received telnet input '" + msg + "'");
        //}
    }

    public void println(String msg) {
        channel.write(msg + "\n");
    }

    public void print(String msg) {
        channel.write(msg);
    }

    public void receivedLine(Channel channel, String line) {
        synchronized (listenerSync) {
            if (listener == null) {
                messageQueue.add(line);
            } else {
                listener.receivedLine(channel, line);
            }
        }
    }

    public void conectionClosed(Channel channel) {
        this.channel = null;
        quit("Channel disconnected");
    }
}
