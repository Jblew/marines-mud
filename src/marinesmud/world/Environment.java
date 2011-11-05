/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.world;

/**
 * Types of physical environment.
 * @author jblew
 */
public enum Environment {
    /**
     * Environment indoor. Weather here has litte influence on sitiuation inside INDOOR place.
     */
    INDOOR,
    /**
     * Here weather fully affects almost everything.
     */
    OUTDOOR,
    /**
     * It's special environment type created for some unusual
     * places like the Admin's White Room or some magical places.
     * Here weather does'n exist. It's always bright in an ISOLATED environment.
     */
    ISOLATED;
}
