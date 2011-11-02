/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.abilities.list;

import marinesmud.world.Direction;
import marinesmud.world.abilities.Ability;
import marinesmud.world.area.room.Exit;
import marinesmud.world.beings.Being;
import marinesmud.world.area.room.Room;

/**
 *
 * @author jblew
 */
public class LookAbility extends Ability {
    public String execute(Being b, Object... parameters)  {
        Room r = b.getRoom();
        String out =  "{X"+r.getName()+"{x\n";

        out += "{cExits: ";
        for(Direction d : r.exits.keySet()) {
            Exit e = r.exits.get(d);
            out += e.getName()+" ";
        }
        out += "\n{x";
        
        out += "\n\n";
        return out;
    }

    public static SayAbility getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final SayAbility INSTANCE = new SayAbility();
    }
}
