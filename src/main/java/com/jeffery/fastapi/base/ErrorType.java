package com.jeffery.fastapi.base;

public enum ErrorType {

	AUTHORITY_ERROR("Ȩ�޴���"), SQL_ERROR("���ݿ����"), UNKNOWN_ERROR("δ֪����");

	private String msg;

	private ErrorType(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

}
