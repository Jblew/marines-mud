/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.world.abilities.list.SayAbility;

/**
 *
 * @author jblew
 */
public class CommunicationCommands {

    private CommunicationCommands() {
    }

    public static CommunicationCommands getInstance() {
        return CommunicationCommandsHolder.INSTANCE;
    }

    private static class CommunicationCommandsHolder {

        private static final CommunicationCommands INSTANCE = new CommunicationCommands();
    }

    @ShortDescription(description = "Say something")
    @MinParameters(count = 1, message = "What do you want to say?")
    @MinimalCommandLength(length = 2)
    public String say(CommandRouter router) throws CommandExecutionException {
        try {
            return SayAbility.getInstance().execute(router.getPlayer(), router.getCommandData());
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }
}
