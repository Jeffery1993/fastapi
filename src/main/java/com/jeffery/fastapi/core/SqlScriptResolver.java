package com.jeffery.fastapi.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.jeffery.fastapi.util.FileUtils;
import com.jeffery.fastapi.util.StringUtils;

public class SqlScriptResolver {

	private static final Logger logger = LoggerFactory.getLogger(SqlScriptResolver.class);

	private static final String MATCH_CREATE_TABLE_SCRIPT = "CREATE TABLE.*?`(.*?)`(.*)ENGINE=.*?AUTO_INCREMENT=.*?DEFAULT CHARSET=.*?;";
	private static final String MATCH_FIELD_AND_TYPE = "`(\\w+)`\\W(\\w+)\\W";
	private static final String EMPTY_STRING = "";

	public static List<Table> getResults(String fileName) {
		logger.info("start to resolve file: " + fileName);
		String sqlScript = FileUtils.readFileAsString(fileName, false);
		List<String> createTableScripts = getCreateTableScripts(sqlScript);
		List<Table> list = new ArrayList<Table>();
		for (String createTableScript : createTableScripts) {
			logger.info("createTableScript: " + createTableScript);
			String tableName = getTableName(createTableScript);
			logger.info("tableName: " + tableName);
			Map<String, String> fieldsAndTypes = getFieldsAndTypes(createTableScript);
			logger.info(fieldsAndTypes.size() + " columns in total");
			for (Map.Entry<String, String> fieldAndType : fieldsAndTypes.entrySet()) {
				logger.info(fieldAndType.getKey() + " [ " + fieldAndType.getValue() + " ] ");
			}
			list.add(new Table(tableName, fieldsAndTypes));
		}
		logger.info("file resolving completed");
		return list;
	}

	protected static List<String> getCreateTableScripts(String sqlScript) {
		return matchAndFindAll(sqlScript, MATCH_CREATE_TABLE_SCRIPT);
	}

	protected static String getTableName(String createTableScript) {
		return matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 1);
	}

	protected static Map<String, String> getFieldsAndTypes(String createTableScript) {
		String content = matchAndFindOnce(createTableScript, MATCH_CREATE_TABLE_SCRIPT, 2);
		List<String> fieldsAndTypes = matchAndFindAll(content, MATCH_FIELD_AND_TYPE, 0);
		Map<String, String> map = new HashMap<String, String>();
		for (String fieldAndType : fieldsAndTypes) {
			String field = matchAndFindOnce(fieldAndType, MATCH_FIELD_AND_TYPE, 1);
			String type = matchAndFindOnce(fieldAndType, MATCH_FIELD_AND_TYPE, 2);
			if (StringUtils.isEmpty(field) || StringUtils.isEmpty(type)) {
				logger.error("either field or type is empty");
				continue;
			}
			map.put(field, DataType.toJavaType(type));
		}
		return map;
	}

	protected static String matchAndFindOnce(String str, String regex) {
		return matchAndFindOnce(str, regex, 0);
	}

	protected static String matchAndFindOnce(String str, String regex, int group) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return m.group(group);
		} else {
			logger.error("could not find " + regex + " in " + str);
			return EMPTY_STRING;
		}
	}

	protected static List<String> matchAndFindAll(String str, String regex) {
		return matchAndFindAll(str, regex, 0);
	}

	protected static List<String> matchAndFindAll(String str, String regex, int group) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			list.add(m.group(group));
		}
		return list;
	}

	public static class Table {

		private String tableName;
		private Map<String, String> fieldsAndTypes;

		public Table() {
		}

		public Table(String tableName, Map<String, String> fieldsAndTypes) {
			this.tableName = tableName;
			this.fieldsAndTypes = fieldsAndTypes;
		}

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public Map<String, String> getFieldsAndTypes() {
			return fieldsAndTypes;
		}

		public void setFieldsAndTypes(Map<String, String> fieldsAndTypes) {
			this.fieldsAndTypes = fieldsAndTypes;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}

	}

	@SuppressWarnings("serial")
	public static class DataType {

		private static final Map<String, List<String>> map;
		private static final String DEFAULT_TYPE = "String";

		static {
			map = new HashMap<String, List<String>>();
			map.put("Short", new ArrayList<String>() {
				{
					add("tinyint");
					add("smallint");
				}
			});
			map.put("Integer", new ArrayList<String>() {
				{
					add("mediumint");
					add("int");
				}
			});
			map.put("Long", new ArrayList<String>() {
				{
					add("bigint");
				}
			});
			map.put("Float", new ArrayList<String>() {
				{
					add("float");
				}
			});
			map.put("Double", new ArrayList<String>() {
				{
					add("double");
				}
			});
			map.put("String", new ArrayList<String>() {
				{
					add("char");
					add("varchar");
					add("tinytext");
					add("text");
					add("mediumtext");
					add("longtext");
					add("json");
				}
			});
			map.put("java.util.Date", new ArrayList<String>() {
				{
					add("date");
					add("time");
					add("year");
					add("datetime");
					add("timestamp");
				}
			});
		}

		public static String toJavaType(String sqlType) {
			if (StringUtils.isEmpty(sqlType)) {
				logger.error("sqlType is null or empty");
				return DEFAULT_TYPE;
			}
			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				if (entry.getValue().contains(sqlType)) {
					return entry.getKey();
				}
			}
			logger.error("unrecognized sqlType: " + sqlType);
			return DEFAULT_TYPE;
		}

	}

}
