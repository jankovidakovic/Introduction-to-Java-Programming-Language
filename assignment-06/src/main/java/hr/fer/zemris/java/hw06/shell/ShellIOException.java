package hr.fer.zemris.java.hw06.shell;

/**
 * Exception which is thrown by MyShell if reading or writing fails.
 *
 * @author jankovidakovic
 *
 */
public class ShellIOException extends RuntimeException {

	public ShellIOException() {
	}

	public ShellIOException(String message) {
		super(message);
	}

	public ShellIOException(Throwable cause) {
		super(cause);
	}

	public ShellIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShellIOException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
