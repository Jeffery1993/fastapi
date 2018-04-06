package com.huawei.fastapi.exception;

public class InvalidResourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidResourceException() {
		super();
	}

	public InvalidResourceException(String message) {
		super(message);
	}

	public InvalidResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidResourceException(Throwable cause) {
		super(cause);
	}

}
