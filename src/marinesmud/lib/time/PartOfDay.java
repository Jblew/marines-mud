/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.lib.time;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import marinesmud.system.Config;

/**
 *
 * @author jblew
 */
public class PartOfDay {

    public final String name;
    public final float sunPosition;
    public final float addToRoomDescriptionProbability;
    public final String [] roomDescriptions;
    public final String[] messages;

    private PartOfDay(String name, float sunPosition, Collection<String> messages, float addToRoomDescriptionProbability, Collection<String> roomDescriptions) {
        this.name = name;
        this.sunPosition = sunPosition;
        this.messages = new String[messages.size()];
        this.addToRoomDescriptionProbability = addToRoomDescriptionProbability;
        this.roomDescriptions = new String [roomDescriptions.size()];

        int i = 0;
        for (String elem : messages) {
            this.messages[i] = elem;
            i++;
        }

        i = 0;
        for (String elem : roomDescriptions) {
            this.roomDescriptions[i] = elem;
            i++;
        }
    }

    public static class Manager {

        private final Map<String, PartOfDay> partsOfDay = Collections.synchronizedMap(new HashMap<String, PartOfDay>());

        private Manager() {
            List<Map<String, Object>> mapList = (List<Map<String, Object>>) Config.getList("time.parts of day");
            for (Map<String, Object> map : mapList) {
                partsOfDay.put((String) map.get("name"),
                        new PartOfDay((String) map.get("name"),
                        Float.valueOf((String) map.get("sun position")),
                        (List<String>) map.get("messages"),
                        Float.valueOf((String) map.get("add to room description probability")),
                        (List<String>) map.get("room descriptions")));
            }
        }

        private static Manager getInstance() {
            return InstanceHolder.INSTANCE;
        }

        public static Collection<PartOfDay> getPartsOfDay() {
            return getInstance().partsOfDay.values();
        }

        public static PartOfDay getPartOfDay(String name) {
            if(!getInstance().partsOfDay.containsKey(name)) throw new NoSuchElementException("PartOfDay (name="+name+")");
            return getInstance().partsOfDay.get(name);
        }

        private static class InstanceHolder {

            private static final Manager INSTANCE = new Manager();
        }
    }
}
