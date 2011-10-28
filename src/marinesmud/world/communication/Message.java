/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.communication;

import marinesmud.world.beings.Being;
import pl.jblew.code.libevent.Event;

/**
 *
 * @author jblew
 */
public abstract class Message implements Event {
    public abstract boolean canHear(Being b);
    public abstract String getMessage();
}
