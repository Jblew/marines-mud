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
public class ExceptMeMessage extends Message {
    private final Being except;
    private final String message;

    public ExceptMeMessage(Being except, String message) {
        this.except = except;
        this.message = message;
    }

    @Override
    public boolean canHear(Being b) {
        return b != except;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
