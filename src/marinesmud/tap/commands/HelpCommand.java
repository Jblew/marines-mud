/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.OutputManager;
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
	public void help(CommandRouter router, OutputManager outputManager) {
		if (router.getParams().size() > 0) {
			HelpTopic [] helpTopics = HelpManager.findTopics(router.getCommandData());
			if (helpTopics.length == 0) {
				outputManager.commandPrintln("{XNie znaleziono!{x");
			} else {
				for (HelpTopic topic : helpTopics) {
					outputManager.commandPrintln(HeaderBuilder.equalsHeader(topic.getName()));
					outputManager.commandPrintln(topic.getContent());
					outputManager.commandPrintln("");
					outputManager.commandPrintln("");
				}
			}
			router.done();
		} else {
			outputManager.commandPrintln("Dostępne komendy: ");
			for (BooleanStringMethodObjectTuple bsmto : router.getCommands()) {
				if (bsmto.third.isAnnotationPresent(ForAdmins.class) && !router.getPlayer().isAdmin()) {
					continue;
				}

				outputManager.commandPrint("   {Y" + bsmto.third.getName() + "{x");
				if (bsmto.third.isAnnotationPresent(ShortDescription.class)) {
					outputManager.commandPrintln(" - " + bsmto.third.getAnnotation(ShortDescription.class).description() + ".");
				} else {
					outputManager.commandPrintln(" - brak opisu.");
				}
			}
			outputManager.commandPrintln("");
			outputManager.commandPrintln("Wpisz '{Xhelp temat_pomocy{x', aby uzyskać szczegółowe informacje na jakiś temat.");
			router.done();
		}
	}
}
