package hr.fer.zemris.java.gui.layouts;

/**
 * Exception which is thrown when an unspecified situation occurs in the
 * CalcLayout manager, such as adding a component into invalid position.
 * 
 * @author jankovidakovic
 *
 */
public class CalcLayoutException extends RuntimeException {

	/**
	 * Default constructor. Takes no arguments.
	 */
	public CalcLayoutException() {
	}

	/**
	 * Constructor which takes the informational message about the cause of the
	 * exception.
	 * 
	 * @param message
	 */
	public CalcLayoutException(String message) {
		super(message);
	}

	/**
	 * Constructor which takes the cause.
	 * 
	 * @param cause
	 */
	public CalcLayoutException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor which takes the message and the cause.
	 * 
	 * @param message
	 * @param cause
	 */
	public CalcLayoutException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor which takes all arguments.
	 * 
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CalcLayoutException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
