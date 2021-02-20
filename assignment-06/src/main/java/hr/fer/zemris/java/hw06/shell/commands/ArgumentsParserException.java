package hr.fer.zemris.java.hw06.shell.commands;

/**
 * Exception which is thrown by ArgumentsParser when unable to parse
 * required argument.
 *
 * @author jankovidakovic
 *
 */
public class ArgumentsParserException extends RuntimeException {

	/**
	 * Constructs a new exception with the given message.
	 * @param message Message to be passed with the exception
	 */
	public ArgumentsParserException(String message) {
		super(message);
	}

}
