/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.world.abilities.list.LookAbility;
/**
 *
 * @author jblew
 */
public class AreaCommands {

    private AreaCommands() {
    }

    public static AreaCommands getInstance() {
        return AreaCommandsHolder.INSTANCE;
    }

    private static class AreaCommandsHolder {
        private static final AreaCommands INSTANCE = new AreaCommands();
    }

    @ShortDescription(description = "Look around")
    @SleepPositionDisallowed(message = "You dream about far places, you are visiting...")
    @MinimalCommandLength(length = 1)
    public String look(CommandRouter router) throws CommandExecutionException {
        try {
            return (String) LookAbility.getInstance().execute(router.getPlayer());
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }
}