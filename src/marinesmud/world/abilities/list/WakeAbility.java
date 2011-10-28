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
public class WakeAbility extends Ability {
    public void execute(Being b, Object... parameters) throws AbilityExecutionException {
        switch (b.getPosition()) {
            case SLEEP:
                b.getRoom().sendMessage(new ExceptMeMessage(b, TextUtils.ucfirst(b.getName()) + " falls asleep."));
                b.setPosition(Position.STAND);
                break;

            default:
                throw new AbilityExecutionException("You aren't sleeping.");

        }
    }

    public static WakeAbility getInstance() {
        return (WakeAbility) InstanceHolder.INSTANCE;
    }
}
