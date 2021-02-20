package hr.fer.zemris.java.hw06.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellCommand;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Command which creates a directory structure. Takes a single argument, path
 * to the directory that is to be created. It creates the directory, along with
 * all the necessary parent directories that may not have existed.
 *
 * @author jankovidakovic
 *
 */
public class MkdirShellCommand implements Recoverable, ShellCommand {

	/**
	 * Executes a command in the given environment. Creates a directory structure
	 * that matches the given argument. Creates all the directories that do not
	 * exist but are in the path that is provided.
	 *
	 * @param env Environment of execution. Ignored in this command.
	 * @param arguments arguments of the command. A single argument is expected,
	 * path to the wanted directory.
	 * @return ShellStatus.CONTINUE, allowing shell to continue running.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		ArgumentsParser parser = new ArgumentsParser(arguments);
		Path newDir = null;
		if (parser.hasMoreArguments()) {
			try {
				newDir = parser.getNextPath(); 
			} catch (ArgumentsParserException e) {
				return recoverFromError(env, e.getMessage());
			}
		} else {
			return recoverFromError(env, "Not enough arguments.");
		}
		
		if (parser.hasMoreArguments()) {
			return recoverFromError(env, "Too many arguments.");
		}
		
		newDir.toFile().mkdirs(); //creates all the necessary directories
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return "mkdir";
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<String>();
		description.add("Creates a given directory path.");
		description.add("Creates all the necessary but nonexistent "
				+ "parent directories.");
		description.add("Takes one argument, path to new directory.");
		return Collections.unmodifiableList(description);
	}

}
