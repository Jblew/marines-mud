/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.script.ScriptException;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.lib.time.MudCalendar;

/**
 *
 * @author jblew
 */
public class InfoCommands {
    private InfoCommands() {
    }

    public static InfoCommands getInstance() {
        return InfoCommandsHolder.INSTANCE;
    }

    private static class InfoCommandsHolder {

        private static final InfoCommands INSTANCE = new InfoCommands();
    }

    @ShortDescription(description = "Wyświetla listę zalogowanych użytkowników.")
    @NoParameters
    @MinimalCommandLength(length = 2)
    public void who(CommandRouter router, OutputManager outputManager) {
        /*outputManager.commandPrintln("Aktualnie po świecie chodzą: ");
        for (Object o : World.getPlayingUsers()) {
            MudUser m = (MudUser) o;
            outputManager.commandPrintln("   " + m.getLogin());
        }
        outputManager.commandPrintln("");
        router.done();*/
    }

    @ShortDescription(description = "Wyświetla aktualny mudowy czas.")
    @NoParameters
    @MinimalCommandLength(length = 2)
    public void time(CommandRouter router, OutputManager outputManager) {
        //String out = "Nie pamiętasz, jaki jest rok ani miesiąć. Wydaje Ci się jednak, że jest " + MudCalendar.getDayOfWeek() + " i prawdopodobnie ";
        //out += MudCalendar.getStringHour();
        //out += ".";
        outputManager.commandPrintln(MudCalendar.getInstance().getDateTime().getTimeCommandResponse());
        router.done();
    }

    @ShortDescription(description = "Wyświetla wynik twojej postaci")
    @NoParameters
    @SleepPositionDisallowed(message = "Śnisz o wszystkich umiejętnościach, jakie posiadłeś.")
    @MinimalCommandLength(length = 2)
    public void score(CommandRouter router, OutputManager outputManager) throws FileNotFoundException, ScriptException, IOException {
        /*outputManager.commandPrintln("Nazywasz się {G" + router.getUser().getName() + "{x.");

        outputManager.commandPrintln("Nazywasz się {B" + router.getUser().getLogin() + "{x.");
        outputManager.commandPrintln("");
        outputManager.commandPrintln("Masz {X" + router.getUser().getLevel() + "{xlevel.");
        outputManager.commandPrintln("Masz {X" + router.getUser().getHp() + "{x/" + router.getUser().getMaxHp() + "hp.");
        outputManager.commandPrintln("Masz {X" + router.getUser().getMv() + "{x/" + router.getUser().getMaxMv() + "mv.");
        outputManager.commandPrintln("Masz {X" + router.getUser().getExperiencePoints() + "{x/" + router.getUser().getMaxExperiencePoints() + "exp.");
        outputManager.commandPrintln("Głód: {X" + router.getUser().getStarvation() * 100 + "{x%.");
        outputManager.commandPrintln("Spragnienie: {X" + router.getUser().getThirst() * 100 + "{x%.");
        outputManager.commandPrintln("");
        outputManager.commandPrintln("Twoje statystyki: ");
        outputManager.commandPrintln("   Inteligencja: " + router.getUser().getInteligence());
        outputManager.commandPrintln("   Siła: " + router.getUser().getStrength());
        outputManager.commandPrintln("   Kondycja: " + router.getUser().getCondition());
        outputManager.commandPrintln("   Zręczność: " + router.getUser().getDexterity());
        outputManager.commandPrintln("   Wis: " + router.getUser().getWiseness());
        outputManager.commandPrintln("   Charyzma: " + router.getUser().getCharisma());
        outputManager.commandPrintln("   Szczęście: " + router.getUser().getLuck());
        outputManager.commandPrintln("");

        router.done();*/
    }

    @ShortDescription(description = "Pokazjue jaki masz poziom, oraz listę dostępnych poziomów.")
    @NoParameters
    @MinimalCommandLength(length = 3)
    public void level(CommandRouter router, OutputManager outputManager) {
        /*outputManager.commandPrintln("POZIOMY, JAKIE MOŻESZ ZBOBYĆ:");
        int i = 1;
        for (String levelName : router.getUser().getProfession().getLevels()) {
            if (i == router.getUser().getLevel()) {
                outputManager.commandPrintln("   {Y" + levelName + "{x <-- Jesteś na tym poziomie");
            } else if (router.getUser().getLevel() > i) {
                outputManager.commandPrintln("   {y" + levelName + "{x");
            } else {
                outputManager.commandPrintln("   " + levelName + "");
            }
            i++;
        }
        outputManager.commandPrintln("");
        router.done();*/
    }
}
