package com.jeffery.fastapi.core.exception;

public class NoSuchPlaceholderException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoSuchPlaceholderException() {
		super();
	}

	public NoSuchPlaceholderException(String message) {
		super(message);
	}

	public NoSuchPlaceholderException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoSuchPlaceholderException(Throwable cause) {
		super(cause);
	}

}
