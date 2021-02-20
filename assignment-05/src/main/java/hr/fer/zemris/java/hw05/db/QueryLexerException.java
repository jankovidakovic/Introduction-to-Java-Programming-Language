package hr.fer.zemris.java.hw05.db;

/**
 * Exception that is thrown by the smartScriptLexer whenever it encounters 
 * an unspecified situation.
 *
 * @author jankovidakovic
 *
 */
public class QueryLexerException extends RuntimeException {

	public QueryLexerException() {
	}

	public QueryLexerException(String message) {
		super(message);
	}

	public QueryLexerException(Throwable cause) {
		super(cause);
	}

	public QueryLexerException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryLexerException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
