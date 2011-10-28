/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import java.util.List;
import java.util.SortedMap;
import pl.jblew.code.jutils.data.containers.tuples.string.TwoStringTuple;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.lib.helpers.HeaderBuilder;
import marinesmud.lib.helpers.TableBuilder;
import marinesmud.world.World;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class IdeaBugTodoCommands {

	private IdeaBugTodoCommands() {
	}

	public static IdeaBugTodoCommands getInstance() {
		return IdeaBugTodoCommandsHolder.INSTANCE;
	}

	private static class IdeaBugTodoCommandsHolder {

		private static final IdeaBugTodoCommands INSTANCE = new IdeaBugTodoCommands();
	}

	@ShortDescription(description = "Możesz zgłosić błąd w grze")
	@MinParameters(count = 1, message = "Jaki błąd chcesz zgłosić?")
	@MinimalCommandLength(length = 2)
	public void bug(CommandRouter router, OutputManager outputManager) {//dodalem
		World.getInstance().report("BUG[" + router.getPlayer().getName() + "] " + router.getCommandData());
		outputManager.commandPrintln("{RDziękujemy!{x");
		router.done();
	}

	@ShortDescription(description = "Możesz zgłosić własny pomysł do gry")
	@MinParameters(count = 1, message = "Jaki pomysł chcesz zgłosić?")
	@MinimalCommandLength(length = 2)
	public void idea(CommandRouter router, OutputManager outputManager) {
		World.getInstance().report("IDEA[" + router.getPlayer().getName() + "] " + router.getCommandData());
		outputManager.commandPrintln("{RDziękujemy!{x");
		router.done();
	}

	@ForAdmins
	@ShortDescription(description = "Dodaj rzecz do zrobienia, lub sprawdź, co jeszcze trzeba zrobić.")
	@MinimalCommandLength(length = 2)
	public void todo(CommandRouter router, OutputManager outputManager) {
		/*List<String> params = router.getParams();
		List<String> reports = World.getInstance().getReports();

		if (params.size() > 0) {
			if (params.get(0).equalsIgnoreCase("add")) {
				if (params.size() < 2) {
					outputManager.commandPrintln("Co chcesz dodać?");
					router.done();
					return;
				}
				String[] parts = router.getCommandData().split(" ", 2);
				String data = parts[1];
				MudReporter.report("TODO[" + router.getPlayer().getName() + "] " + data);
				reports = MudReporter.getReports();
			} else if (params.get(0).equalsIgnoreCase("del")) {
				if (params.size() < 2) {
					outputManager.commandPrintln("Co chcesz usunąć?");
					router.done();
					return;
				}
				try {
					Integer.parseInt(params.get(1));
				} catch (NumberFormatException e) {
					outputManager.commandPrintln("To nie jest liczba: " + e.getMessage());
					router.done();
					return;
				}
				if (!reports.containsKey(Integer.valueOf(params.get(1)))) {
					outputManager.commandPrintln("Nie ma takiego zgłoszenia!");
					router.done();
					return;
				} else {
					int id = Integer.valueOf(params.get(1));
					MudReporter.removeReport(id);
					outputManager.commandPrintln("Usunięto " + reports.get(id) + ".");
					reports = MudReporter.getReports();
				}
			} else if (params.get(0).equalsIgnoreCase("look")) {
				if (params.size() < 2) {
					outputManager.commandPrintln("Co chcesz obejrzec?");
					router.done();
					return;
				}
				try {
					Integer.parseInt(params.get(1));
				} catch (NumberFormatException e) {
					outputManager.commandPrintln("To nie jest liczba: " + e.getMessage());
					router.done();
					return;
				}
				if (!reports.containsKey(Integer.valueOf(params.get(1)))) {
					outputManager.commandPrintln("Nie ma takiego zgłoszenia!");
					router.done();
					return;
				} else {
					int id = Integer.valueOf(params.get(1));
					outputManager.commandPrintln("{X"+reports.get(id)+"{x");
					outputManager.commandPrintln("");
					router.done();
					return;
				}
			} else if(params.size() > 3) {
				String[] parts = router.getCommandData().split(" ", 2);
				String data = parts[1];
				MudReporter.report("TODO[" + router.getPlayer().getName()) + "] " + data);
				reports = MudReporter.getReports();
			} else {
				router.addErrorMsg("Todo: Nie ma takiej opcji.");
				return;
			}
		}
		outputManager.commandPrintln(HeaderBuilder.equalsHeader("Zgłoszenia"));

		TwoStringTuple[] tableData = new TwoStringTuple[reports.size()];

		int i = 0;
		for (int id : reports.keySet()) {
			String description = reports.get(id);
			tableData[i] = new TwoStringTuple(id + "", TextUtils.shorten(description, 60));
			i++;
		}
		outputManager.commandPrintln(TableBuilder.createTable(3, new String[]{"#", "Opis"}, tableData));
		//outputManager.commandPrintln("");
		//outputManager.commandPrintln("{yKomendy:{x '{Xtodo{x' - zobacz listę zgłoszeń; '{Xtodo look <num>{x' - zobacz zgloszenie <num>; '{Xtodo add [text]{x' - dodaj zadanie do wykonania;"
		//		+ " '{Xtodo del [id]{x' - usuń zgłoszenie; '{Xbug [text]{x' - zgłoś błąd; '{Xidea [text]{x' - zgłoś nowy pomysł");
		outputManager.commandPrintln(HeaderBuilder.equalsBottom());
		router.done();*/
	}
}
