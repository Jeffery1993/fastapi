package com.jeffery.fastapi.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.base.exception.DAOException;

public abstract class DAO<P extends AbstractQueryParam, D extends AbstractModel> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public abstract Mapper<P, D> getMapper();

	public void create(D model) throws DAOException {
		try {
			getMapper().create(model);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error create model " + model, e);
		}
	}

	public void create(List<D> modelList) throws DAOException {
		try {
			getMapper().batchCreate(modelList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error create modelList " + modelList, e);
		}
	}

	public Integer update(D model) throws DAOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("this", model);
		for (String field : model.changeSet()) {
			map.put(field, field);
		}
		try {
			return getMapper().update(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error update model " + model, e);
		}
	}

	public Integer update(D model, Set<String> changeSet) throws DAOException {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("this", model);
		for (String field : changeSet) {
			map.put(field, field);
		}
		try {
			return getMapper().update(map);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error update model " + model, e);
		}
	}

	public PageList<D> query(P param) throws DAOException {
		PageList<D> pageList = new PageList<D>();
		try {
			pageList.setDataList(getMapper().query(param));
			pageList.setParam(param);
			if (param.getCountNeeded()) {
				pageList.setTotalCount(getMapper().count(param));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error query with param " + param, e);
		}
		return pageList;
	}

	public Integer count(P param) throws DAOException {
		try {
			return getMapper().count(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error count with param " + param, e);
		}
	}

	public D find(Long id) throws DAOException {
		try {
			return getMapper().find(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error find by id " + id, e);
		}
	}

	public int delete(Long id) throws DAOException {
		try {
			return getMapper().delete(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DAOException("Error delete by id " + id, e);
		}
	}

}
