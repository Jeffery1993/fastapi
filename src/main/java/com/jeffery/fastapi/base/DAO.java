package com.jeffery.fastapi.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class DAO<P extends AbstractQueryParam, D extends AbstractModel> {

	public abstract Mapper<P, D> getMapper();

	public void create(D model) {
		getMapper().create(model);
	}

	public void create(List<D> modelList) {
		getMapper().batchCreate(modelList);
	}

	public Integer update(D model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("this", model);
		for (String field : model.changeSet()) {
			map.put(field, field);
		}
		return getMapper().update(map);
	}

	public Integer update(D model, Set<String> changeSet) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("this", model);
		for (String field : changeSet) {
			map.put(field, field);
		}
		return getMapper().update(map);
	}

	public PageList<D> query(P param) {
		PageList<D> pageList = new PageList<D>();
		pageList.setDataList(getMapper().query(param));
		pageList.setParam(param);
		if (param.getCountNeeded()) {
			pageList.setTotalCount(getMapper().count(param));
		}
		return pageList;
	}

	public Integer count(P param) {
		return getMapper().count(param);
	}

	public D find(Long id) {
		return getMapper().find(id);
	}

	public int delete(Long id) {
		return getMapper().delete(id);
	}

}
