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
public abstract class Ability extends SingletonObject {
    protected Ability() {
        super();
    }
    public abstract void execute(Being b, Object... parameters) throws AbilityExecutionException;

    public void execute(Being b) throws AbilityExecutionException {
        execute(b, null);
    }
}
