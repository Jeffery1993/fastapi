package com.jeffery.fastapi.base.exception;

import com.jeffery.fastapi.base.ErrorType;

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

	@Override
	public ErrorType getErrorType() {
		return ErrorType.PARAM_ERROR;
	}

}
