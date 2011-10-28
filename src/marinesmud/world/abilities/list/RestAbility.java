/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.abilities.list;

import marinesmud.world.abilities.Ability;
import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.world.beings.Being;
import marinesmud.world.Position;
import marinesmud.world.communication.ExceptMeMessage;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class RestAbility extends Ability {
    public void execute(Being b, Object... parameters) throws AbilityExecutionException {
        switch (b.getPosition()) {
            case REST:
            case SLEEP:
                throw new AbilityExecutionException("You are already resting.");

            default:
                b.getRoom().sendMessage(new ExceptMeMessage(b, TextUtils.ucfirst(b.getName()) + " sits and starts resting."));
                b.setPosition(Position.REST);
        }
    }

    public static RestAbility getInstance() {
        return (RestAbility) InstanceHolder.INSTANCE;
    }
}