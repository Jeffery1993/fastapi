package com.jeffery.fastapi.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageList<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer totalCount = 0;
	private AbstractQueryParam param;
	private List<T> dataList = new ArrayList<T>();

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public AbstractQueryParam getParam() {
		return param;
	}

	public void setParam(AbstractQueryParam param) {
		this.param = param;
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

}
