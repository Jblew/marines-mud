/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.world;

/**
 * An enum describing position of an being.
 * @author jblew
 * @license Kod jest objęty licencją zawartą w pliku LICESNE
 */
public enum Position {
    /**
     * Being is standing.
     * In this mode hp and mv increment factor is [x1].
     * All standart commands are enabled.
     */
    STAND,
    /**
     * Being is sleeping.
     * In this mode hp and mv increment factor is [x2.25].
     * Many of standard commands are disabled.
     * Also almost all notifications are disabled.
     * Being cannot see, what is going on, when it's sleeping.
     */
    SLEEP,
    /**
     * Being is resting.
     * In this mode hp and mv increment factor is [x1.5].
     * All standart commands are enabled, except of commands that need standing, like walking.
     */
    REST,
    /**
     * Being is fighting.
     * In this mode hp and mv increment factor is [x0.75].
     * Many of standart commands are disabled. Being cannot change room while fighting.
     * In this position some special fight abilities becomes enabled, eg. Berserk command.
     */
    FIGHT
}
