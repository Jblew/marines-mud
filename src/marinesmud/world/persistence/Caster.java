/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world.persistence;

/**
 *
 * @author jblew
 */
public interface Caster {
    public Class<? extends WorldEnity> castTo(String castTo);
}
