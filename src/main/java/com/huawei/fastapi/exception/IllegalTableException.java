package com.huawei.fastapi.exception;

public class IllegalTableException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IllegalTableException() {
		super();
	}

	public IllegalTableException(String message) {
		super(message);
	}

	public IllegalTableException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalTableException(Throwable cause) {
		super(cause);
	}

}
