package hr.fer.zemris.java.hw06.shell.commands;

import hr.fer.zemris.java.hw06.shell.Environment;
import hr.fer.zemris.java.hw06.shell.ShellStatus;

/**
 * Commands which implement this interface have the ability to recover from
 * errors. In such case, an appropriate error message is written to the
 * output of the shell ,and the shell continues running instead of terminating.
 *
 * @author jankovidakovic
 *
 */
public interface Recoverable {

	/**
	 * Recovers from error by printing the error message and continuing
	 * with shell running.
	 *
	 * @param env Environment in which the message will be printed
	 * @param message Message to be printed
	 * @return ShellStatus.CONTINUE
	 */
	default ShellStatus recoverFromError(
			Environment env, String message) {
		env.writeln(message);
		return ShellStatus.CONTINUE;
	}
}
