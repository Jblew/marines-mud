/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.world.World;

/**
 *
 * @author jblew
 */
public class IdeaBugTodoCommands {
    private IdeaBugTodoCommands() {
    }

    public static IdeaBugTodoCommands getInstance() {
        return IdeaBugTodoCommandsHolder.INSTANCE;
    }

    private static class IdeaBugTodoCommandsHolder {
        private static final IdeaBugTodoCommands INSTANCE = new IdeaBugTodoCommands();
    }

    @ShortDescription(description = "Report a bug")
    @MinParameters(count = 1, message = "What bug do you want to report?")
    @MinimalCommandLength(length = 2)
    public String bug(CommandRouter router) {//dodalem
        World.getInstance().report("BUG[" + router.getPlayer().getName() + "] " + router.getCommandData());
        return "{RThank you!{x\n";
    }

    @ShortDescription(description = "Report an idea")
    @MinParameters(count = 1, message = "What idea do you want to report?")
    @MinimalCommandLength(length = 2)
    public String idea(CommandRouter router) {//dodalem
        World.getInstance().report("IDEA[" + router.getPlayer().getName() + "] " + router.getCommandData());
        return "{RThank you!{x\n";
    }

    @ShortDescription(description = "Report a todo task")
    @MinParameters(count = 1, message = "What do you want to report?")
    @MinimalCommandLength(length = 2)
    @ForAdmins
    public String todo(CommandRouter router) {//dodalem
        World.getInstance().report("TODO[" + router.getPlayer().getName() + "] " + router.getCommandData());
        return "{RThank you!{x\n";
    }
}
