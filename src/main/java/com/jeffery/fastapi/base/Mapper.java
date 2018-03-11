package com.jeffery.fastapi.base;

import java.util.List;
import java.util.Map;

public interface Mapper<P extends AbstractQueryParam, D extends AbstractModel> {

	/**
	 * Create a record according to the given model.
	 * 
	 * @param model
	 */
	void create(D model);

	/**
	 * Create records according to the given modelList.
	 * 
	 * @param modelList
	 */
	void batchCreate(List<D> modelList);

	/**
	 * Update the record with the given model.
	 * 
	 * @param model
	 * @return
	 */
	Integer update(Map<String, Object> map);

	/**
	 * Query the records by the given @param.
	 * 
	 * @param param
	 * @return
	 */
	List<D> query(P param);

	/**
	 * Count the records by the given @param.
	 * 
	 * @param param
	 * @return
	 */
	Integer count(P param);

	/**
	 * Find the record by id.
	 * 
	 * @param id
	 * @return
	 */
	D find(Long id);

	/**
	 * Delete the record by id.
	 * 
	 * @param id
	 * @return
	 */
	int delete(Long id);

}