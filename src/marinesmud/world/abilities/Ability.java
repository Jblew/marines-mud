/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.abilities;

import marinesmud.world.beings.Being;
import pl.jblew.code.jutils.SingletonObject;

/**
 *
 * @author jblew
 */
public abstract class Ability {
    protected Ability() {
    }
    
    public abstract Object execute(Being b, Object... parameters) throws AbilityExecutionException;

    public Object execute(Being b) throws AbilityExecutionException {
        return execute(b, null);
    }
}
