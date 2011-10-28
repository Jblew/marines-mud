/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;


import marinesmud.system.Config;
import marinesmud.world.beings.Being;
import marinesmud.world.Position;
import marinesmud.tap.commands.annotations.FightPositionDisallowed;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;
import pl.jblew.code.jutils.utils.TextUtils;
import marinesmud.world.area.room.Room;
import pl.jblew.code.jutils.utils.RandomUtils;

/**
 *
 * @author jblew
 */
public class FightCommands {

    private FightCommands() {
    }

    public static FightCommands getInstance() {
        return FightCommandsHolder.INSTANCE;
    }

    @ShortDescription(description = "Rozpoczyna walkę")
    @MinParameters(count = 1, message = "Kogo chcesz zabić?")
    @MinimalCommandLength(length = 2)
    @FightPositionDisallowed(message = "Przecież już walczysz.")
    public void kill(CommandRouter router, OutputManager outputManager) {
        /*Room r = router.getUser().getRoom();
        Being dstB = r.getBeingForName(router.getParams().get(0));
        if (dstB == null) {
            router.addErrorMsg("Nie ma tu nikogo takiego!");
            return;
        } else if (dstB == router.getUser()) {
            router.addErrorMsg("Nie możesz popełnić samobójstwa.");
            return;
        } else {
            outputManager.commandPrintln("{RWykrzykujesz zawołanie bitewne do " + dstB.getDopelniacz() + "!{x");
            dstB.messageFromSystem("{R" + TextUtils.ucfirst(router.getUser().getName()) + " wykrzykuje zawołanie bitewne do ciebie!{x");
            r.messageFromSystem(TextUtils.ucfirst(router.getUser().getName()) + " i " + dstB.getName() + " zaczynają walczyć!", new Being[]{dstB, router.getUser()});
            Fight f = new Fight(new Being[][]{new Being[]{router.getUser()}, new Being[]{dstB}});
            f.start();
            outputManager.commandPrintln("");
            router.done();
        }*/
    }

    @ShortDescription(description = "Dołącz do walki")
    @MinParameters(count = 1, message = "Komu chcesz pomóc?")
    @MinimalCommandLength(length = 2)
    @FightPositionDisallowed(message = "Przecież już walczysz.")
    public void assist(CommandRouter router, OutputManager outputManager) {
        /*Room r = router.getUser().getRoom();
        Being dstB = r.getBeingForName(router.getParams().get(0));
        if (dstB == null) {
            router.addErrorMsg("Nie ma tu nikogo takiego!");
            return;
        } else if (dstB.getStateHolder().getPosition() != Position.FIGHT) {
            router.addErrorMsg(TextUtils.ucfirst(dstB.getName()) + " nie walczy!");
            return;
        } else {
            outputManager.commandPrintln("{RWykrzykujesz zawołanie bitewne i pomagasz " + dstB.getCelownik() + " w walce!{x");
            dstB.messageFromSystem("{R" + TextUtils.ucfirst(router.getUser().getName()) + " wykrzykuje zawołanie bitewne i pomaga ci walczyć!{x");
            r.messageFromSystem(TextUtils.ucfirst(router.getUser().getName()) + " pomaga " + dstB.getCelownik() + " walczyć!", new Being[]{dstB, router.getUser()});
            dstB.getStateHolder().getFight().assist(router.getUser(), dstB);
            outputManager.commandPrintln("");
            router.done();
        }*/
    }

    @ShortDescription(description = "Kończy walkę")
    @NoParameters
    @MinimalCommandLength(length = 2)
    //@Todo(message="flee ma przenosić usera do poprzedniej lokalizacji, w ktorej byl")
    public void flee(CommandRouter router, OutputManager outputManager) {
        /*if (router.getUser().getStateHolder().getPosition() != Position.FIGHT) {
            router.addErrorMsg("Przecież nie walczysz!");
            return;
        }
        if (RandomUtils.getTrue(Config.getFloat("flee probability"))) {
            router.getUser().getStateHolder().stopFight();
            outputManager.commandPrintln("Uciekasz z walki.");

        } else {
            outputManager.commandPrintln(Config.get("cannot flee message"));
        }
        router.done();*/
    }

    private static class FightCommandsHolder {

        private static final FightCommands INSTANCE = new FightCommands();
    }
}
