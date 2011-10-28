/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap.commands;

import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ParametersCount;
import marinesmud.tap.commands.annotations.RestPositionDisallowed;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;

/**
 *
 * @author jblew
 */
public class StoresCommands {

    private StoresCommands() {
    }

    public static StoresCommands getInstance() {
        return StoresCommandsHolder.INSTANCE;
    }

    private static class StoresCommandsHolder {
        private static final StoresCommands INSTANCE = new StoresCommands();
    }

	@ShortDescription(description = "Lista przedmiotów w plecaku")
	@NoParameters
	@SleepPositionDisallowed(message = "Śnisz o pięknych rzeczach, które posiadasz...")
	@MinimalCommandLength(length = 2)
	public void inventory(CommandRouter router, OutputManager outputManager) {
		/*outputManager.commandPrintln("");
		outputManager.commandPrintln("Nosisz przy sobie: ");
		Map<String, Integer> objectNames = router.getUser().getInventory().toStringCountableArray();
		for (String s : objectNames.keySet()) {
			if (s != null && !s.trim().isEmpty()) {
				int count = objectNames.get(s);
				if (count == 1) {
					outputManager.commandPrintln("   " + TextUtils.ucfirst(s) + "");
				} else {
					outputManager.commandPrintln("   (" + count + ") " + TextUtils.ucfirst(s) + "");
				}
			}
		}
		outputManager.commandPrintln("");
		router.done();*/
	}

	@ShortDescription(description = "Podnieś przedmiot i włóż do plecaka")
	@ParametersCount(count = 1, notEnoughMessage = "Co chcesz wziąć?")
	@RestPositionDisallowed(message = "Najpierw wstań!")
	@SleepPositionDisallowed(message = "Najpierw wstań!")
	@MinimalCommandLength(length = 2)
	public void take(CommandRouter router, OutputManager outputManager) {
		/*Room r = router.getUser().getRoom();
		MudObjectInstance moi = r.getResetsManager().getObjectInstanceForName(router.getParams().get(0));
		if (moi == null) {
			router.addErrorMsg("Nie ma tu niczego takiego!");
			return;
		} else {
			r.getResetsManager().removeObjectInstance(moi);
			//MudLogger.info("User add objectInstance #" + moi.getId());
			router.getUser().getInventory().add(moi);
			outputManager.commandPrintln("Wziąłeś {X" + moi.getObject().getBiernik() + "{x");
			router.done();
		}*/
	}

	@ShortDescription(description = "Daj komuś przedmiot.")
	@ParametersCount(count = 2, notEnoughMessage = "Co i komu chcesz dać?")
	@SleepPositionDisallowed(message = "Najpierw się obudź!")
	@MinimalCommandLength(length = 2)
	public void give(CommandRouter router, OutputManager outputManager) {
		/*Room r = router.getUser().getRoom();
		Being dstB = r.getBeingForName(router.getParams().get(0));
		if (dstB == null && !dstB.isUser()) {
			router.addErrorMsg("Nie ma tu nikogo takiego!");
		} else {
			MudUser dstU = (MudUser) dstB;
			MudObjectInstance moi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getParams().get(1));
			if (moi == null) {
				router.addErrorMsg("Nie masz niczego takiego!");
				return;
			} else {
				dstU.getInventory().add(moi);
				router.getUser().getInventory().remove(moi);
				try {
					outputManager.commandPrintln("Dałeś {X" + moi.getObject().getBiernik() + "{x " + dstU.getCelownik() + ".");
				} catch (NoSuchMudObjectException ex) {
					Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
				}
				router.done();
			}
		}*/
	}

