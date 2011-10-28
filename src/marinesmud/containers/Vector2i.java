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
public class Vector2i implements Serializable {
    public final int x;
    public final int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
