package com.jeffery.fastapi.base;

import java.io.Serializable;

public abstract class AbstractVO implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long id;
	protected String gmtCreate;
	protected String gmtModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(String gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public String getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(String gmtModified) {
		this.gmtModified = gmtModified;
	}

}
