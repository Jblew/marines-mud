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
import pl.jblew.code.jutils.utils.RandomUtils;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class SayAbility extends Ability {
    public String execute(Being b, Object... parameters) throws AbilityExecutionException {
        if(parameters.length < 0 || !(parameters[0] instanceof String)) throw new IllegalArgumentException("SayAbility requires one parameter (String).");
        String msg = b.getRace().getEncoder().encode((String)parameters[0]);
        if(b.getPosition() == Position.SLEEP) {
            msg = RandomUtils.getTextDestroyedForPercent(msg, 45);
        }
        b.getRoom().sendMessage(new ExceptMeMessage(b, TextUtils.ucfirst(b.getName())+" says '"+msg+"'."));
        return msg;
    }

    public static SayAbility getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final SayAbility INSTANCE = new SayAbility();
    }
}
