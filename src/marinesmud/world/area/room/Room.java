/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.area.room;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.NoSuchElementException;
import marinesmud.lib.time.MudCalendar;
import marinesmud.world.Direction;
import pl.jblew.code.jutils.utils.MathUtils;
import marinesmud.world.persistence.WorldEnity;
import marinesmud.world.area.Area;
import marinesmud.world.area.AreaType;
import marinesmud.world.communication.Message;
import marinesmud.world.items.Item;
import marinesmud.world.persistence.MultipleEnityManager;
import pl.jblew.code.jutils.utils.RandomUtils;
import pl.jblew.code.libevent.EventManager;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class Room extends WorldEnity implements Serializable {
    @Persistent protected String name = "room name";
    @Persistent protected String baseText = "room description";
    @Persistent protected AreaType areaType = AreaType.getDefault();
    @Persistent protected boolean alwaysBright = false;
    @Persistent protected boolean alwaysDark = false;
    @Persistent protected boolean globalWeather = true;
    @Persistent protected boolean rent = false;
    @Persistent public final Map<Direction, Exit> exits = Collections.synchronizedMap(new HashMap<Direction, Exit>());
    @Persistent public final Map<Class<? extends Item>, Integer> resetItemQuantity = Collections.synchronizedMap(new HashMap<Class<? extends Item>, Integer>());
    @Persistent public final List<Item> items = Collections.synchronizedList(new ArrayList<Item>());
    public transient final EventManager<Message> messageEventManager = new EventManager<Message>();

    public Room() {
        super();
    }

    public Room(int id) {
        super(id);

        if (wasCreated()) {
            areaType = getArea().getDefaultAreaType();
        }
    }

    public Room(int id, File f) {
        super(id, f);
    }

    @Override
    protected void destruct() {
    }

    public synchronized void sendMessage(Message message) {
        messageEventManager.fireEvent(message);
    }

    public synchronized void resetObjects() {
        for (Class<? extends Item> cls : resetItemQuantity.keySet()) {
            int resetQuantity = resetItemQuantity.get(cls);
            int count = 0;
            for (Item item : items) {
                if (cls.isInstance(item)) {
                    count++;
                }
            }
            if (count < resetQuantity) {
                try {
                    for (int i = 0; i < (resetQuantity - count); i++) {
                        Item item = cls.newInstance();
                        this.items.add(item);
                    }
                } catch (InstantiationException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Room.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (count > resetQuantity) {
                for (int i = 0; i < (count - resetQuantity); i++) {
                    while(true) {
                        Item item = (Item) RandomUtils.chooseObjectRandomly(items.toArray());
                        if (cls.isInstance(item)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }
        }
        ListIterator<Item> it = items.listIterator();
        while(it.hasNext()) {
            Item item = it.next();
            boolean found = false;
            for (Class<? extends Item> cls : resetItemQuantity.keySet()) {
                if(cls.isInstance(item)) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                it.remove();
            }
        }
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized String getBaseText() {
        return baseText;
    }

    public synchronized AreaType getAreaType() {
        return areaType;
    }

    public synchronized Area getArea() {
        Collection<Area> areas = Area.Manager.getInstance().getElements();
        for (Area a : areas) {
            if (MathUtils.pointBelongsTo(getId(), a.getMinRoom(), a.getMaxRoom())) {
                return a;
            }
        }
        throw new NoSuchElementException("Room " + getId() + " doesn't fit any area!");
    }

    public synchronized boolean isBright() {
        if (alwaysBright) {
            return true;
        }

        if (alwaysDark) {
            return false;
        }

        return MudCalendar.getInstance().getDateTime().isPartOfDay("day");
    }

    private synchronized boolean hasGlobalWeather() {
        return globalWeather;
    }

    public synchronized int getResetsFrequencyInTicks() {
        return getArea().getResetsFrequencyInTicks();
    }

    @Override
    public MultipleEnityManager<Room> getManager() {
        return Manager.getInstance();
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String enityName() {
        return "room";
    }

    @Override
    public synchronized String toString() {
        return getName() + "^" + getId();
    }

    @Override
    public boolean needsCasting() {
        return false;
    }

    @Override
    public String getCastTo() {
        throw new UnsupportedOperationException("Room needn't casting.");
    }

    public static final class Manager extends MultipleEnityManager<Room> {
        private Manager() {
            super(Room.class, "room");
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
