package com.jeffery.fastapi.core.sql;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.base.utils.RegexUtils;

public class SQLResolver {

	private static final Logger logger = LoggerFactory.getLogger(SQLResolver.class);

	private static final String MATCH_CREATE_TABLE_SCRIPT = "CREATE TABLE.*?`(.*?)`(.*)ENGINE=.*?AUTO_INCREMENT=.*?DEFAULT CHARSET=.*?;";
	private static final String MATCH_FIELD_AND_TYPE = "`(\\w+)`\\W(\\w+)\\W";

	public static List<SQLTable> getSQLTables(String sqlScript) {
		logger.info("Start to resolve SQL script");
		List<String> createTableScripts = getCreateTableScripts(sqlScript);
		List<SQLTable> list = new ArrayList<SQLTable>();
		for (String createTableScript : createTableScripts) {
			logger.info("CreateTableScript: " + createTableScript);
			String tableName = getTableName(createTableScript);
			logger.info("TableName: " + tableName);
			List<SQLField> fields = getFields(createTableScript);
			for (SQLField field : fields) {
				logger.info(field.getFieldName() + " [ " + field.getFieldType() + " ] ");
			}
			list.add(new SQLTable(tableName, fields));
			logger.info(fields.size() + " columns in total");
		}
		logger.info("SQL script resolving completed");
		return list;
	}

	protected static List<String> getCreateTableScripts(String sqlScript) {
		return RegexUtils.matchAndFindAll(sqlScript.replaceAll("[\\r\\n]", ""), MATCH_CREATE_TABLE_SCRIPT);
	}

	protected static String getTableName(String createTableScript) {
		return RegexUtils.matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 1);
	}

	protected static List<SQLField> getFields(String createTableScript) {
		String content = RegexUtils.matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 2);
		List<String> fields = RegexUtils.matchAndFindAll(content, MATCH_FIELD_AND_TYPE, 0);
		List<SQLField> sqlFields = new ArrayList<SQLField>();
		for (String field : fields) {
			String fieldName = RegexUtils.matchAndFindOnce(field, MATCH_FIELD_AND_TYPE, 1);
			String fieldType = RegexUtils.matchAndFindOnce(field, MATCH_FIELD_AND_TYPE, 2);
			SQLField sqlField = new SQLField();
			sqlField.setFieldName(fieldName);
			sqlField.setFieldType(SQLType.toJavaType(fieldType));
			sqlFields.add(sqlField);
		}
		return sqlFields;
	}

}
