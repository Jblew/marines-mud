/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.beings;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author jblew
 */
public class Mob extends Being implements Serializable {
    
    public Mob() {
        super();
    }

    public Mob(int id) {
        super(id);
    }

    public Mob(int id, File f) {
        super(id, f);
    }

    @Override
    public String getCastTo() {
        return "mob";
    }
}
