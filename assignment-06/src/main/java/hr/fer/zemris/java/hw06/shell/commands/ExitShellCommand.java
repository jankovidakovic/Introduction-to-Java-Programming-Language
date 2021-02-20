/**
 * 
 */
package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command which exits the MyShell interface. Takes no arguments.
 *
 * @author jankovidakovic
 *
 */
public class ExitShellCommand implements ShellCommand, Recoverable {

	/**
	 * Executes the command by exiting the MyShell interface.
	 *
	 * @param env Environment of execution. Ignored in this command.
	 * @param arguments arguments of the command. Since this command
	 * takes no arguments, this field is ignored.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		if (parser.hasMoreArguments()) {
			return recoverFromError(env, "Too many arguments.");
		}
		return ShellStatus.TERMINATE;
	}

	@Override
	public String getCommandName() {
		return "exit";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		description.add("Exits the MyShell interface.");
		description.add("Takes no arguments.");
		return Collections.unmodifiableList(description);
	}

}
