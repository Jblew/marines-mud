/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.area;

import java.io.Serializable;
import marinesmud.world.Environment;

/**
 *
 * @author jblew
 */
public enum AreaType implements Serializable {
    GROUND(".", Environment.OUTDOOR),
    INDOOR("*", Environment.INDOOR),
    WAY("+", Environment.OUTDOOR),
    WHITE_ROOM("#", Environment.ISOLATED);
    public final String mapSign;
    public final Environment environment;

    private AreaType(String mapSign, Environment environment) {
        this.mapSign = mapSign;
        this.environment = environment;
    }

    public static AreaType getDefault() {
        return GROUND;
    }
}
