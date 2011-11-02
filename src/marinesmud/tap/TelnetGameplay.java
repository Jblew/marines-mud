/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import marinesmud.tap.commands.CommandRouter;
import marinesmud.world.beings.Player;
import marinesmud.world.communication.ExceptMeMessage;
import org.jboss.netty.channel.Channel;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class TelnetGameplay implements TelnetListener {
    private final TelnetAsProxyInstance tapi;
    private final Player player;
    private final CommandRouter commandRouter;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public TelnetGameplay(TelnetAsProxyInstance tapi, Player player) {
        this.tapi = tapi;
        this.player = player;
        this.commandRouter = new CommandRouter(this, player);
    }

    public void run() {
        tapi.setListener(this);

        tapi.println("\n\n");
        tapi.println(commandRouter.command("look"));

        player.getRoom().sendMessage(new ExceptMeMessage(player, TextUtils.ucfirst(player.getName()) + " materializuje się tutaj."));

        while(running.get()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    public void receivedLine(Channel channel, String line) {
        tapi.println(commandRouter.command(line));
    }

    public void conectionClosed(Channel channel) {
        throw new UnsupportedOperationException("TelnetGameplay does not support conectionClosed(Channel) from TelnetListener interface.");
    }
}
