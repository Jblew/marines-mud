/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap;

import org.jboss.netty.channel.Channel;

/**
 *
 * @author jblew
 */
public interface TelnetListener {
    public void receivedLine(Channel channel, String line);
    public void conectionClosed(Channel channel);
}
