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
import marinesmud.tap.TelnetGameplay;
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
    private final TelnetGameplay telnetGameplay;
    private final Player player;
    private String input = null;
    private String commandName = null;
    private String commandData = "";
    private LinkedList<String> commandParams = new LinkedList<String>();
    private BooleanStringMethodObjectTuple[] commands = null;

    public CommandRouter(TelnetGameplay telnetGameplay, Player player) {
        this.telnetGameplay = telnetGameplay;
        this.player = player;

        try {
            commands = new BooleanStringMethodObjectTuple[]{
                        generateCommandTuple(false, "gecho", AdminCommands.getInstance()),
                        generateCommandTuple(false, "mtime", AdminCommands.getInstance()),
                        generateCommandTuple(false, "look", AreaCommands.getInstance()),
                        generateCommandTuple(false, "immtalk", AdminCommands.getInstance()),
                        generateCommandTuple(false, "say", CommunicationCommands.getInstance()),
                        generateCommandTuple(false, "help", HelpCommand.getInstance()),
                        generateCommandTuple(false, "idea", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "bug", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "todo", IdeaBugTodoCommands.getInstance()),
                        generateCommandTuple(false, "stand", PositionCommands.getInstance()),
                        generateCommandTuple(false, "rest", PositionCommands.getInstance()),
                        generateCommandTuple(false, "sleep", PositionCommands.getInstance()),
                        generateCommandTuple(false, "wake", PositionCommands.getInstance())
                    };
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {

            Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private BooleanStringMethodObjectTuple generateCommandTuple(boolean isAliased, String commandName, Object instance) throws NoSuchMethodException {
        return (new BooleanStringMethodObjectTuple(isAliased, commandName, instance.getClass().getDeclaredMethod(commandName, CommandRouter.class), instance));
    }

    private BooleanStringMethodObjectTuple generateCommandTuple(boolean isAliased, String commandName, String methodName, Object instance) throws NoSuchMethodException {
        return (new BooleanStringMethodObjectTuple(isAliased, commandName, instance.getClass().getDeclaredMethod(methodName, CommandRouter.class), instance));
    }

    public String command(String input_) {
        String out = "";
        String errorMessage = "";
        boolean commandFine = false;
        input = input_;
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
                        throw new CommandExecutionException(command.third.getAnnotation(StandPositionDisallowed.class).message());
                    }
                    if (command.third.isAnnotationPresent(RestPositionDisallowed.class) && position == Position.REST) {
                        throw new CommandExecutionException(command.third.getAnnotation(RestPositionDisallowed.class).message());
                    }
                    if (command.third.isAnnotationPresent(SleepPositionDisallowed.class) && position == Position.SLEEP) {
                        throw new CommandExecutionException(command.third.getAnnotation(SleepPositionDisallowed.class).message());
                    }
                    if (command.third.isAnnotationPresent(FightPositionDisallowed.class) && position == Position.FIGHT) {
                        throw new CommandExecutionException(command.third.getAnnotation(FightPositionDisallowed.class).message());
                    }
                    if (command.third.isAnnotationPresent(ForAdmins.class) && !player.isAdmin()) {
                        continue;
                    }
                    if (command.third.isAnnotationPresent(NoParameters.class) && commandParams.size() > 0) {
                        throw new CommandExecutionException(TextUtils.ucfirst(command.third.getName()) + " nie wymaga żadnych parametrów. " + commandParams.size() + " to zbyt dużo.");
                    }
                    if (command.third.isAnnotationPresent(MinParameters.class) && commandParams.size() < command.third.getAnnotation(MinParameters.class).count()) {
                        throw new CommandExecutionException(command.third.getAnnotation(MinParameters.class).message().replace("%minParametersCount", String.valueOf(command.third.getAnnotation(MinParameters.class).count())).replace("%methodName", command.third.getName()));
                    }
                    if (command.third.isAnnotationPresent(MaxParameters.class) && commandParams.size() > command.third.getAnnotation(MaxParameters.class).count()) {
                        throw new CommandExecutionException(command.third.getAnnotation(MaxParameters.class).message().replace("%maxParametersCount", String.valueOf(command.third.getAnnotation(MaxParameters.class).count())).replace("%methodName", command.third.getName()));
                    }
                    if (command.third.isAnnotationPresent(ParametersCount.class) && commandParams.size() > command.third.getAnnotation(ParametersCount.class).count()) {
                        throw new CommandExecutionException(command.third.getAnnotation(ParametersCount.class).tooManyMessage().replace("%wantedParametersCount", String.valueOf(command.third.getAnnotation(ParametersCount.class).count())).replace("%methodName", command.third.getName()));
                    }
                    if (command.third.isAnnotationPresent(ParametersCount.class) && commandParams.size() < command.third.getAnnotation(ParametersCount.class).count()) {
                        throw new CommandExecutionException(command.third.getAnnotation(ParametersCount.class).notEnoughMessage().replace("%wantedParametersCount", String.valueOf(command.third.getAnnotation(ParametersCount.class).count())).replace("%methodName", command.third.getName()));
                    }
                    out += command.third.invoke(command.fourth, this);
                    commandFine = true;
                    break; //if no exception was thrown

                } else if (command.third.isAnnotationPresent(Aliased.class)) {
                    out += command.third.invoke(command.fourth, this);
                    commandFine = true;
                    break; //if no exception was thrown
                }
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                if (ex.getCause() instanceof CommandExecutionException) {
                    errorMessage = ex.getMessage();
                } else {
                    commandFine = true;
                    out += "Wystąpił błąd podczas wykonywania komendy. Przepraszamy.\n";
                    if (player.isAdmin()) {
                        out += ExceptionUtils.getLongDescription(ex.getCause()) + "\n";
                        ex.getCause().getStackTrace();
                    }
                    Logger.getLogger(CommandRouter.class.getName()).log(Level.SEVERE, null, ex.getCause());
                    break;
                }
            } catch (CommandExecutionException ex) {
                errorMessage = ex.getMessage();
            }
        }
        if (!commandFine) {
            if (!errorMessage.isEmpty()) {
                out += errorMessage.replace("%parametersCount", String.valueOf(commandParams.size())) + "\n";
            } else {
                out += "Masz jakiś problem!?\n";
            }
        }
        Logger.getLogger("Commands").log(Level.INFO, logEntry);
        return out;
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
}
