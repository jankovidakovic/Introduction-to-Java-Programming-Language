package hr.fer.zemris.java.hw06.shell;

import java.util.SortedMap;

/**
 * Environment for command execution. Each command communicates with user
 * only through this interface (writes lines, reads lines).
 *
 * @author jankovidakovic
 *
 */
public interface Environment {

	
	/**
	 * Reads a single line of input and returns it as a string.
	 *
	 * @return Single line of input
	 * @throws ShellIOException if reading fails
	 */
	String readLine() throws ShellIOException;
	
	
	/**
	 * Writes a given string to the output of the shell.
	 *
	 * @param text String to be passed to the output
	 * @throws ShellIOException if writing fails
	 */
	void write(String text) throws ShellIOException;
	
	
	/**
	 * Writes a given string as a single line(string is followed by a newline)
	 *
	 * @param text String to be written
	 * @throws ShellIOException if writing fails
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returns the available commands in a map, intended to be retrieved
	 * by the name of the command
	 *
	 * @return Map of available commands
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns the symbol which the shell should output for  multiline commands
	 *
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets the multiline symbol to the given character
	 *
	 * @param symbol new multiline symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns the symbol which the shell should output when it expects
	 *
	 * the new command (prompt).
	 * @return prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets the prompt symbol to the given character
	 *
	 * @param symbol new prompt symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns the symbol which the user should write when wanting to
	 * split the command into multiple lines
	 *
	 * @return morelines symbol
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets the morelines symbol to the given character
	 *
	 * @param symbol new morelines symbol
	 */
	void setMorelinesSymbol(Character symbol);
}
