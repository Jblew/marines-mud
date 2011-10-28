/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.containers;

import java.io.Serializable;

/**
 *
 * @author jblew
 */
public class Vector2f implements Serializable {
    public final float x;
    public final float y;

    public Vector2f(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
