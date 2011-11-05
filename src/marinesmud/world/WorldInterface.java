/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world;

import marinesmud.world.communication.Message;

/**
 *
 * @author jblew
 */
public interface WorldInterface {
    public void sendMessage(Message msg);
    public long getSSID();
    public String hello();
}
