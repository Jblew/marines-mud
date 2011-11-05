/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.items.list;

import java.io.File;
import marinesmud.world.items.Item;
import marinesmud.world.items.ItemCaster;

/**
 *
 * @author jblew
 */
public class PinkElephant extends Item {
    static {
        ItemCaster.getInstance().registerCast("pink_elephant", PinkElephant.class);
    }
    @Persistent private String name = "Pink elephant";
    @Persistent private String description = "Yu can see small pink elephant made of coloured glass. As you think, it's useless.";

    public PinkElephant() {
        super();
    }
    
    public PinkElephant(int id) {
        super(id);
    }

    public PinkElephant(int id, File f) {
        super(id, f);
    }

    @Override
    public long version() {
        return 10L;
    }

    @Override
    public String getCastTo() {
        return "pink_elephant";
    }

}
