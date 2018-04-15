package com.huawei.fastapi.sql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.util.RegexUtils;

public class SQLResolver {

	private static final Logger logger = LoggerFactory.getLogger(SQLResolver.class);

	private static final String MATCH_CREATE_TABLE_SCRIPT = "CREATE TABLE.*?`(.*?)` \\((.*?)PRIMARY KEY \\(`id`\\).*?\\) ENGINE=.*?DEFAULT CHARSET=.*?;";
	private static final String MATCH_FIELD_AND_TYPE = "`(\\w+)`\\W(\\w+).*?(COMMENT '(.*?)')?,";

	/**
	 * Get SQL tables by resolving scripts.
	 * 
	 * @param sqlScript
	 * @return
	 */
	public static List<SQLTable> getSQLTables(String sqlScript) {
		logger.info("*** Start to resolve SQL script ***");
		List<String> createTableScripts = getCreateTableScripts(sqlScript);
		List<SQLTable> list = new ArrayList<SQLTable>();
		for (String createTableScript : createTableScripts) {
			logger.info("CreateTableScript: " + createTableScript);
			String tableName = getTableName(createTableScript);
			List<SQLField> fields = getFields(createTableScript);
			SQLTable table = new SQLTable(tableName, fields);
			logger.info("Resolved table: " + table);
			table.checkFields();
			list.add(table);
		}
		logger.info("*** SQL script resolving completed ***");
		return list;
	}

	/**
	 * Get list of CREATE TABLE scripts.
	 * 
	 * @param sqlScript
	 * @return
	 */
	protected static List<String> getCreateTableScripts(String sqlScript) {
		return RegexUtils.matchAndFindAll(sqlScript.replaceAll("[\\r\\n]", ""), MATCH_CREATE_TABLE_SCRIPT);
	}

	/**
	 * Get table name of a single script.
	 * 
	 * @param createTableScript
	 * @return
	 */
	protected static String getTableName(String createTableScript) {
		return RegexUtils.matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 1);
	}

	/**
	 * Get fields of a single script.
	 * 
	 * @param createTableScript
	 * @return
	 */
	protected static List<SQLField> getFields(String createTableScript) {
		String content = RegexUtils.matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 2);
		List<String> fields = RegexUtils.matchAndFindAll(content, MATCH_FIELD_AND_TYPE, 0);
		List<SQLField> sqlFields = new ArrayList<SQLField>();
		for (String field : fields) {
			String fieldName = RegexUtils.matchAndFindOnce(field, MATCH_FIELD_AND_TYPE, 1);
			String fieldType = RegexUtils.matchAndFindOnce(field, MATCH_FIELD_AND_TYPE, 2);
			String fieldComment = RegexUtils.matchAndFindOnce(field, MATCH_FIELD_AND_TYPE, 4);
			SQLField sqlField = new SQLField();
			sqlField.setFieldName(fieldName);
			sqlField.setFieldType(SQLType.toJavaType(fieldType));
			sqlField.setFieldComment(fieldComment);
			sqlFields.add(sqlField);
		}
		return sqlFields;
	}

}
