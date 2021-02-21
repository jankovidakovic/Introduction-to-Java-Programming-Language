package hr.fer.zemris.java.blogapp.dao;

/**
 * Model of an exception which is caused when something goes wrong with the DAO.
 * 
 * @author jankovidakovic
 *
 */
public class DAOException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor which accepts the message to be displayed with the exception,
	 * and cause of the exception.
	 * 
	 * @param message message to be displayed with the exception
	 * @param cause   cause of the exception
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructor which accepts only the message to be displayed with the
	 * exception.
	 * 
	 * @param message message to be displayed with the exception.
	 */
	public DAOException(String message) {
		super(message);
	}
}
