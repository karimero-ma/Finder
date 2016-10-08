package jp.co.km.finder;

public class InvalidFileCommandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFileCommandException() {
	}

	public InvalidFileCommandException(String message) {
		super(message);
	}

	public InvalidFileCommandException(Throwable cause) {
		super(cause);
	}

	public InvalidFileCommandException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFileCommandException(String message, Throwable cause
			, boolean enableSuppression,boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
