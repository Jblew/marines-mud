/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.lib.logging;

/**
 *
 * @author jblew
 */
public class Level extends java.util.logging.Level {
    /**
     * NOTICE is a message level indicating not importane error, or some abnormal action. It has lower priority than Warning but higher than INFO. It's int value is 850.
     */
    public final static Level NOTICE = new Level("NOTICE", 850);

    /**
     * Create a named Level with a given integer value.
     * Note that this constructor is "protected" to allow subclassing. In general clients of logging should use one of the constant Level objects such as SEVERE or FINEST. However, if clients need to add new logging levels, they may subclass Level and define new constants.
     * @param name - the name of the Level, for example "SEVERE".
     * @param value - an integer value for the level.
     * @throws NullPointerException - if the name is null
     */
    protected Level(String name, int value) throws NullPointerException {
        super(name, value);
    }

    /**
     * Create a named Level with a given integer value and a given localization resource name.
     * @param name - the name of the Level, for example "SEVERE".
     * @param value - an integer value for the level.
     * @param resourceBundleName - name of a resource bundle to use in localizing the given name. If the resourceBundleName is null or an empty string, it is ignored.
     * @throws NullPointerException - if the name is null
     */
    protected Level(String name, int value, String resourceBundleName) throws NullPointerException {
        super(name, value, resourceBundleName);
    }
}
