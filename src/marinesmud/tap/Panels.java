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
import marinesmud.tap.Telnet.CommunicationManager;
import marinesmud.world.beings.Player;
import marinesmud.world.communication.ExceptMeMessage;
import marinesmud.world.communication.Message;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 * @author jblew
 */
public class Panels {
    public static Player login(Telnet.CommunicationManager communicationManager) throws IOException {
        Player player = null;
        boolean loggedIn = false;
        int i = 0;
        String login = QuestionsHelper.prompt(communicationManager, "{XType your login: {x ");
        while (!loggedIn) {
            if (i > 10) {
                communicationManager.println("{RDon't make fun ;){x");
                throw new CloseUserConnectionRuntimeException("Flood(>10) in login panel.");
            }
            if (login.isEmpty()) {
                if (i > 0) {
                    login = QuestionsHelper.prompt(communicationManager, "{XType your login: {x ");
                } else {
                    login = QuestionsHelper.prompt(communicationManager, "");
                }
            }
            if (!login.trim().isEmpty()) {

                if (Player.exists(login)) {
                    player = Player.getByName(login);
                    communicationManager.echoOff();
                    String password = QuestionsHelper.prompt(communicationManager, "Type yout password: ");
                    communicationManager.echoOn();
                    if (player.checkPassword(password)) {
                        if (player.isLoggedIn()) {
                            multiLogin(communicationManager, player);
                        } else {
                            player.login(login, password);
                            communicationManager.println("Logged in: " + login);
                            communicationManager.setCommunicationMode(player.getPreferredCommunicationMode());
                            Logger.getLogger("Tap").log(Level.INFO, "{0} logged in...", login);
                            loggedIn = true;
                            return player;
                        }
                    } else {
                        wrongPassword(communicationManager);
                    }
                } else {
                    wrongPassword(communicationManager);
                }
            }
            i++;
        }
        return player;
    }

    private static void wrongPassword(Telnet.CommunicationManager communicationManager) throws IOException {
        communicationManager.println("{RWrong login lub password! If you want to create an account or restore your password, please visit our website.{x");
        communicationManager.println("");
    }

    private static void multiLogin(Telnet.CommunicationManager communicationManager, Player player) {
        communicationManager.println("\nSorry. You're already logged in.{x\n");
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

    public static void showMenu(CommunicationManager communicationManager, Player player) {
        boolean selected = false;
        while (!selected) {
            communicationManager.println(Config.get("asciiarts.menu"));

            communicationManager.println("");
            String opt = QuestionsHelper.prompt(communicationManager, "Type option number:");
            if (opt.equalsIgnoreCase("0")) {
                communicationManager.println("See you again!");
                throw new CloseUserConnectionRuntimeException("Exits game.");
            } else if (opt.equalsIgnoreCase("1")) {
                communicationManager.println("You're entering game! Good luck!");
                selected = true;
                return;
            } else {
                communicationManager.println("{YIncorrect option. Try again!{x");
            }
        }
    }

    public static void showGame(Player player, Telnet.CommunicationManager comm) {
        OutputManager outputManager = new OutputManager(comm, player);
        CommandRouter commands = new CommandRouter(outputManager, player);

        outputManager.getCommunicationManager().println("\n\n---------------");
        commands.command("look");

        if (player.isAdmin()) {
            commands.command("todo");
        }

        player.getRoom().sendMessage(new ExceptMeMessage(player, TextUtils.ucfirst(player.getName()) + " materializuje się tutaj."));
        //user.getRoom().enterRoom(user);

        String lastInput = "";
        while (true) {
            String input = "";
            try {
                input = outputManager.getCommunicationManager().blockingReadLine().trim();
                if (input.trim().equals("!")) {
                    input = lastInput.trim();
                }

                if (input == null) {
                    break;
                } else if (!input.isEmpty()) {
                    outputManager.inputFromUser(input);
                    commands.command(input);
                } else {
                    outputManager.inputFromUser(input);
                }

                lastInput = input.trim();
            } catch (NullPointerException ex) {
                break;
            }
        }
    }
}
