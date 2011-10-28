/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marinesmud.tap.commands;

import java.util.Arrays;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import marinesmud.tap.commands.annotations.ForAdmins;
import marinesmud.tap.commands.annotations.MaxParameters;
import marinesmud.tap.commands.annotations.MinParameters;
import marinesmud.tap.commands.annotations.MinimalCommandLength;
import marinesmud.tap.commands.annotations.NoParameters;
import marinesmud.tap.commands.annotations.ParametersCount;
import marinesmud.tap.commands.annotations.RestPositionDisallowed;
import marinesmud.tap.commands.annotations.ShortDescription;
import marinesmud.tap.commands.annotations.SleepPositionDisallowed;
import marinesmud.tap.commands.annotations.StandPositionDisallowed;
import marinesmud.system.Config;
import pl.jblew.code.jutils.data.containers.tuples.FourTuple;
import marinesmud.tap.commands.annotations.Aliased;
import marinesmud.world.Position;
import marinesmud.tap.commands.annotations.FightPositionDisallowed;
import marinesmud.tap.OutputManager;
import marinesmud.world.beings.Player;
import pl.jblew.code.jutils.utils.ExceptionUtils;
import pl.jblew.code.jutils.utils.TextUtils;

/**
 *
 *  * @author jblew  * @license Kod jest objęty licencją zawartą w pliku LICESNE

 */
class BooleanStringMethodObjectTuple extends FourTuple<Boolean, String, Method, Object> {

    public BooleanStringMethodObjectTuple(Boolean b, String s, Method m, Object o) {
        super(b, s, m, o);
    }
}

public final class CommandRouter {

    private OutputManager outputManager = null;
    private Player player = null;
    private String input = null;
    private String commandName = null;
    private String commandData = "";
    private LinkedList<String> commandParams = new LinkedList<String>();
    private boolean commanded = false;
    private String errorMessage = "";
    private BooleanStringMethodObjectTuple[] commands = null;

