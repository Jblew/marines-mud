/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap.commands;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ShortDescription;

/**
 *
 * @author jblew
 */
public class MagicCommands {

    private MagicCommands() {
    }

    public static MagicCommands getInstance() {
        return MagicCommandsHolder.INSTANCE;
    }

    private static class MagicCommandsHolder {
        private static final MagicCommands INSTANCE = new MagicCommands();
    }

	@ShortDescription(description = "Rzucasz zaklęcie")
	@MinParameters(count = 1, message = "Jakie zaklęcie chcesz rzucić?")
	@MinimalCommandLength(length = 2)
	public void cast(CommandRouter router, OutputManager outputManager) {
		/*List<String> params = new LinkedList(router.getParams());
		Spell s = Spells.getSpellForName(params.get(0));

		if (s != null) {
			params.remove(0);
			s.cast(outputManager, router.getUser(), params);
			router.done();
		} else {
			router.addErrorMsg("Nie ma takiego czaru!");
		}*/
	}
 }
