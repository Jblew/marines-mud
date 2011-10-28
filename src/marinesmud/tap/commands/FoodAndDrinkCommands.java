/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package marinesmud.tap.commands;

import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.tap.OutputManager;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.ParametersCount;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import pl.jblew.code.jutils.utils.TextUtils;
import marinesmud.world.items.Item;
import marinesmud.world.items.types.Drinkable;

/**
 *
 * @author jblew
 */
public class FoodAndDrinkCommands {

    private FoodAndDrinkCommands() {
    }

    public static FoodAndDrinkCommands getInstance() {
        return FoodAndDrinkCommandsHolder.INSTANCE;
    }

    private static class FoodAndDrinkCommandsHolder {
        private static final FoodAndDrinkCommands INSTANCE = new FoodAndDrinkCommands();
    }

	@ShortDescription(description = "Zjedz coś")
	@ParametersCount(count = 1, notEnoughMessage = "Co chcesz zjeść?")
	@SleepPositionDisallowed(message = "Śnisz o wspaniałych potrawach.")
	@MinimalCommandLength(length = 2)
	public void eat(CommandRouter router, OutputManager outputManager) {
		/*try {
			if (router.getCommandData().isEmpty()) {
				return;
			}
			MudObjectInstance moi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getCommandData());
			if (moi == null) {
				router.addErrorMsg("Nie masz niczego takiego!");
				return;
			} else {
				Item m = moi.getObject();
				if (m.getType().isEatable()) {
					Eatable eatableObject = (Eatable) m.getType();
					if (eatableObject.isFoodLevelEnoughToEat(moi)) {
						int decrementVal = eatableObject.getStarvationDecrement(m);
						if (router.getUser().getStarvation() > decrementVal + 1) {
							router.getUser().decrementStarvation(decrementVal);
							eatableObject.decrementFoodLevel(moi);
							if (eatableObject.isFoodLevelEnoughToEat(moi)) {
								outputManager.commandPrintln("Odgryzasz kawałek {X" + m.getDopelniacz() + "{x.");
							} else {
								router.getUser().getInventory().remove(moi);
								outputManager.commandPrintln("Zjadasz {X" + m.getBiernik() + "{x.");
							}

						} else {
							outputManager.commandPrintln("Już więcej nie zmieścisz!");
						}
					} else {
						router.getUser().getInventory().remove(moi);
						outputManager.commandPrintln("Niestety, zostały same reztki. Wyrzucasz je za siebie.");
					}
				} else {
					outputManager.commandPrintln(TextUtils.ucfirst(m.getName()) + " nie nadaje się do jedzenia!");
				}
				router.done();
			}
		} catch (NoSuchMudObjectException ex) {
			Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
		}*/
	}

	@ShortDescription(description = "Wypij coś")
	@ParametersCount(count = 1, notEnoughMessage = "Co chcesz wypić?")
	@SleepPositionDisallowed(message = "Śnisz o mocnym piwie z beczki.")
	@MinimalCommandLength(length = 2)
	public void drink(CommandRouter router, OutputManager outputManager) {
		/*try {
			if (router.getCommandData().isEmpty()) {
				return;
			}
			MudObjectInstance moi = router.getUser().getInventory().getObjectInstanceForObjectName(router.getCommandData());
			if (moi == null) {
				router.addErrorMsg("Nie masz niczego takiego!");
				return;
			} else {
				Item m = moi.getObject();
				if (m.getType().isDrinkable()) {
					Drinkable drinkableObject = (Drinkable) m.getType();
					if (drinkableObject.isDrinkLevelEnoughToDrink(moi)) {
						int decrementVal = drinkableObject.getThirstDecrement(m);
						if (router.getUser().getThirst() > decrementVal + 1) {
							router.getUser().decrementThirst(decrementVal);
							drinkableObject.decrementDrinkLevel(moi);
							if (drinkableObject.isDrinkLevelEnoughToDrink(moi)) {
								outputManager.commandPrintln("Wypijasz trochę {X" + m.getDopelniacz() + "{x.");
							} else {
								router.getUser().getInventory().remove(moi);
								outputManager.commandPrintln("Wypijasz do końca {X" + m.getBiernik() + "{x.");
							}

						} else {
							outputManager.commandPrintln("Już więcej nie wypijesz!");
						}
					} else {
						router.getUser().getInventory().remove(moi);
						outputManager.commandPrintln("Niestety, to jest puste. Wyrzucasz to daleko za siebie!");
					}
				} else {
					outputManager.commandPrintln(TextUtils.ucfirst(m.getName()) + " nie nadaje się do picia!");
				}
				router.done();
			}
		} catch (NoSuchMudObjectException ex) {
			Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
		}*/
	}
 }