    public CommandRouter(OutputManager outputManager, Player player) {
        this.outputManager = outputManager;
        this.player = player;
        
        try {
            commands = new BooleanStringMethodObjectTuple[]{
                        generateCommandTuple(false, "gecho", AdminCommands.getInstance()),
                        generateCommandTuple(false, "mtime", AdminCommands.getInstance()),
                        generateCommandTuple(false, "map", AreaCommands.getInstance()),
                        generateCommandTuple(true, "aliased_go", AreaCommands.getInstance()),
                        generateCommandTuple(false, "look", AreaCommands.getInstance()),
                        generateCommandTuple(false, "scan", AreaCommands.getInstance()),
                        generateCommandTuple(false, "immtalk", AdminCommands.getInstance()),
                        generateCommandTuple(false, "say", CommunicationCommands.getInstance()),
                        generateCommandTuple(false, "tell", CommunicationCommands.getInstance()),
                        generateCommandTuple(false, "yell", CommunicationCommands.getInstance()),
                        generateCommandTuple(false, "config", ConfigCommands.getInstance()),
                        //generateCommandTuple(false, "channels", ConfigCommands.getInstance()),
                        //generateCommandTuple(false, "prompt", ConfigCommands.getInstance()),
                        generateCommandTuple(false, "kill", FightCommands.getInstance()),
                        generateCommandTuple(false, "assist", FightCommands.getInstance()),
                        generateCommandTuple(false, "flee", FightCommands.getInstance()),
                        generateCommandTuple(false, "clouds", DebugCommands.getInstance()),
                        generateCommandTuple(false, "goto", "gt", DebugCommands.getInstance()),
                        generateCommandTuple(false, "error", DebugCommands.getInstance()),
                        generateCommandTuple(false, "gbug", DebugCommands.getInstance()),
                        generateCommandTuple(false, "gval", DebugCommands.getInstance()),
                        generateCommandTuple(false, "ls", DebugCommands.getInstance()),
                        generateCommandTuple(false, "uptime", DebugCommands.getInstance()),
                        generateCommandTuple(false, "random", DebugCommands.getInstance()),
                        generateCommandTuple(false, "atype", DebugCommands.getInstance()),
                        generateCommandTuple(false, "vnum", DebugCommands.getInstance()),
                        generateCommandTuple(false, "eat", FoodAndDrinkCommands.getInstance()),
                        generateCommandTuple(false, "drink", FoodAndDrinkCommands.getInstance()),
                        generateCommandTuple(false, "help", HelpCommand.getInstance()),
                        generateCommandTuple(false, "idea", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "bug", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "todo", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "reset", AdminCommands.getInstance()),
                        generateCommandTuple(false, "heal", AdminCommands.getInstance()),
                        generateCommandTuple(false, "who", InfoCommands.getInstance()),
                        generateCommandTuple(false, "time", InfoCommands.getInstance()),
                        generateCommandTuple(false, "level", InfoCommands.getInstance()),
                        generateCommandTuple(false, "score", InfoCommands.getInstance()),
                        generateCommandTuple(false, "cast", MagicCommands.getInstance()),
                        generateCommandTuple(false, "stand", PositionCommands.getInstance()),
                        generateCommandTuple(false, "rest", PositionCommands.getInstance()),
                        generateCommandTuple(false, "sleep", PositionCommands.getInstance()),
                        generateCommandTuple(false, "wake", PositionCommands.getInstance()),
                        generateCommandTuple(false, "quit", QuitCommands.getInstance()),
                        generateCommandTuple(false, "mquit", QuitCommands.getInstance()),
                        generateCommandTuple(false, "offer", QuitCommands.getInstance()),
                        generateCommandTuple(false, "rent", QuitCommands.getInstance()),
                        generateCommandTuple(true, "aliased_socials", SocialCommands.getInstance()),
                        generateCommandTuple(false, "inventory", StoresCommands.getInstance()),
                        generateCommandTuple(false, "equipment", StoresCommands.getInstance()),
                        generateCommandTuple(false, "take", StoresCommands.getInstance()),
                        generateCommandTuple(false, "get", "take", StoresCommands.getInstance()),
                        generateCommandTuple(false, "give", StoresCommands.getInstance()),
                        generateCommandTuple(false, "drop", StoresCommands.getInstance()),
                        generateCommandTuple(false, "wear", StoresCommands.getInstance()),
                        generateCommandTuple(false, "remove", StoresCommands.getInstance()),
                        generateCommandTuple(false, "quitserver", AdminCommands.getInstance()),
                        generateCommandTuple(false, "restartserver", AdminCommands.getInstance())};

        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {

            Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BooleanStringMethodObjectTuple generateCommandTuple(boolean isAliased, String commandName, Object instance) throws NoSuchMethodException {
        return (new BooleanStringMethodObjectTuple(isAliased, commandName, instance.getClass().getDeclaredMethod(commandName, CommandRouter.class, OutputManager.class), instance));
    }

    private BooleanStringMethodObjectTuple generateCommandTuple(boolean isAliased, String commandName, String methodName, Object instance) throws NoSuchMethodException {
        return (new BooleanStringMethodObjectTuple(isAliased, commandName, instance.getClass().getDeclaredMethod(methodName, CommandRouter.class, OutputManager.class), instance));
    }

    public void command(String input_) {
        outputManager.commandModeOn();
        input = input_;
        commanded = false;
        errorMessage = "";
        commandParams.clear();
        commandName = "";
        commandData = "";
        String logEntry = "";

        logEntry += "[" + player.getName() + "] " + input;

        List<Map<String, String>> fastCommandShortcut = Config.getList("fast commands shortcuts");
        for (Map<String, String> m : fastCommandShortcut) {
            String shortcut = m.get("shortcut");
            String command = m.get("command");
            if (input.trim().equalsIgnoreCase(shortcut)) {
                input = ("hh235h" + input).replace("hh235h" + shortcut, command);
                logEntry += " FCS{" + command + "}";
            } else if (input.trim().toLowerCase().startsWith(shortcut)) {
                input = ("hh235h" + input).replace("hh235h" + shortcut, command + " ");
                logEntry += " FCS{" + command + "=" + input + "}";
            }
        }

        String[] inputParts = input.split(" ", 2);
        commandName = inputParts[0];
        if (inputParts.length > 1) {
            commandData = inputParts[1].trim();
            commandParams.addAll(Arrays.asList(commandData.split(" ")));
        }

        for (BooleanStringMethodObjectTuple command : getCommands()) {
            try {
                if (!command.first) {
                    if (!command.third.isAnnotationPresent(ShortDescription.class)) {
                        throw new MissingAnnotationException("Missing annotation ShortDescription in method " + command.third.getName() + "!");
                    }
                    if (!command.third.isAnnotationPresent(MinimalCommandLength.class)) {
                        throw new MissingAnnotationException("Missing annotation MinimalCommandLength in method " + command.third.getName() + "!");
                    }
                }

                if (command.second.startsWith(commandName) && commandName.length() >= command.third.getAnnotation(MinimalCommandLength.class).length()) {
                    Position position = player.getPosition();
                    if (command.third.isAnnotationPresent(StandPositionDisallowed.class) && position == Position.STAND) {
                        addErrorMsg(command.third.getAnnotation(StandPositionDisallowed.class).message());
                        continue;
                    }
                    if (command.third.isAnnotationPresent(RestPositionDisallowed.class) && position == Position.REST) {
                        addErrorMsg(command.third.getAnnotation(RestPositionDisallowed.class).message());
                        continue;
                    }
                    if (command.third.isAnnotationPresent(SleepPositionDisallowed.class) && position == Position.SLEEP) {
                        addErrorMsg(command.third.getAnnotation(SleepPositionDisallowed.class).message());
                        continue;
                    }
                    if (command.third.isAnnotationPresent(FightPositionDisallowed.class) && position == Position.FIGHT) {
                        addErrorMsg(command.third.getAnnotation(FightPositionDisallowed.class).message());
                        continue;
                    }
                    if (command.third.isAnnotationPresent(ForAdmins.class) && !player.isAdmin()) {
                        continue;
                    }
                    if (command.third.isAnnotationPresent(NoParameters.class) && commandParams.size() > 0) {
                        addErrorMsg(TextUtils.ucfirst(command.third.getName()) + " nie wymaga żadnych parametrów. " + commandParams.size() + " to zbyt dużo.");
                        continue;
                    }
                    if (command.third.isAnnotationPresent(MinParameters.class) && commandParams.size() < command.third.getAnnotation(MinParameters.class).count()) {
                        addErrorMsg(command.third.getAnnotation(MinParameters.class).message().replace("%minParametersCount", String.valueOf(command.third.getAnnotation(MinParameters.class).count())).replace("%methodName", command.third.getName()));
                        continue;
                    }
                    if (command.third.isAnnotationPresent(MaxParameters.class) && commandParams.size() > command.third.getAnnotation(MaxParameters.class).count()) {
                        addErrorMsg(command.third.getAnnotation(MaxParameters.class).message().replace("%maxParametersCount", String.valueOf(command.third.getAnnotation(MaxParameters.class).count())).replace("%methodName", command.third.getName()));
                        continue;
                    }
                    if (command.third.isAnnotationPresent(ParametersCount.class) && commandParams.size() > command.third.getAnnotation(ParametersCount.class).count()) {
                        addErrorMsg(command.third.getAnnotation(ParametersCount.class).tooManyMessage().replace("%wantedParametersCount", String.valueOf(command.third.getAnnotation(ParametersCount.class).count())).replace("%methodName", command.third.getName()));
                        continue;
                    }
                    if (command.third.isAnnotationPresent(ParametersCount.class) && commandParams.size() < command.third.getAnnotation(ParametersCount.class).count()) {
                        addErrorMsg(command.third.getAnnotation(ParametersCount.class).notEnoughMessage().replace("%wantedParametersCount", String.valueOf(command.third.getAnnotation(ParametersCount.class).count())).replace("%methodName", command.third.getName()));
                        continue;
                    }
                    command.third.invoke(command.fourth, this, outputManager);
                    if (commanded) {
                        break;
                    }

                } else if (command.third.isAnnotationPresent(Aliased.class)) {
                    command.third.invoke(command.fourth, this, outputManager);
                    if (commanded) {
                        break;
                    }
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                outputManager.commandPrintln("Wystąpił błąd podczas wykonywania komendy. Przepraszamy.");
                if (player.isAdmin()) {
                    outputManager.commandPrintln(ExceptionUtils.getLongDescription(ex.getCause()));
                    ex.getCause().getStackTrace();
                }
                Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex.getCause());
                commanded = true;
                break;
            }
        }
        if (!commanded) {
            if (!errorMessage.isEmpty()) {
                outputManager.commandPrintln(errorMessage.replace("%parametersCount", String.valueOf(commandParams.size())));
            } else {
                outputManager.commandPrintln("Masz jakiś problem!?");
            }
        }
        Logger.getLogger("Commands").log(Level.INFO, logEntry);
        outputManager.commandModeOff();
    }

    public boolean addErrorMsg(String msg) {
        if (!errorMessage.isEmpty()) {
            return false;
        }
        errorMessage = msg;
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public String getCommandData() {
        return commandData;
    }

    public String getCommandName() {
        return commandName;
    }

    public List<String> getParams() {
        return Collections.unmodifiableList(commandParams);
    }

    public void done() {
        commanded = true;
    }

    public String getInput() {
        return input;
    }

    public void cleanCommands() {
    }

    public BooleanStringMethodObjectTuple[] getCommands() {
        return commands;
    }

    public void setCommands(BooleanStringMethodObjectTuple[] commands) {
        this.commands = commands;
    }

    void errorStop(String str) {
        outputManager.commandPrintln("{R" + str + "{x.");
        commanded = true;
    }
}
