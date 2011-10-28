/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.NoParameters;
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
import marinesmud.tap.commands.annotations.Aliased;
import marinesmud.world.World;
import marinesmud.world.area.room.Room;
import marinesmud.world.beings.Player;
import marinesmud.world.communication.ExceptMeMessage;
import marinesmud.world.communication.Message;
/**
 *
 * @author jblew
 */
public class AreaCommands {

    private AreaCommands() {
    }

    public static AreaCommands getInstance() {
        return AreaCommandsHolder.INSTANCE;
    }

    private static class AreaCommandsHolder {

        private static final AreaCommands INSTANCE = new AreaCommands();
    }

    /*@NoParameters
    @SleepPositionDisallowed(message = "Lepiej śpij!")
    @ShortDescription(description = "Wyświetla małą mapkę.")
    @MinimalCommandLength(length = 2)
    public void map(CommandRouter router, OutputManager outputManager) {
        outputManager.commandPrintln(router.getUser().getRoom().drawMap(router.getUser()));
        router.done();
    }*/

    /*@Todo(message="Zamiast 'min mv to go' sprawdzac poziom mv roomo, do ktorego idzie user.")
    private boolean checkIfUserCanGo(Exit e, CommandRouter router, OutputManager outputManager) {
        Position position = router.getUser().getStateHolder().getPosition();
        if (position == Position.SLEEP || position == Position.REST) {
            router.addErrorMsg("Najpierw wstań!");
            return false;
        }
        if (position == Position.FIGHT) {
            router.addErrorMsg("Przecież walczysz.");
            return false;
        }
        if (router.getUser().getMv() < Config.getInt("min mv to go")) {
            outputManager.commandPrintln("Jesteś zbyt zmęczony, żeby gdzieś pójść. Lepiej usiądź i odpocznij.");
            router.done();
            return false;
        }
        return e.getType().canPass(router.getUser());
    }*/

    @ShortDescription(description = "Te komendy są odpowiedzialne za poruszanie się po świecie. North, South, West, East, Up, Down oraz nazwy pokoi")
    @Aliased
    public void aliased_go(CommandRouter router, OutputManager outputManager) /*throws NoSuchRoomException, NoSuchMudObjectException*/ {
        /*//S/ystem.ou/t.pri/ntln("invoke go");
        if (router.getCommandName().isEmpty()) {
            return;
        }
        boolean went = false;
        Room r = router.getUser().getRoom();
        for (Direction d : r.getExitsManager().getExits().keySet()) {
            Exit e = r.getExitsManager().getExits().get(d);
            if ((TextUtils.checkShortcut(e.getName(), router.getInput()) || d.getShortcutName().startsWith(router.getInput()))) {
                if (!checkIfUserCanGo(e, router, outputManager)) {
                    return;
                }

                router.getUser().getRoom().messageFromSystem(TextUtils.ucfirst(router.getUser().getLogin()) + " odchodzi na " + d.getMianownik() + ".",  new Being [] {router.getUser()});
                router.getUser().goTo(e.getTarget(), false);
                router.getUser().getRoom().messageFromSystem(TextUtils.ucfirst(router.getUser().getLogin()) + " przychodzi z " + d.getDopelniacz() + ".",  new Being [] {router.getUser()});
                went = true;
                locationLook(router.getUser().getRoom(), router, outputManager);
                break;
            }
        }
        if (!went && ("north".startsWith(router.getInput()) || "south".startsWith(router.getInput()) || "west".startsWith(router.getInput()) || "east".startsWith(router.getInput()) || "up".startsWith(router.getInput()) || "down".startsWith(router.getInput()))) {
            router.addErrorMsg("Nie możesz iść w tamtym kierunku!");
        }

        if (went) {
            router.done();
        }*/
    }

