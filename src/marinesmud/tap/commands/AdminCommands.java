/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.world.beings.Being;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ParametersCount;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.lib.helpers.QuestionsHelper;
import marinesmud.lib.time.MudCalendar;
import marinesmud.system.shutdown.MudShutdown;
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
    public void gecho(CommandRouter router, OutputManager outputManager) {
        if (router.getCommandData().isEmpty()) {
            return;
        }
        outputManager.commandPrint("Mówisz wszystkim '");
        outputManager.commandPrint(router.getCommandData());
        outputManager.commandPrintln("'!");
        String out = router.getPlayer().getName() + " mówi globalnie '" + router.getCommandData() + "'.";
        World.getInstance().sendMessage(new ExceptMeMessage(router.getPlayer(), out));
        router.done();
    }

    @ShortDescription(description = "Resetuj rooma")
    @ForAdmins
    @NoParameters
    @MinimalCommandLength(length = 2)
    public void reset(CommandRouter router, OutputManager outputManager) {
        //router.getUser().getRoom().resetMobs();
        //router.getUser().getRoom().getResetsManager().resetObjects();
        //router.done();
    }

    @ShortDescription(description = "Wyświetla szczegółowe informacje o czasie i cyklu słonecznym")
    @ForAdmins
    @NoParameters
    @MinimalCommandLength(length = 3)
    public void mtime(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln(MudCalendar.getInstance().getDateTime().getStringTime());
        router.done();
    }

    @ShortDescription(description = "Wyłącza muda")
    @ForAdmins
    @NoParameters
    @MinimalCommandLength(length = 7)
    public void quitserver(CommandRouter router, OutputManager outputManager) {
        if (QuestionsHelper.booleanPrompt(outputManager.getCommunicationManager(), "Czy jesteś pewien, że chcesz wyłączyć serwer")) {
            String pass = QuestionsHelper.prompt(outputManager.getCommunicationManager(), "Podaj swoje hasło:");
            if (router.getPlayer().checkPassword(pass)) {
                outputManager.commandPrintln("{Y**********************************{x");
                outputManager.commandPrintln("{Y*  {RZATRZYMUJĘ I WYŁĄCZAM SERWER{Y  *{x");
                outputManager.commandPrintln("{Y**********************************{x");
                outputManager.commandPrintln("");
                outputManager.commandFlushImmediately();
                MudShutdown.shutdown();
                router.done();
            } else {
                outputManager.commandPrintln("Podałeś złe hasło.");
            }
            router.done();
        }
    }

    @ShortDescription(description = "Restartuje muda")
    @ForAdmins
    @NoParameters
    @MinimalCommandLength(length = 7)
    public void restartserver(CommandRouter router, OutputManager outputManager) {
        if (QuestionsHelper.booleanPrompt(outputManager.getCommunicationManager(), "Czy jesteś pewien, że chcesz wyłączyć serwer")) {
            String pass = QuestionsHelper.prompt(outputManager.getCommunicationManager(), "Podaj swoje hasło:");
            if (router.getPlayer().checkPassword(pass)) {
                outputManager.commandPrintln("{Y**********************************{x");
                outputManager.commandPrintln("{Y*     {RRESTARTUJE SERWER{Y      *{x");
                outputManager.commandPrintln("{Y**********************************{x");
                outputManager.commandPrintln("{GMud zostanie zrestartowany, jeśli był uruchomiony przez skrypt run.sh. Zaloguj się za chwilę.{x");
                outputManager.commandPrintln("");
                outputManager.commandFlushImmediately();
                MudShutdown.restart();
                router.done();
            } else {
                outputManager.commandPrintln("Podałeś złe hasło.");
            }
            router.done();
        }
    }

    @ShortDescription(description = "Uzdrawia użytkownika")
    @ForAdmins
    @ParametersCount(count = 1)
    @MinimalCommandLength(length = 2)
    public void heal(CommandRouter router, OutputManager outputManager) {
        /*String userName = router.getParams().get(0);
        if (userName.equalsIgnoreCase("all") || userName.equalsIgnoreCase("*")) {
            for (Being b : router.getPlayer().getRoom().) {
                if (b instanceof Player) {
                    MudUser u = (MudUser) b;
                    u.getVariablesManager().set("mv", u.getVariablesManager().get("max_mv"));
                    u.getVariablesManager().set("hp", u.getVariablesManager().get("max_hp"));
                    outputManager.commandPrintln("Uzdrawiłem " + u.getBiernik() + ".");
                }
            }
            router.done();
        } else {
            Being b = router.getUser().getRoom().getBeingForName(userName);
            if (b != null && b.isUser()) {
                MudUser u = (MudUser) b;
                u.getVariablesManager().set("mv", "max_mv");
                u.getVariablesManager().set("hp", "max_hp");
                outputManager.commandPrintln("Uzdrawiłem " + u.getName() + ".");
            } else {
                router.addErrorMsg("Nie ma tu nikogo takiego!");
            }
        }*/
    }

    @ShortDescription(description = "Wysyła wiadomość do kanału rozmów nieśmiertelnych")
    @ForAdmins
    @MinParameters(count = 1)
    @MinimalCommandLength(length = 2)
    public void immtalk(CommandRouter router, OutputManager outputManager) {
        //outputManager.commandPrintln(msg);
        final String msg = router.getCommandData();
        World.getInstance().sendMessage(new Message() {

            @Override
            public boolean canHear(Being b) {
                return (b instanceof Player && ((Player)b).isAdmin());
            }

            @Override
            public String getMessage() {
                return msg;
            }
        });

        router.done();
    }
}
