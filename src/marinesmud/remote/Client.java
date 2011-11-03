/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.remote;

import com.esotericsoftware.kryonet.Connection;
import marinesmud.world.beings.Player;

/**
 *
 * @author jblew
 */
public class Client {
    public final Connection channel;
    private boolean loggedIn = false;
    private Player player;

    public Client(Connection channel) {
        this.channel = channel;
    }

    public synchronized void logIn(Player player) {
        this.player = player;
    }

    public synchronized void logOut() {
        this.player = null;
        this.loggedIn = false;
    }
    
    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized Player getPlayer() {
        if(player == null) throw new UnsupportedOperationException("This client is not loggedIn");
        return player;
    }
}
