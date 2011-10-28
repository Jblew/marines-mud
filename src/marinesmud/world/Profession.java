/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

/**
 *
 * @author jblew
 */
public enum Profession {
    WARRIOR, BARBARIAN;

    public static Profession getDefault() {
        return WARRIOR;
    }
}
