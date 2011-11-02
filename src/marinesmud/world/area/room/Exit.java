/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.area.room;

import java.io.Serializable;
import marinesmud.world.beings.Being;
import marinesmud.world.area.ExitType;

/**
 *
 * @author jblew
 */
public class Exit implements Serializable {
    private int targetId;
    private final String name;
    private final ExitType type;

    public Exit(Room target, String name_, ExitType type_) {
        targetId = target.getId();
        name = name_;
        type = type_;
    }

    public synchronized Room getTarget() {
        return Room.Manager.getInstance().getElement(targetId);
    }

    public synchronized void setTarget(Room r) {
        targetId = r.getId();
    }

    public String getName() {
        return name;
    }

    public ExitType getType() {
        return type;
    }

    public boolean canPass(Being b) {
        return type.canPass(b);
    }

    @Override
    public String toString() {
        return targetId + "^" + getName() + "^" + getType().name();
    }

    /*public static Exit fromString(String in) { //before ^ it was ##
        String[] parts = in.split("^");
        if (parts.length != 3) {
            throw new RuntimeException("Malformed exit string!");
        }

        return new Exit(Integer.valueOf(parts[0]), parts[1], ExitType.Manager.getType(parts[2]));
    }*/
}
