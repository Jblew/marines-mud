/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.items;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import marinesmud.world.persistence.Caster;
import marinesmud.world.persistence.WorldEnity;

/**
 *
 * @author jblew
 */
public class ItemCaster implements Caster {
    private final Map<String, Class<? extends WorldEnity>> casts = new HashMap<String, Class<? extends WorldEnity>>();

    private ItemCaster() {
    }

    public synchronized void registerCast(String name, Class<? extends WorldEnity> cls) {
        casts.put(name, cls);
    }

    public synchronized Class<? extends WorldEnity> castTo(String castTo) {
        if(!casts.containsKey(castTo)) throw new NoSuchElementException();
        return casts.get(castTo);
    }

        public static ItemCaster getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final ItemCaster INSTANCE = new ItemCaster();
    }
}
