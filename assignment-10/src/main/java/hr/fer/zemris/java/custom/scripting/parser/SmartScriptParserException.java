package hr.fer.zemris.java.custom.scripting.parser;

/**
 * Default exception of the SmartScriptParser. Parser will throw this
 * exception whenever it encounters an error which is caused by 
 * the input document's invalid structure. Parser should throw no 
 * exceptions other than this one.
 * @author jankovidakovic
 *
 */
public class SmartScriptParserException extends RuntimeException {

	/**
	 * auto generated serialVersionUID
	 */
	private static final long serialVersionUID = -7873703860495022568L;

	public SmartScriptParserException() {
	}

	public SmartScriptParserException(String message) {
		super(message);
	}

	public SmartScriptParserException(Throwable cause) {
		super(cause);
	}

	public SmartScriptParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public SmartScriptParserException(String message, Throwable cause, 
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
