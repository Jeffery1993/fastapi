package com.jeffery.fastapi.exception;

public class AuthorityException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public AuthorityException() {
		super();
	}

	public AuthorityException(String message) {
		super(message);
	}

	public AuthorityException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorityException(Throwable cause) {
		super(cause);
	}

}
