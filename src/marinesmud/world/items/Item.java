/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.items;

import java.io.File;
import marinesmud.world.items.Item;
import marinesmud.world.items.list.PinkElephant;
import marinesmud.world.persistence.EnityManager;
import marinesmud.world.persistence.WorldEnity;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public abstract class Item extends WorldEnity {
    @Persistent private String name = "item name";
    @Persistent private String description = "item description";
    @Persistent private float value = 0.1f;
    @Persistent private boolean hidden = false;
    @Persistent private boolean wearable = true;
    @Persistent private boolean immobile = false;

    public Item() {
        super();
    }

    public Item(int id) {
        super(id);
    }

    public Item(int id, File f) {
        super(id, f);
    }

    public String getPassiveAction() {
        return "lie";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getValue() {
        return value;
    }

    @Override
    public EnityManager<Item> getManager() {
        return Item.Manager.getInstance();
    }

    @Override
    public String enityName() {
        return "item";
    }

    @Override
    public String toString() {
        return getName() + "^" + getId();
    }

    @Override
    public boolean needsCasting() {
        return true;
    }

    public static final class Manager extends EnityManager<Item> {
        private Manager() {
            super(ItemCaster.getInstance(), "item", 8, 9);
        }

        @Override
        protected void createFirst() {
            new PinkElephant(getFirstId());
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
