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
public class SleepAbility extends Ability{
    public Object execute(Being b, Object... parameters) throws AbilityExecutionException {
        switch (b.getPosition()) {
            case SLEEP:
                throw new AbilityExecutionException("ZZzzzz...");

            default:
                b.getRoom().sendMessage(new ExceptMeMessage(b, TextUtils.ucfirst(b.getName()) + " falls asleep."));
                b.setPosition(Position.SLEEP);
        }
        return null;
    }

    public static SleepAbility getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final SleepAbility INSTANCE = new SleepAbility();
    }
}
