/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.beings;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import marinesmud.world.persistence.Caster;
import marinesmud.world.persistence.WorldEnity;

/**
 *
 * @author jblew
 */
public class BeingCaster implements Caster {
    //private final Map<String, Class<? extends WorldEnity>> casts = new HashMap<String, Class<? extends WorldEnity>>();

    private BeingCaster() {
    }

    //public synchronized void registerCast(String name, Class<? extends Being> cls) {
    //    System.out.println("Registered cast for for '"+name+"'.");
    //    casts.put(name, cls);
    //}

    public synchronized Class<? extends WorldEnity> castTo(String castTo) {
        /*if (!casts.containsKey(castTo)) {
            System.out.println("No cast for '"+castTo+"'.");
            throw new NoSuchElementException();
        }
        return casts.get(castTo);*/
        if(castTo.equals("player")) return Player.class;
        else if(castTo.equals("mob")) return Mob.class;
        else throw new NoSuchElementException();
    }

    public static BeingCaster getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final BeingCaster INSTANCE = new BeingCaster();
    }
}
