/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.world.abilities.list.RestAbility;
import marinesmud.world.abilities.list.SleepAbility;
import marinesmud.world.abilities.list.StandAbility;
import marinesmud.world.abilities.list.WakeAbility;

/**
 *
 * @author jblew
 */
public class PositionCommands {
    private PositionCommands() {
    }

    public static PositionCommands getInstance() {
        return PositionCommandsHolder.INSTANCE;
    }

    private static class PositionCommandsHolder {
        private static final PositionCommands INSTANCE = new PositionCommands();
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Stand")
    public String stand(CommandRouter router) throws CommandExecutionException {
        try {
            StandAbility.getInstance().execute(router.getPlayer());
            return "You stand.";
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Rest")
    public String rest(CommandRouter router) throws CommandExecutionException {
        try {
            RestAbility.getInstance().execute(router.getPlayer());
            return "You sit and start resting.";
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Sleep")
    public String sleep(CommandRouter router) throws CommandExecutionException {
        try {
            SleepAbility.getInstance().execute(router.getPlayer());
            return "You fall asleep.";
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Wake up")
    public String wake(CommandRouter router) throws CommandExecutionException {
        try {
            WakeAbility.getInstance().execute(router.getPlayer());
            return "You wake up.";
        } catch (AbilityExecutionException ex) {
            throw new CommandExecutionException(ex.getMessage());
        }
    }
}
