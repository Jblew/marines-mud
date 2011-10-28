/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.items;

import java.io.File;
import marinesmud.world.persistence.MultipleEnityManager;
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

    @Override
    protected void destruct() {
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
    public MultipleEnityManager<Item> getManager() {
        return Manager.getInstance();
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

    private static final class Manager extends MultipleEnityManager<Item> {
        private Manager() {
            super(ItemCaster.getInstance(), "item");
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
