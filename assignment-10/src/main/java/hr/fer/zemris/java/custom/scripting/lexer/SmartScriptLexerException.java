package hr.fer.zemris.java.custom.scripting.lexer;

/**
 * Default exception of the SmartScriptLexer. It will be thrown whenever
 * the lexer ancounters an unspecified situation.
 * @author jankovidakovic
 *
 */
public class SmartScriptLexerException extends RuntimeException {

	/**
	 * auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -1346307518035207142L;

	public SmartScriptLexerException() {
	}

	public SmartScriptLexerException(String message) {
		super(message);
	}

	public SmartScriptLexerException(Throwable cause) {
		super(cause);
	}

	public SmartScriptLexerException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmartScriptLexerException(String message, Throwable cause, 
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
