/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ParametersCount;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.system.UptimeKeeper;
import pl.jblew.code.jutils.utils.RandomUtils;

/**
 *
 * @author jblew
 */
public class DebugCommands {

    private DebugCommands() {
    }

    public static DebugCommands getInstance() {
        return DebugCommandsHolder.INSTANCE;
    }

    private static class DebugCommandsHolder {

        private static final DebugCommands INSTANCE = new DebugCommands();
    }

    @ShortDescription(description = "Pokazuje, gdzie są jakie chmury.")
    @NoParameters
    @ForAdmins
    @MinimalCommandLength(length = 3)
    public void clouds(CommandRouter router, OutputManager outputManager) {
        /*outputManager.commandPrintln("PO ŚWIECIE WĘDRUJĄ NASTĘPUJĄCE CHMURY: ");
        for (Cloud c : Weather.getClouds()) {
            String out = c.getState().getName() + " - " + c.getRoom().getName() + " #" + c.getRoom().getVnum();
            if (c.getRoom().getCloud() == c) {
                out += " ZAREJESTROWANA";
            } else {
                out += " {RNIEZAREJESTROWANA{x";
            }
            outputManager.commandPrintln(out);
        }
        outputManager.commandPrintln("");
        router.done();*/
    }

    @ForAdmins
    @ShortDescription(description = "Wyświetla vnum roomu, w którym się znajdujesz.")
    @NoParameters
    @MinimalCommandLength(length = 2)
    public void vnum(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln("Znajdujesz sie w roomie : #" + router.getPlayer().getRoom().getId());
        router.done();
    }

    @ForAdmins
    @ShortDescription(description = "Wyświetla typ roomu, w którym się znajdujesz.")
    @NoParameters
    @MinimalCommandLength(length = 3)
    public void atype(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln("Typ roomu, w którym się znajdujesz, to: " + router.getPlayer().getRoom().getAreaType().name());
        router.done();
    }
    
    @ForAdmins
    @ShortDescription(description = "Pokazuje uptime serwera.")
    @NoParameters
    @MinimalCommandLength(length = 3)
    public void uptime(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln(UptimeKeeper.getTextUptime());
        outputManager.commandPrintln("");
        router.done();
    }

    @ForAdmins
    @ShortDescription(description = "Generuje błąd RuntimeException.")
    @ParametersCount(count = 1, notEnoughMessage = "{RJeśli jesteś pewien, że chcesz wywołać RuntimeException wpisz {xerror yes{R.{x")
    @MinimalCommandLength(length = 2)
    public void error(CommandRouter router, OutputManager outputManager) {
        if (!router.getParams().get(0).equalsIgnoreCase("yes")) {
            router.addErrorMsg("{RJeśli jesteś pewien, że chcesz wywołać RuntimeException wpisz {xerror yes{R.{x");
            return;
        } else {
            outputManager.commandPrintln("{Rthrow new RuntimeException()!{x");
            router.done();
            throw new RuntimeException();
        }
    }

    @ForAdmins
    @ShortDescription(description = "Wkurza admina ;)")
    @NoParameters
    @MinimalCommandLength(length = 2)
    public void ls(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln("Przezwyczajenia z konsoli ;D");
        router.done();
    }

    @ForAdmins
    @ShortDescription(description = "Rzuć kostką")
    @MinParameters(count = 1, message = "Jaką kostką chcesz rzucić?")
    @MinimalCommandLength(length = 2)
    public void random(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln("Rzucasz kostką mającą " + router.getParams().get(0) + " oczek. Wypada Ci: " + (RandomUtils.getInt(0, Integer.valueOf(router.getParams().get(0)) - 1) + 1));
        router.done();
    }

    @ShortDescription(description = "Komenda goto. Jest aliasowana, bo w javie słowo goto jest zastrzeżone")
    @ForAdmins
    @MinimalCommandLength(length = 2)
    @MinParameters(count = 1, message = "JGdzie chcesz się przenieść?")
    public void gt(CommandRouter router, OutputManager outputManager) {
        /*String in = router.getParams().get(0);
        if (TypeUtils.isInteger(in)) {
            int vnum = Integer.valueOf(in).intValue();
            if (World.roomExists(vnum)) {
                Room dest = World.getRoom(vnum);
                router.getUser().goTo(dest, true);
                outputManager.commandPrintln("Zostałeś przeniesiony do roomu #" + vnum + ".\n");
                outputManager.commandPrintln(dest.getLook(router.getUser()));
                outputManager.commandPrintln("");
            } else {
                outputManager.commandPrintln("{RTen room nie istnieje!{x");
                outputManager.commandPrintln("");
            }
        }
        else {
            MudUser mu = World.getUserInGameForName(in);
            if(mu == null) {
                outputManager.commandPrintln("{RNie ma nikogo ani niczego takiego!{x");
                outputManager.commandPrintln("");
            }
            else {
                router.getUser().goTo(mu.getRoom(), true);
                outputManager.commandPrintln("Zostałeś przeniesiony do roomu #" + mu.getRoom().getVnum() + ".\n");
                outputManager.commandPrintln(mu.getRoom().getLook(router.getUser()));
                outputManager.commandPrintln("");
            }
        }
        router.done();*/
    }
}
