/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.communication;

import marinesmud.world.beings.Being;

/**
 *
 * @author jblew
 */
public class MessageToEverybody extends Message {
    private final String message;

    public MessageToEverybody(String message) {
        this.message = message;
    }

    @Override
    public boolean canHear(Being b) {
        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
