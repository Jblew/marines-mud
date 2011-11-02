/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.world.beings.Being;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.lib.time.MudCalendar;
import marinesmud.world.World;
import marinesmud.world.beings.Player;
import marinesmud.world.communication.ExceptMeMessage;
import marinesmud.world.communication.Message;

/**
 *
 * @author jblew
 */
public class AdminCommands {
    private AdminCommands() {
    }

    public static AdminCommands getInstance() {
        return AdminCommandsHolder.INSTANCE;
    }

    private static class AdminCommandsHolder {
        private static final AdminCommands INSTANCE = new AdminCommands();
    }

    @ShortDescription(description = "Global message")
    @ForAdmins
    @MinParameters(count = 1, message = "What do you want to sav?")
    @SleepPositionDisallowed(message = "Nawet admin nie może gadać przez sen!")
    @MinimalCommandLength(length = 2)
    public String gecho(CommandRouter router) throws CommandExecutionException {
        if (router.getCommandData().isEmpty()) {
            throw new CommandExecutionException("What do you want to say?");
        }

        String out = router.getPlayer().getName() + " mówi globalnie '" + router.getCommandData() + "'.";
        World.getInstance().sendMessage(new ExceptMeMessage(router.getPlayer(), out));
        return "Mówisz wszystkim '" + router.getCommandData() + "'!";
    }

    @ShortDescription(description = "Wyświetla szczegółowe informacje o czasie i cyklu słonecznym")
    @ForAdmins
    @NoParameters
    @MinimalCommandLength(length = 3)
    public String mtime(CommandRouter router) {
        return MudCalendar.getInstance().getDateTime().getStringTime();
    }


    @ShortDescription(description = "Wysyła wiadomość do kanału rozmów nieśmiertelnych")
    @ForAdmins
    @MinParameters(count = 1)
    @MinimalCommandLength(length = 2)
    public String immtalk(final CommandRouter router) {
        //outputManager.commandPrintln(msg);
        final String msg = router.getCommandData();
        World.getInstance().sendMessage(new Message() {
            @Override
            public boolean canHear(Being b) {
                return (b instanceof Player && ((Player) b).isAdmin() && b != router.getPlayer());
            }

            @Override
            public String getMessage() {
                return msg;
            }
        });

        return msg;
    }
}
