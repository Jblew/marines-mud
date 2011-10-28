/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import pl.jblew.code.jutils.utils.RandomUtils;
import pl.jblew.code.jutils.utils.TextUtils;
import marinesmud.world.beings.Being;
import marinesmud.world.Position;
import marinesmud.world.Direction;
import marinesmud.world.area.room.Exit;
import marinesmud.world.area.room.Room;

/**
 *
 * @author jblew
 */
public class CommunicationCommands {

    private CommunicationCommands() {
    }

    public static CommunicationCommands getInstance() {
        return CommunicationCommandsHolder.INSTANCE;
    }

    private static class CommunicationCommandsHolder {

        private static final CommunicationCommands INSTANCE = new CommunicationCommands();
    }

    @ShortDescription(description = "Powiedz coś")
    @MinParameters(count = 1, message = "Co chcesz powiedzieć?")
    @MinimalCommandLength(length = 2)
    public void say(CommandRouter router, OutputManager outputManager) /**/ {
        /*String out = router.getUser().getRace().getEncoder().encode(router.getCommandData());
        if (router.getUser().getStateHolder().getPosition() == Position.SLEEP) {
            outputManager.commandPrintln("Mamroczesz coś niezrozumiale przez sen.");
            router.getUser().getRoom().messageFromBeing(RandomUtils.getTextDestroyedForPercent(out, 25), router.getUser().getRace().getEncoder(), router.getUser(), new Being [] {router.getUser()}, "mamrocze przez sen");
        } else {
            outputManager.commandPrintln(TextUtils.ucfirst(router.getUser().getDictionaryElement("mowisz")) + ": '{Y" + out + "{x'.");
            router.getUser().getRoom().messageFromBeing(out, router.getUser().getRace().getEncoder(), router.getUser(), new Being [] {router.getUser()}, "mamrocze przez sen");
        }
        router.done();*/
    }

    @ShortDescription(description = "Powiedz coś komuś")
    @MinParameters(count = 2, message = "Co i komu chcesz powiedzieć?")
    @SleepPositionDisallowed(message = "Mamroczesz coś niezrozumiale przez sen.")
    @MinimalCommandLength(length = 2)
    public void tell(CommandRouter router, OutputManager outputManager) {
        /*Room r = router.getUser().getRoom();
        Being dstB = r.getBeingForName(router.getParams().get(0));
        if (dstB == null) {
            router.addErrorMsg("Nie ma tu nikogo takiego!");
            return;
        } else {
            String out = router.getCommandData().replaceFirst(router.getParams().get(0), "");
            String msg = router.getUser().getRace().getEncoder().encode(out);
            outputManager.commandPrintln(TextUtils.ucfirst(router.getUser().getRace().getDictionary().getElement("mowisz")) + " " + dstB.getCelownik() + " '" + msg + "'.");
            dstB.messageFromBeing(router.getUser(), TextUtils.ucfirst(router.getUser().getLogin()) + " " + router.getUser().getRace().getDictionary().getElement("mowi") + " ci '" + msg + "'.");
        }

        router.done();*/
    }

    @ShortDescription(description = "Krzycz!")
    @MinParameters(count = 1, message = "Co chcesz krzyknąć?")
    @SleepPositionDisallowed(message = "Przecież śpisz!")
    @MinimalCommandLength(length = 1)
    public void yell(CommandRouter router, OutputManager outputManager)  {
        /*outputManager.commandPrintln(TextUtils.ucfirst(router.getUser().getRace().getDictionary().getElement("krzyczysz")) + ": '{B" + router.getCommandData() + "{x'.");
        
        router.getUser().getRoom().messageFromBeing(router.getCommandData(), router.getUser().getRace().getEncoder(), router.getUser(), new Being [] {router.getUser()}, router.getUser().getRace().getDictionary().getElement("krzyczy"));
        
        Room r = router.getUser().getRoom();
        for (Direction d : r.getExitsManager().getExits().keySet()) {
            Exit e = r.getExitsManager().getExits().get(d);
            e.getTarget().messageFromBeing(router.getCommandData(), router.getUser().getRace().getEncoder(), router.getUser(), new Being [] {router.getUser()}, router.getUser().getRace().getDictionary().getElement("krzyczy"));
        }
        router.done();*/
    }
}
