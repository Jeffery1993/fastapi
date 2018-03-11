package com.jeffery.fastapi.exception;

public class ParamException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ParamException() {
		super();
	}

	public ParamException(String message) {
		super(message);
	}

	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamException(Throwable cause) {
		super(cause);
	}

}
