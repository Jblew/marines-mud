/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import marinesmud.system.Config;
import marinesmud.world.Position;
import marinesmud.lib.helpers.TextWrapper;
import marinesmud.system.threadmanagers.Tickers;
import marinesmud.world.beings.Player;

/**
 *
 * @author jblew
 */

/**
 *
 * @author jblew
 */
public class OutputManager {

    private final List<String> messagesToIgnore = Collections.synchronizedList(new LinkedList<String>());
    private boolean outputLocked = false;
    private final Telnet.CommunicationManager communicationManager;
    private final Player player;
    private BlockingQueue<String> stateMessages = new LinkedBlockingQueue<String>();
    private AtomicBoolean promptDisplayed = new AtomicBoolean(false);
    private String commandBuffer = "";
    private boolean autoPrompt = true;
    private final Runnable stateMessagesDispatcherRunnable = new Runnable() {

        @Override
        public void run() {
            if (stateMessages.size() > 0) {
                String out = "";
                while (true) {
                    String msg = stateMessages.poll();
                    if (msg == null) {
                        break;
                    } else {
                        out += msg + "\n";
                    }
                }
                prepareToDisplay();
                toOutput(out);
                afterDisplay();
            }
        }
    };

    public OutputManager(Telnet.CommunicationManager communicationManager, Player player) {
        this.communicationManager = communicationManager;
        this.player = player;

        Tickers.registerTickable(stateMessagesDispatcherRunnable, Tickers.Unit.TICK, 1);
    }

    public Telnet.CommunicationManager getCommunicationManager() {
        return communicationManager;
    }

    public void autoPromptOn() {
        autoPrompt = true;
        displayPrompt("");
    }

    public void autoPromptOff() {
        autoPrompt = false;
    }

    public void outputFromFight(String fightOutput, String prompt) {
        prepareToDisplay();
        toOutput(fightOutput + "\n");
        displayPrompt(prompt);
    }

    private void displayPrompt(String additionaly) {
        if (true) {
            String prompt = "\n";

            prompt += "{g<{x";
            //prompt += "{X" + getLogin() + "{x ";
            Position position = player.getPosition();
            if (position != Position.SLEEP) {
                prompt += player.getExperiencePoints() + "/" + player.getExperiencePointsNeededForNextLevel() + "exp ";
                prompt += player.getLevel() + "lv ";
            }
            prompt += player.getHealthPoints() + "/" + player.getMaxHealthPoints() + "hp ";
            prompt += player.getMovementPoints() + "/" + player.getMaxMovementPoints() + "mv ";
            if (position != Position.SLEEP) {
                prompt += " " + player.getMoney()+"$";
            } else {
                prompt += "...";
            }

            prompt += "{g> {x";

            prompt += additionaly;

            if (!outputLocked) {
                communicationManager.print(prompt);
            }
        }

        promptDisplayed.set(true);
    }

    public void messageFromRoom(String msg) {
        if (!messagesToIgnore.contains(msg)) {
            prepareToDisplay();
            toOutput(msg + "\n");
            afterDisplay();
        } else {
            messagesToIgnore.remove(msg);
        }
    }

    public void messageFromBeing(String msg) {
        prepareToDisplay();
        toOutput(msg + "\n");
        afterDisplay();
    }

    public void messageFromSystem(String msg) {
        prepareToDisplay();
        toOutput(msg + "\n");
        afterDisplay();
    }

    /*public void messageFromChannel(String msg, Channel channel) {
        if(enabledChannels.hasElement(channel)) messageFromSystem(msg);
    }*/

    public void stateNotification(String msg) {
        if (!stateMessages.contains(msg)) {
            stateMessages.add(msg);
        }
    }

    public void commandPrint(String str) {
        commandBuffer += str;
    }

    public void commandPrintln(String str) {
        commandBuffer += str + "\n";
    }

    public void commandModeOn() {
        commandBuffer = "";
        prepareToDisplay();
    }

    public void commandModeOff() {
        toOutput(TextWrapper.hardWrap(commandBuffer, Config.getInt("output width") + 2));
        afterDisplay();
    }

    public void ignoreMessage(String msg) {
        messagesToIgnore.add(msg);
    }

    public void inputFromUser(String input) {
        promptDisplayed.set(false);
        if (input.isEmpty() && autoPrompt) {
            displayPrompt("");
        }
    }

    public void close() {
        System.out.println("OutputManager.close()");
        Tickers.unregisterTickable(stateMessagesDispatcherRunnable);
        communicationManager.close();
    }

    private void prepareToDisplay() {
        if (promptDisplayed.get()) {
            if (!outputLocked) {
                communicationManager.println("");
            }
            promptDisplayed.set(false);
        }
    }

    private void afterDisplay() {
        if (autoPrompt) {
            displayPrompt("");
        }
    }

    private void toOutput(String msg) {
        if (!outputLocked) {
            communicationManager.print(msg);
        }
        promptDisplayed.set(false);
    }

    public void commandFlushImmediately() {
        commandModeOff();
        toOutput("\n");
        commandModeOn();
    }

    public void lockOutput() {
        outputLocked = true;
    }

    public void unlockOutput() {
        outputLocked = false;
    }
}