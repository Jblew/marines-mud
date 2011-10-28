/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ShortDescription;

/**
 *
 * @author jblew
 */
public class QuitCommands {

	private QuitCommands() {
	}

	public static QuitCommands getInstance() {
		return QuitCommandsHolder.INSTANCE;
	}

	private static class QuitCommandsHolder {

		private static final QuitCommands INSTANCE = new QuitCommands();
	}

	@ForAdmins
	@ShortDescription(description = "Natychmiastowo kończy grę i zamyka połączenie.")
	@MinimalCommandLength(length = 2)
	@NoParameters
	public void mquit(CommandRouter router, OutputManager outputManager) {
		/*outputManager.commandPrintln("{RWymuszasz wyjście z gry!{x");
		outputManager.commandPrintln("");
		router.exitGame();
		router.done();*/
	}

	@ShortDescription(description = "Kiedyś wylogowywało użytkownika do menu. Teraz tylko go wkurza.")
	@NoParameters
	@MinimalCommandLength(length = 2)
	public void quit(CommandRouter router, OutputManager outputManager) {
		outputManager.commandPrintln("{XHa! Nie ma tak dobrze. Jeżeli uważasz się za władcę świata, możesz spróbować użyć komendy mquit. W przeciwnym razie poszukaj jakiejś karczmy lub gospody i użyj komendy {Yrent{x{X.{x");
		router.done();
	}

	@ShortDescription(description = "Sprawdź, czy możesz sie wylogować i ile będzie cię kosztowało przechowanie rzeczy.")
	@NoParameters
	@MinimalCommandLength(length = 3)
	public void offer(CommandRouter router, OutputManager outputManager) {
		/*Room r = router.getUser().getRoom();
		if (r.getFeaturesManager().hasElement(RoomFeature.rent)) {
			try {
				outputManager.commandPrintln("{YKarczmarz wypisuje rachunek. Czekaj cierpliwie.{x");
				outputManager.commandFlushImmediately();
				try {
					TimeUnit.MILLISECONDS.sleep(1600);
				} catch (InterruptedException ex) {
					Logger.getLogger(QuitCommands.class.getName()).log(Level.SEVERE, null, ex);
				}

				List<MudObjectInstance> objects = router.getUser().getAllObjectInstances();
				TwoStringTuple[] pricelist = new TwoStringTuple[objects.size()];
				int i = 0;
				float totalCost = 0;
				for (MudObjectInstance moi : objects) {
					float cost = moi.getObject().getValue() * Config.getFloat("item cost per rent day factor");
					TwoStringTuple tst = new TwoStringTuple(moi.getObject().getName(), cost + "");
					pricelist[i] = tst;
					totalCost += cost;
					i++;
				}

				outputManager.commandPrintln("Karczmarz podnosi się i podtyka ci pod nos kartkę: ");
				outputManager.commandPrintln(HeaderBuilder.equalsHeader("Rachunek"));
				outputManager.commandPrintln("\n");
				outputManager.commandPrintln(TableBuilder.createTable(4, new String[]{"Przedmiot", "Koszt / dzień"}, pricelist));
				outputManager.commandPrintln("\n");
				outputManager.commandPrintln("W sumie będzie cię to kosztować " + totalCost + " dziennie.");
				outputManager.commandPrintln("\n");
				outputManager.commandPrintln(HeaderBuilder.equalsBottom());
			} catch (NoSuchMudObjectException ex) {
				Logger.getLogger(QuitCommands.class.getName()).log(Level.SEVERE, null, ex);
			}
			router.done();
		} else {
			router.addErrorMsg("Lepiej idź w tym celu do karczmy lub gospody!");
		}*/
	}

	@ShortDescription(description = "Wylogowuje użytkownika do menu")
	@NoParameters
	@MinimalCommandLength(length = 3)
	public void rent(CommandRouter router, OutputManager outputManager) {
		/*Room r = router.getUser().getRoom();
		if (r.getFeaturesManager().hasElement(RoomFeature.rent)) {
			offer(router, outputManager);
			outputManager.commandPrintln("Dematerializujesz się. Wszystkie twoje rzeczy zostaną przechowane w karczmie, lub zniszczone, jeżeli taka będzie wola Bezimiennych.");
			outputManager.commandPrintln("{RKończysz grę!{x");
			router.exitGame();
			router.done();
		} else {
			router.addErrorMsg("Lepiej idź w tym celu do karczmy lub gospody!");
		}*/
	}
}
