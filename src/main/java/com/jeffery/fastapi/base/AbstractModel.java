package com.jeffery.fastapi.base;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson.JSON;

public abstract class AbstractModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	private Set<String> changeSet;

	public AbstractModel() {
		changeSet = getAllFields();
	}

	private Set<String> getAllFields() {
		Set<String> changeSet = new HashSet<String>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (!"id".equals(fieldName) && !"changeSet".equals(fieldName) && !"serialVersionUID".equals(fieldName)) {
				changeSet.add(fieldName);
			}
		}
		return changeSet;
	}

	public Set<String> changeSet() {
		return changeSet;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
