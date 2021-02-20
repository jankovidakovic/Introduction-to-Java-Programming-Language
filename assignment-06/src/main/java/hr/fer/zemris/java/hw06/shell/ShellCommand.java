package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Interface which defines a shell command, and provides the 
 * declaration of the methods that a shell command object is supposed
 * to implement.
 *
 * @author jankovidakovic
 *
 */
public interface ShellCommand {

	/**
	 * Executes the command in the given environment and with given arguments.
	 * Returns the shell status after the command execution.
	 *
	 * @param env Environment in which the command will be executed in.
	 * @param arguments Arguments of the command
	 * @return shell status after command execution. Returns CONTINUE if 
	 * command was executed successfully, and TERMINATE if there was an error
	 * in reading or writing.
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns the name of the command
	 *
	 * @return Command name
	 */
	String getCommandName();
	
	/**
	 * Returns the textual description of the command as a list of strings,
	 * each representing a single line.
	 *
	 * @return Textual description of the command
	 */
	List<String> getCommandDescription();
}