	@ShortDescription(description = "Wyrzuć przedmiot z plecaka")
	@ParametersCount(count = 1, notEnoughMessage = "Co chcesz wyrzucić?")
	@RestPositionDisallowed(message = "Najpierw wstań!")
	@SleepPositionDisallowed(message = "Najpierw wstań!")
	@MinimalCommandLength(length = 2)
	public void drop(CommandRouter router, OutputManager outputManager) {
		/*if (router.getCommandData().isEmpty()) {
			return;
		}
		MudObjectInstance moi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getCommandData());
		if (moi == null) {
			router.addErrorMsg("Nie masz niczego takiego!");
			return;
		} else {
			router.getUser().getRoom().getResetsManager().addObjectInstance(moi);
			router.getUser().getInventory().remove(moi);
			outputManager.commandPrintln("Upuściłeś {X" + moi.getObject().getBiernik() + "{x.");
			router.done();
		}*/
	}

	
	@ShortDescription(description = "Lista przedmiotów, które nosisz na sobie")
	@NoParameters
	@SleepPositionDisallowed(message = "Śnisz o pięknych ubraniach, które nosisz...")
	@MinimalCommandLength(length = 2)
	public void equipment(CommandRouter router, OutputManager outputManager) {
            /*
		outputManager.commandPrintln("");
		outputManager.commandPrintln("Nosisz na sobie: ");
		String strList = router.getUser().getEquipment().strList();
		outputManager.commandPrintln("");
		outputManager.commandPrintln("   " + strList.replace("\n", "\n   "));
		router.done();*/
	}

	@ShortDescription(description = "Nałóż coś na siebie")
	@MinParameters(count = 1, message = "Co chcesz nałożyć?")
	@SleepPositionDisallowed(message = "Najpierw wstań!")
	@MinimalCommandLength(length = 3)
	public void wear(CommandRouter router, OutputManager outputManager) {
		/*MudObjectInstance moi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getParams().get(0));
		if (moi == null) {
			router.addErrorMsg("Nie masz niczego takiego!");
			return;
		} else {
			Item mo = moi.getObject();
			if (!mo.getFeaturesManager().hasElement(MudObjectFeature.not_wearable)) {
				EnumManager<EquipmentLocation> wearManager = mo.getWearLocationsManager();
				for (EquipmentLocation l : wearManager.getElements()) {
					try {
						for (EquipmentLocation el : l.getCommonLocations()) {
							if (!router.getUser().getEquipment().isLocationEmpty(el)) {
								router.addErrorMsg("Zdejmij najpierw " + router.getUser().getEquipment().get(el).getObject().getBiernik() + "!");
								continue;
							}
						}
						if (router.getUser().getEquipment().isLocationEmpty(l)) {
							router.getUser().getInventory().remove(moi);
							router.getUser().getEquipment().put(l, moi);
							outputManager.commandPrintln(l.getActiveAction().replace("%s", "{X" + moi.getObject().getName() + "{x"));
                                                        router.getUser().getRoom().messageFromSystem(l.getActiveRoomMessage().replace("%s", "{X" + moi.getObject().getBiernik() + "{x").replace("%n", "{X" + TextUtils.ucfirst(router.getUser().getName()) + "{x"), new Being[] {router.getUser()});
							router.done();
							break;
						} else {
							router.addErrorMsg("Zdejmij najpierw " + router.getUser().getEquipment().get(l).getObject().getBiernik() + "!");
						}
					} catch (NotEmptyEquipmentLocationException ex) {
						Logger.getLogger(StoresCommands.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			} else {
				router.addErrorMsg("Nie możesz tego nałożyć!");
			}
		}*/
	}

	@ShortDescription(description = "Zdejmij coś z siebie.")
	@MinParameters(count = 1, message = "Co chcesz zdjąć?")
	@SleepPositionDisallowed(message = "Najpierw wstań!")
	@MinimalCommandLength(length = 3)
	public void remove(CommandRouter router, OutputManager outputManager) {
		/*MudObjectInstance moi = router.getUser().getEquipment().getObjectInstanceForObjectName(router.getParams().get(0));
		if (moi == null) {
			router.addErrorMsg("Nie masz niczego takiego!");
			return;
		} else {
			router.getUser().getEquipment().remove(moi);
			router.getUser().getInventory().add(moi);
			outputManager.commandPrintln("Zdejmujesz " + moi.getObject().getBiernik() + ".");
			outputManager.commandPrintln("");
			router.done();
		}*/
	}
 }
