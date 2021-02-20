package hr.fer.zemris.java.hw06.shell;

/**
 * Denotes the current status of MyShell.
 *
 * @author jankovidakovic
 *
 */
public enum ShellStatus {

	/**
	 * Shell should continue running and wait for the next command
	 */
	CONTINUE,
	
	/**
	 * Shell should be terminated
	 */
	TERMINATE,
}
