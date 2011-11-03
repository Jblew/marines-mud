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
    being(Being.class, Being.Manager.getInstance()),
    world(World.class, World.Manager.getInstance()),
    area(Area.class, Area.Manager.getInstance()),
    room(Room.class, Room.Manager.getInstance()),
    item(Item.class, Item.Manager.getInstance());

    public final Class<? extends WorldEnity> cls;
    public final EnityManager<? extends WorldEnity> manager;

    private Enities(Class<? extends WorldEnity> cls, EnityManager<? extends WorldEnity> manager) {
        this.cls = cls;
        this.manager = manager;
    }
}
