/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

import marinesmud.world.area.Area;
import marinesmud.world.area.room.Room;
import marinesmud.world.beings.Being;
import marinesmud.world.items.Item;
import marinesmud.world.persistence.EnityManager;
import marinesmud.world.persistence.WorldEnity;

/**
 *
 * @author jblew
 */
public enum Enities {
    WORLD(World.class, World.Manager.getInstance()),
    AREA(Area.class, Area.Manager.getInstance()),
    ROOM(Room.class, Room.Manager.getInstance()),
    BEING(Being.class, Being.Manager.getInstance()),
    ITEM(Item.class, Item.Manager.getInstance());

    public final Class<? extends WorldEnity> cls;
    public final EnityManager<? extends WorldEnity> manager;

    private Enities(Class<? extends WorldEnity> cls, EnityManager<? extends WorldEnity> manager) {
        this.cls = cls;
        this.manager = manager;
    }

    public static void initAll() {
        for(Enities e : values()) {
            e.manager.init();
        }
    }
}
