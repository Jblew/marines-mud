/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.scl;

import marinesmud.game.gameplay.MudUser;
import org.jboss.netty.channel.Channel;

/**
 *
 * @author jblew
 */
public class Client {
    public final Channel channel;
    private boolean loggedIn = false;
    private MudUser user;

    public Client(Channel channel) {
        this.channel = channel;
    }

    public synchronized void logIn(MudUser user) {
        this.user = user;
    }

    public synchronized void logOut() {
        this.user = null;
        this.loggedIn = false;
    }
    
    public synchronized boolean isLoggedIn() {
        return loggedIn;
    }

    public synchronized MudUser getUser() {
        if(user == null) throw new UnsupportedOperationException("This client is not loggedIn");
        return user;
    }
}
