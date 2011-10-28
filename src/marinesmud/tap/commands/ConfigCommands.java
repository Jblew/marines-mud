/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import java.util.LinkedList;
import java.util.List;
import marinesmud.tap.commands.annotations.MaxParameters;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.lib.helpers.TableBuilder;
import marinesmud.tap.CommunicationMode;
import pl.jblew.code.jutils.data.containers.tuples.string.StringTuple;
import pl.jblew.code.jutils.data.containers.tuples.string.ThreeStringTuple;
import pl.jblew.code.jutils.utils.EnumUtils;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class ConfigCommands {

    private ConfigCommands() {
    }

    public static ConfigCommands getInstance() {
        return ConfigCommandsHolder.INSTANCE;
    }

    private static class ConfigCommandsHolder {

        private static final ConfigCommands INSTANCE = new ConfigCommands();
    }

    @ShortDescription(description = "Zmienia ustawienia użytkownika. Np. 'config code' zmienia zestaw znaków.")
    @MinimalCommandLength(length = 2)
    public void config(CommandRouter router, OutputManager outputManager) {
        List<String> params = router.getParams();
        if (params.isEmpty()) {
            router.addErrorMsg("Musisz podać opcję, którą chcesz zmienić. Narazie możesz zmienić tylko kodowanie znaków, wpisując 'config codeset <nazwa kodowania>'");
            return;

        }
        if ("codeset".startsWith(params.get(0).toLowerCase()) || "codeset".equalsIgnoreCase(params.get(0))) {
            if (params.size() < 2) {
                outputManager.commandPrintln("{YDostepne kodowania znakow{x:");
                outputManager.commandPrintln("   {Yutf{x   - Utf 8 Nie zawsze obsługuje duże S (z ogonkiem) i N (z ogonkiem) przy wprowadzaniu.");
                outputManager.commandPrintln("   {Yiso{x   - Iso-8859-2. Obsługuje wszystkie polskie znaki.");
                outputManager.commandPrintln("   {Ywin{x   - Windows-1250. Nie był jeszcze testowany.");
                outputManager.commandPrintln("   {Ynopol{x - Bez polskich liter. Jest to domyślne kodowanie. Wszystkie polskie litery są usuwane.");
                outputManager.commandPrintln("");
                router.done();
                return;
            }
            String cName = router.getParams().get(1);
            if (CommunicationMode.modeExists(cName)) {
                outputManager.getCommunicationManager().setCommunicationMode(CommunicationMode.valueOf(cName));
                router.getPlayer().setPreferredCommunicationMode(CommunicationMode.valueOf(cName));
                outputManager.commandPrintln("TEST: ");
                outputManager.commandPrintln("   Zażółć gęślą jaźń!");
                outputManager.commandPrintln("   ąĄęĘćĆńŃśŚóÓźŹżŻ");
                router.done();
            } else {
                router.addErrorMsg("Nieprawidlowe kodowanie! Dostępne kodowania, to: {Xutf{x, {Xiso{x, {Xwin{x, {Xnopol{x.");
                return;
            }
        } /*else if (router.getUser().isAdmin() && ("vnum".startsWith(params.get(0).toLowerCase()) || "vnum".equalsIgnoreCase(params.get(0)))) {
            if (router.getUser().getDisplayVnum()) {
                router.getUser().getVariablesManager().set("display_vnum", "0");
            } else {
                router.getUser().getVariablesManager().set("display_vnum", "1");
            }
            outputManager.commandPrintln(router.getUser().getRoom().getLook(router.getUser()));
            router.done();
        } else if (router.getUser().isAdmin() && ("holylight".startsWith(params.get(0).toLowerCase()) || "holylight".equalsIgnoreCase(params.get(0)))) {
            if (router.getUser().getHolyLight()) {
                router.getUser().getVariablesManager().set("holy_light", "0");
            } else {
                router.getUser().getVariablesManager().set("holy_light", "1");
            }
            outputManager.commandPrintln(router.getUser().getRoom().getLook(router.getUser()));
            router.done();
        }*/ else {
            router.addErrorMsg("Nieprawidlowy paramter!");
            return;
        }
    }
}
