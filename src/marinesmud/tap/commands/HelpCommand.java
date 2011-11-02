/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.lib.help.HelpManager;
import marinesmud.lib.help.HelpTopic;
import marinesmud.lib.helpers.HeaderBuilder;

/**
 *
 * @author jblew
 */
public class HelpCommand {
    private HelpCommand() {
    }

    public static HelpCommand getInstance() {
        return HelpCommandHolder.INSTANCE;
    }

    private static class HelpCommandHolder {
        private static final HelpCommand INSTANCE = new HelpCommand();
    }


    @ShortDescription(description = "Wyświetla listę dostępnych komend z ich opisami.")
    @MinimalCommandLength(length = 2)
    public String help(CommandRouter router) {
        String out = "";
        if (router.getParams().size() > 0) {
            HelpTopic[] helpTopics = HelpManager.findTopics(router.getCommandData());
            if (helpTopics.length == 0) {
                out += "{XNie znaleziono!{x\n";
            } else {
                for (HelpTopic topic : helpTopics) {
                    out += HeaderBuilder.equalsHeader(topic.getName()) + "\n";
                    out += topic.getContent() + "\n";
                    out += "\n";
                    out += "\n";
                }
            }
        } else {
            out += "Dostępne komendy: \n";
            for (BooleanStringMethodObjectTuple bsmto : router.getCommands()) {
                if (bsmto.third.isAnnotationPresent(ForAdmins.class) && !router.getPlayer().isAdmin()) {
                    continue;
                }

                out += "   {Y" + bsmto.third.getName() + "{x";
                if (bsmto.third.isAnnotationPresent(ShortDescription.class)) {
                    out += " - " + bsmto.third.getAnnotation(ShortDescription.class).description() + ".\n";
                } else {
                    out += " - brak opisu.\n";
                }
            }
            out += "\n";
            out += "Wpisz '{Xhelp temat_pomocy{x', aby uzyskać szczegółowe informacje na jakiś temat.\n";
        }
        return out;
    }
}
