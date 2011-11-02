/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.area;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import marinesmud.world.persistence.WorldEnity;
import marinesmud.world.area.room.Room;
import marinesmud.world.persistence.EnityManager;
/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public final class Area extends WorldEnity {
@Persistent private String name = "area name";
    @Persistent private AreaType defaultAreaType = AreaType.getDefault();
    @Persistent private int minRoom = 0;
    @Persistent private int maxRoom = 0;
    @Persistent private int resetsFrequencyInTicks = 60;
    @Persistent private String[] texts = new String[]{};

    public Area(int id) {
        super(id);

        if (minRoom == 0 && maxRoom == 0) {
            int _minRoom = 0;
            for (Area a : Manager.getInstance().getElements()) {
                if (a.getMaxRoom() > _minRoom) {
                    _minRoom = a.getMaxRoom();
                }
            }
            minRoom = _minRoom + 1000;
            maxRoom = minRoom + 1000;
        }
    }

    public Area(int id, File f) {
        super(id, f);
    }

    @Override
    public void destruct() {
        super.destruct();
        List<Room> rooms = getRooms();
        for(Room r : rooms.subList(0, rooms.size())) {
            r.destruct();
        }
    }

    public synchronized String getName() {
        return name;
    }

    public synchronized AreaType getDefaultAreaType() {
        return defaultAreaType;
    }

    public synchronized int getMinRoom() {
        return minRoom;
    }

    public synchronized int getMaxRoom() {
        return maxRoom;
    }

    public synchronized int getResetsFrequencyInTicks() {
        return resetsFrequencyInTicks;
    }

    public synchronized List<Room> getRooms() {
        List<Room> out = new LinkedList<Room>();

        for (Room r : Room.Manager.getInstance().getElements()) {
            if (r.getArea() == this) {
                out.add(r);
            }
        }

        return out;
    }

    public synchronized void setRoomRange(int min, int max) {
        this.minRoom = min;
        this.maxRoom = max;
    }

    public synchronized String[] getTexts() {
        return texts;
    }

    @Override
    public synchronized EnityManager<Area> getManager() {
        return Manager.getInstance();
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String enityName() {
        return "area";
    }

    @Override
    public boolean needsCasting() {
        return false;
    }

    @Override
    public String getCastTo() {
        throw new UnsupportedOperationException("Area needn't casting.");
    }

    public static final class Manager extends EnityManager<Area> {
        private Manager() {
            super(Area.class, "area");
        }

        public static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        private static class InstanceHolder {
            public static final Manager INSTANCE = new Manager();
        }
    }
}
