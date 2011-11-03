/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.persistence;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author jblew
 */
public final class EnityShot implements Serializable {
    private final String name;
    private final int id;
    private final HashMap<String, Object> fields;

    public EnityShot(String name, int id, HashMap<String, Object> fields) {
        this.name = name;
        this.id = id;
        this.fields = fields;
    }

    public int getId() {
        return id;
    }

    public String getEnityName() {
        return name;
    }

    public Object get(String field) {
        return fields.get(field);
    }
}
