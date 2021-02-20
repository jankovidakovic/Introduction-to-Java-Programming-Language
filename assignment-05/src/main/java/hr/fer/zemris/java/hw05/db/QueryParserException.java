package hr.fer.zemris.java.hw05.db;

/**
 * Exception that is thrown by the QueryParser whenever it cannot 
 * successfully parse a query, because the query given was invalid
 * according to the database's specification.
 *
 * @author jankovidakovic
 *
 */
public class QueryParserException extends RuntimeException {

	public QueryParserException() {
		super();
	}

	public QueryParserException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryParserException(String message) {
		super(message);
	}

	public QueryParserException(Throwable cause) {
		super(cause);
	}

	

}
