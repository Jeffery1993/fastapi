package com.huawei.fastapi;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.core.DaoMapperAppender;
import com.huawei.fastapi.core.ModelParamAppender;
import com.huawei.fastapi.sql.SQLField;
import com.huawei.fastapi.sql.SQLTable;

public class Appender {

	private static final Logger logger = LoggerFactory.getLogger(Appender.class);

	private SQLTable table;

	/**
	 * Set the tableName to be added.
	 * 
	 * @param tableName
	 */
	public Appender(String tableName) {
		table = new SQLTable(tableName);
	}

	/**
	 * Add a field as type of SQLField.
	 * 
	 * @param field
	 */
	public void addField(SQLField field) {
		table.addField(field);
	}

	/**
	 * Add fields as type of SQLField.
	 * 
	 * @param fields
	 */
	public void addFields(List<SQLField> fields) {
		table.addFields(fields);
	}

	/**
	 * Add a field by field name and type.
	 * 
	 * @param fieldName
	 * @param fieldType
	 */
	public void addField(String fieldName, String fieldType) {
		addField(fieldName, fieldType, null);
	}

	/**
	 * Add a field by field name, type and comment.
	 * 
	 * @param fieldName
	 * @param fieldType
	 * @param fieldComment
	 */
	public void addField(String fieldName, String fieldType, String fieldComment) {
		SQLField field = new SQLField();
		field.setFieldName(fieldName);
		field.setFieldType(fieldType);
		field.setFieldComment(fieldComment);
		table.addField(field);
	}

	/**
	 * Add fields as type of Map.
	 * 
	 * @param fields
	 */
	public void addFields(Map<String, String> fields) {
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			SQLField field = new SQLField();
			field.setFieldName(entry.getKey());
			field.setFieldType(entry.getValue());
			table.addField(field);
		}
	}

	/**
	 * Append fields to files.
	 */
	public void appendFiles() {
		logger.info("Table to be added: " + table);
		try {
			ModelParamAppender modelParamAppender = new ModelParamAppender(table);
			modelParamAppender.appendFiles();
			DaoMapperAppender daoMapperAppender = new DaoMapperAppender(table);
			daoMapperAppender.appendFiles();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		logger.info("*** Append fields successfully ***");
	}

}
