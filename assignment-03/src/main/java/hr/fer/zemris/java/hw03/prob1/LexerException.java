package hr.fer.zemris.java.hw03.prob1;


/**
 * Represents the exception of the lexer. Lexer will throw this exception
 * whenever it encounters something unspecified in the documentation, for 
 * example if the input string is not of proper format.
 *
 * @author jankovidakovic
 *
 */
public class LexerException extends RuntimeException {

	public LexerException() {
		super();
	}

	public LexerException(String message) {
		super(message);
	}

	public LexerException(Throwable cause) {
		super(cause);
	}

	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}

	public LexerException(String message, Throwable cause, 
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
