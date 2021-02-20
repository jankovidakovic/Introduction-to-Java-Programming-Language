package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command that displays helpful information about the shell.
 * If started with no arguments, it displays names of all supported commands.
 * If started with a single argument, it displays the name and the description
 * of selected command.
 *
 * @author jankovidakovic
 *
 */
public class HelpShellCommand implements Recoverable, ShellCommand {

	/**
	 * Executes the command in the given environment, using the provided
	 * arguments. Prints names of all available commands if no argument
	 * is provided, or name and description of a command that is provided
	 * as a single argument.
	 *
	 * @param env Environment of execution
	 * @param arguments arguments of command. If none are provided, it prints
	 * then names of all the available commands. If a single argument is provided,
	 * a name of the command, it prints the name and the description.
	 * @return ShellStatus.CONTINUE, allowing the shell to continue running.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		String commandName = null;
		if (parser.hasMoreArguments()) { //a single command name is expected
			try {
				commandName = parser.getNextString();
			} catch (ArgumentsParserException e) {
				return recoverFromError(env, e.getMessage());
			}
			if (parser.hasMoreArguments()) {
				return recoverFromError(env, "Too many arguments.");
			}
			if (env.commands().containsKey(commandName)) {
				ShellCommand command = env.commands().get(commandName);
				env.writeln("Name of the command: " + command.getCommandName());
				for (String descriptionLine : command.getCommandDescription()) {
					env.writeln(descriptionLine);
				}
				
			}
		} else { //prints names of all available commands
			env.writeln("Available commands: ");
			for (String command : env.commands().keySet()) {
				env.writeln("\t" + command);
			}
		}
		return null;
	}

	@Override
	public String getCommandName() {
		return "help";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		description.add("Displays helpful information about the MyShell.");
		description.add("If started with no arguments, displays the names of "
				+ "all available commands.");
		description.add("If started with a single argument, displays the "
				+ "name and description of given command.");
		return Collections.unmodifiableList(description);
	}

}
