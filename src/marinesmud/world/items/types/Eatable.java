/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.items.types;

/**
 *
 * @author jblew
 */
public interface Eatable {
    /**
     *
     * @return starvation level decremention.
     */
    public float eat();

    public float getFoodLevel();

    public boolean isFoodLevelEnoughToDrink();
}
