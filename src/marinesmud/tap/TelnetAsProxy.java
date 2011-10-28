/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.lib.SocketStats;
import pl.jblew.code.jutils.utils.IdGenerator;
import marinesmud.system.Config;
import marinesmud.tap.Telnet.CommunicationManager;
import marinesmud.world.beings.Player;
import pl.jblew.code.ccutils.ThreadsManager;
import pl.jblew.code.ccutils.ThreadsManager.TaskObject;


/**
 *
 * @author jblew
 */
public final class TelnetAsProxy implements Runnable {
    private SocketChannel socket = null;
    private final int id = IdGenerator.generate();
    private Telnet.CommunicationManager communicationManager = null;
    private Player player = null;
    private int timeoutTicks = 0;
    private final TaskObject taskObject;

    public TelnetAsProxy(SocketChannel socket_) {
        this.socket = socket_;
        this.communicationManager = new CommunicationManager(socket, this);

        taskObject = ThreadsManager.getGlobal().runLongTask(this);
    }

    @Override
    public void run() {
        try {
            SocketStats.printStats(socket);
            getCommunicationManager().print(Config.get("asciiarts.intro"));
            try {
                player = Panels.login(getCommunicationManager());
            } catch (NullPointerException e) {
                Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Not logged in!");
                return;
            }
            while (true) {
                Panels.showMenu(getCommunicationManager(), getPlayer());
                getPlayer().play();
                Panels.showGame(player, getCommunicationManager());
            }
        } catch (CloseUserConnectionRuntimeException ex) {
            quit(ex.getMessage());
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
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger("TelnetAsProxy").log(Level.SEVERE, null, ex);
        }
        if (player == null) {
            Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Stopped user (unknown) thread. Reason: {0}", reason);
        } else {
            Logger.getLogger("TelnetAsProxy").log(Level.INFO, "Stopped user ({0}) thread. Reason: {1}", new Object[]{player.getName(), reason});
        }
    }

    public int getTimeoutTicks() {
        return timeoutTicks;
    }

    public void incrementTimeoutTicks() {
        timeoutTicks++;
    }

    public void resetTimeoutTicks() {
        timeoutTicks = 0;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public Telnet.CommunicationManager getCommunicationManager() {
        return communicationManager;
    }

    public Player getPlayer() {
        return player;
    }
}
