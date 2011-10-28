/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.world.abilities.AbilityExecutionException;
import marinesmud.world.beings.Being;
import marinesmud.tap.OutputManager;
import marinesmud.world.Position;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.RestPositionDisallowed;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.tap.commands.annotations.StandPositionDisallowed;
import marinesmud.world.abilities.list.RestAbility;
import marinesmud.world.abilities.list.SleepAbility;
import marinesmud.world.abilities.list.StandAbility;
import marinesmud.world.abilities.list.WakeAbility;
import pl.jblew.code.jutils.utils.TextUtils;

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
    public void stand(CommandRouter router, OutputManager outputManager) {
        try {
            StandAbility.getInstance().execute(router.getPlayer());
            outputManager.commandPrintln("You stands.");
            router.done();
        } catch (AbilityExecutionException ex) {
            router.addErrorMsg(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Rest")
    public void rest(CommandRouter router, OutputManager outputManager) {
        try {
            RestAbility.getInstance().execute(router.getPlayer());
            outputManager.commandPrintln("You sits and starts resting.");
            router.done();
        } catch (AbilityExecutionException ex) {
            router.addErrorMsg(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Sleep")
    public void sleep(CommandRouter router, OutputManager outputManager) {
        try {
            SleepAbility.getInstance().execute(router.getPlayer());
            outputManager.commandPrintln("You falls asleep.");
            router.done();
        } catch (AbilityExecutionException ex) {
            router.addErrorMsg(ex.getMessage());
        }
    }

    @NoParameters
    @MinimalCommandLength(length = 2)
    @ShortDescription(description = "Wake up")
    public void wake(CommandRouter router, OutputManager outputManager) {
        try {
            WakeAbility.getInstance().execute(router.getPlayer());
            outputManager.commandPrintln("You wakes up.");
            router.done();
        } catch (AbilityExecutionException ex) {
            router.addErrorMsg(ex.getMessage());
        }
    }
}
