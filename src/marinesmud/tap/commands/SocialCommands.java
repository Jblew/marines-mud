/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap.commands;

import marinesmud.tap.commands.annotations.Aliased;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class SocialCommands {

    private SocialCommands() {
    }

    public static SocialCommands getInstance() {
        return SocialCommandsHolder.INSTANCE;
    }

    private static class SocialCommandsHolder {
        private static final SocialCommands INSTANCE = new SocialCommands();
    }

	@ShortDescription(description = "Komendy emocji. Wpisz socials aby zobaczyć ich listę.")
	@SleepPositionDisallowed(message = "Wykrzywiasz gębę przez sen.")
        @Aliased
	public void aliased_socials(CommandRouter router, OutputManager outputManager) {
		/*if (router.getInput().length() > 2) {
			if ("socials".startsWith(router.getInput()) && router.getInput().length() > 2) {
				outputManager.commandPrintln("{BDostępne komendy emocji: {x");
				for (Social s : Social.Manager.getInstance().getSocials()) {
					outputManager.commandPrintln("   {Y" + TextUtils.ucfirst(s.command) + "{x - " + TextUtils.ucfirst(s.notargetInvoker));
				}
				router.done();
				return;
			}

			boolean targeted = false;
			if (router.getParams().size() > 0) {
				targeted = true;
			} else if (router.getParams().size() > 1) {
				router.addErrorMsg("Socials: Nie możesz wykonywać kilku gestów na raz.");
				return;
			}
			if (router.getInput().length() > 2) {
				for (Social s : Social.Manager.getInstance().getSocials()) {
					if (s.command.startsWith(router.getCommandName())) {
						if (targeted) {
							if (s.isTargeted) {
								s.showTargeted(outputManager, router.getUser(), router.getParams().get(0));
								router.done();
							} else {
								router.addErrorMsg("Nie możesz wykonać tego gestu do konkretnej osoby.");
								return;
							}
						} else {
							s.showNoTarget(outputManager, router.getUser());
							router.done();
						}
						return;
					}
				}
			}
		}*/
	}
 }
