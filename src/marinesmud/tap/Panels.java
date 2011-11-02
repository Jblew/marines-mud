/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.tap.commands.CommandRouter;
import marinesmud.lib.helpers.QuestionsHelper;
import marinesmud.system.Config;
import marinesmud.world.beings.Player;
import marinesmud.world.communication.ExceptMeMessage;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class Panels {
    public static Player login(TelnetAsProxyInstance tapi) throws IOException, InterruptedException {
        Player player = null;
        boolean loggedIn = false;
        int i = 0;
        while (!loggedIn) {
            if (i > 10) {
                tapi.println("{RDon't make fun ;){x");
                throw new CloseUserConnectionRuntimeException("Flood(>10) in login panel.");
            }
            String login = QuestionsHelper.prompt(tapi, "{XType your login: {x ");
            String password = QuestionsHelper.prompt(tapi, "Type your password: ");
            if (!login.isEmpty()) {
                if (Player.exists(login)) {
                    player = Player.getByName(login);
                    if (player.checkPassword(password)) {
                        if (player.isLoggedIn()) {
                            multiLogin(tapi, player);
                        } else {
                            player.login(login, password);
                            tapi.println("Logged in: " + login);
                            Logger.getLogger("Tap").log(Level.INFO, "{0} logged in...", login);
                            loggedIn = true;
                            return player;
                        }
                    } else {
                        wrongPassword(tapi);
                    }
                } else {
                    wrongPassword(tapi);
                }
            }
            i++;
        }
        return player;
    }

    private static void wrongPassword(TelnetAsProxyInstance tapi) throws IOException {
        tapi.println("{RWrong login lub password! If you want to create an account or restore your password, please visit our website.{x");
        tapi.println("");
    }

    private static void multiLogin(TelnetAsProxyInstance tapi, Player player) {
        tapi.println("\nSorry. You're already logged in.{x\n");
        /*if (QuestionsHelper.booleanPrompt(communicationManager, "Czy chcesz wylogowac sie z obu sesji, aby moc zalogowac sie jeszcze raz?")) {
        communicationManager.println("");
        communicationManager.println("");
        communicationManager.println("");
        communicationManager.println("{YWylogowujemy Cie. Zaloguj się ponownie.{x");
        user.getGamePlayManager().exitGameAndCloseConnection();
        String login = "";
        try {
        return LoginPanel.login(communicationManager);
        } catch (IOException ex) {
        Logger.getLogger(MultiLoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        } else {
        throw new CloseUserConnectionRuntimeException("Multiplie login! (Logout cancelled)");
        }
        throw new UnsupportedOperationException("This exception is impossible. Program couldn't get here, in class udLoginPanel.");*/
    }

    public static void showMenu(TelnetAsProxyInstance tapi, Player player) throws InterruptedException {
        boolean selected = false;
        while (!selected) {
            tapi.println(Config.get("asciiarts.menu"));

            tapi.println("");
            String opt = QuestionsHelper.prompt(tapi, "Type option number:");
            if (opt.equalsIgnoreCase("0")) {
                tapi.println("See you again!");
                throw new CloseUserConnectionRuntimeException("Exits game.");
            } else if (opt.equalsIgnoreCase("1")) {
                tapi.println("You're entering game! Good luck!");
                selected = true;
                return;
            } else {
                tapi.println("{YIncorrect option. Try again!{x");
            }
        }
    }
}