    private void locationLook(Room r, CommandRouter router, OutputManager outputManager) {
        //if (router.getUser().getGamePlayManager().isSmud()) {
        //    outputManager.commandPrintln(Config.get("smud html lt") + "clk /" + Config.get("smud html gt") + r.getLook(router.getUser()).replace("\n", "\n" + Config.get("smud html lt") + "lkl /" + Config.get("smud html gt")));
        //} else {
        //    outputManager.commandPrintln(r.getLook(router.getUser()));
        //}
    }

    @ShortDescription(description = "Rozejrzyj się po pokoju, w którym się znajdujesz")
    @SleepPositionDisallowed(message = "Śnisz o dalekich krainach...")
    @MinimalCommandLength(length = 1)
    public void look(CommandRouter router, OutputManager outputManager) /*throws NoSuchRoomException, NoSuchMudObjectException*/ {
        /*Room r = router.getUser().getRoom();
        if (r == null) {
            return;
        }
        if (router.getCommandData().isEmpty()) {
            locationLook(r, router, outputManager);
            router.done();
        } else {
            PersistentSynchronizedStringMap extraDescriptionsMap = r.getExtraDescriptions();

            for(String key : extraDescriptionsMap.keySet()) {
                if(TextUtils.checkShortcut(key, router.getCommandData())) {
                    outputManager.commandPrintln(extraDescriptionsMap.get(key));
                    router.done();
                    return;
                }
            }

            Being b = r.getBeingForName(router.getCommandData().toLowerCase());
            if (b != null) {
                outputManager.commandPrintln(b.getDescription());
                router.done();
                return;
            }

            MudObjectInstance moi = r.getResetsManager().getObjectInstanceForName(router.getCommandData().toLowerCase());
            if (moi != null) {
                outputManager.commandPrintln(moi.getObject().getDescription());
                router.done();
                return;
            }

            MudObjectInstance eqmoi = router.getUser().getEquipment().getObjectInstanceForObjectName(router.getCommandData().toLowerCase());
            if (eqmoi != null) {
                outputManager.commandPrintln(eqmoi.getObject().getDescription());
                router.done();
                return;
            }

            MudObjectInstance invmoi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getCommandData().toLowerCase());
            if (invmoi != null) {
                outputManager.commandPrintln(invmoi.getObject().getDescription());
                router.done();
                return;
            }

            router.addErrorMsg("Na co chcesz spojrzeć?");
        }*/
        router.addErrorMsg("Look?");
    }

    @ShortDescription(description = "Rozejrzyj się po najbliższych pokojach")
    @NoParameters
    @SleepPositionDisallowed(message = "Śnisz o dalekich krainach...")
    @MinimalCommandLength(length = 2)
    public void scan(CommandRouter router, OutputManager outputManager) /*throws NoSuchRoomException*/ {
        /*Room here = router.getUser().getRoom();
        outputManager.commandPrintln("{BTutaj:{x");
        boolean hasUser = false;
        for (Being b : here.getBeings()) {
            if (!b.getName().equals(router.getUser().getLogin())) {
                outputManager.commandPrintln("   " + b.getStateHolder().getAction() + " {G" + TextUtils.ucfirst(b.getName()) + "{x.");
                hasUser = true;
            }
        }
        if (!hasUser) {
            outputManager.commandPrintln("   nikogo tu nie widać.");
        }
        outputManager.commandPrintln("");

        for (Direction d : here.getExitsManager().getExits().keySet()) {
            Exit e = here.getExitsManager().getExits().get(d);
            if (e.getType().canSeeIt(router.getUser())) {
                outputManager.commandPrintln("{B" + TextUtils.ucfirst(e.getName()) + ":{x");
                hasUser = false;
                for (Being b : e.getTarget().getBeings()) {
                    if (!b.getName().equals(router.getUser().getLogin())) {
                        outputManager.commandPrintln("   " + b.getStateHolder().getAction() + " {G" + TextUtils.ucfirst(b.getName()) + "{x.");
                        hasUser = true;
                    }
                }
                if (!hasUser) {
                    outputManager.commandPrintln("   nikogo tu nie widać.");
                }
                outputManager.commandPrintln("");
            }
        }
        router.done();*/
    }
}