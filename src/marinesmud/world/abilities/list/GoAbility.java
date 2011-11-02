/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.abilities.list;

import marinesmud.world.Direction;
import marinesmud.world.abilities.Ability;
import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.world.beings.Being;

/**
 *
 * @author jblew
 */
public class GoAbility extends Ability {
    public Object execute(Being b, Object... parameters) throws AbilityExecutionException {
        if(parameters.length < 1 || !(parameters[0] instanceof Direction)) throw new IllegalArgumentException("GoAbility requires direction parameter.");
        return null;
    }

    public static GoAbility getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final GoAbility INSTANCE = new GoAbility();
    }
}
