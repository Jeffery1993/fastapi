package com.jeffery.fastapi.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;

public abstract class AbstractQueryParam implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Integer page = 1;
	protected Integer pageSize = 10;
	protected Boolean countNeeded = false;
	protected Long id;
	protected List<Long> idList;
	protected Date gmtCreate;
	protected Date gmtModified;

	public Integer getStartNum() {
		if (page == null || page.intValue() < 1) {
			return Integer.valueOf(0);
		} else {
			return (page - 1) * pageSize;
		}
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getCountNeeded() {
		return countNeeded;
	}

	public void setCountNeeded(Boolean countNeeded) {
		this.countNeeded = countNeeded;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Long> getIdList() {
		return idList;
	}

	public void setIdList(List<Long> idList) {
		this.idList = idList;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

}
