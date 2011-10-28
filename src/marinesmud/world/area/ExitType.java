/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.area;

import marinesmud.world.beings.Being;
import marinesmud.world.beings.Player;

/**
 *
 * @author jblew
 */
public enum ExitType {
    NORMAL() {
        public boolean canPass(Being b) {
            return true;
        }

        public boolean canSee(Being b) {
            return true;
        }
    },
    HIDDEN() {
        public boolean canPass(Being b) {
            return true;
        }

        public boolean canSee(Being b) {
            return (b instanceof Player) && ((Player) b).isAdmin();
        }
    }, ADMIN() {
        public boolean canPass(Being b) {
            return (b instanceof Player) && ((Player) b).isAdmin();
        }

        public boolean canSee(Being b) {
            return (b instanceof Player) && ((Player) b).isAdmin();
        }
    };

    public abstract boolean canPass(Being b);

    public abstract boolean canSee(Being b);

    private ExitType() {
    }

    public static ExitType getDefault() {
        return NORMAL;
    }
}
