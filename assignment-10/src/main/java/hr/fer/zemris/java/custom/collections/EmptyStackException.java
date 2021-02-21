package hr.fer.zemris.java.custom.collections;


/**
 * Exception intended to be used for unsupported operations performed on the
 * stack container. This exception is not a checked exception, so it does not
 * need to be declared with the <code>throws</code> keyword in the methods
 * that cause it.
 * @author jankovidakovic
 *
 */
public class EmptyStackException extends RuntimeException {


	/**
	 * auto generated serialVersionUID
	 */
	private static final long serialVersionUID = 6307697858263274779L;

	/**
	 * Default constructor with no parameters
	 */
	public EmptyStackException() {
	}

	/**
	 * Constructor that takes message as input
	 * @param message message of the exception
	 */
	public EmptyStackException(String message) {
		super(message);
	}

	//other constructors are not implemeneted because they are not yet needed

}
