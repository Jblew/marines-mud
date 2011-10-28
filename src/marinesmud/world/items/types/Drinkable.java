/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world.items.types;

/**
 *
 * @author jblew
 */
public interface Drinkable {
    /**
     *
     * @return thirst level decremention.
     */
    public float drink();

    public float getDrinkLevel();

    public boolean isDrinkLevelEnoughToDrink();
}
