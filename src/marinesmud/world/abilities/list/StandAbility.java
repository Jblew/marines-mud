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
public class StandAbility extends Ability {
    public Object execute(Being b, Object... parameters) throws AbilityExecutionException {
        switch (b.getPosition()) {
            case STAND:
                throw new AbilityExecutionException("Aren't you standing?");

            default:
                b.getRoom().sendMessage(new ExceptMeMessage(b, TextUtils.ucfirst(b.getName()) + " falls asleep."));
                b.setPosition(Position.STAND);
        }
        return null;
    }

    public static StandAbility getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final StandAbility INSTANCE = new StandAbility();
    }
}
