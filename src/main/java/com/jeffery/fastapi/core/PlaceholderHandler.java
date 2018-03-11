package com.jeffery.fastapi.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.core.SqlScriptResolver.Table;
import com.jeffery.fastapi.util.StringUtils;

public class PlaceholderHandler {

	private static final Logger logger = LoggerFactory.getLogger(PlaceholderHandler.class);

	private static final String MATCH_PLACEHOLDER = "\\$\\{(.*?)\\}";

	private static final String ENTER_KEY = System.lineSeparator();
	private static final String TAB_KEY = "\t";

	public static String rendering(String str, Map<String, String> placeholders) {
		Pattern p = Pattern.compile(MATCH_PLACEHOLDER);
		Matcher m = p.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while (m.find()) {
			String placeholder = placeholders.get(m.group(1));
			if (StringUtils.isEmpty(placeholder)) {
				logger.error("placeholder is empty for " + m.group(1));
				continue;
			}
			m.appendReplacement(buffer, placeholder);
		}
		m.appendTail(buffer);
		return buffer.toString();
	}

	public static boolean checkParam(Table table) {
		Map<String, String> fieldsAndTypes = table.getFieldsAndTypes();
		if (!fieldsAndTypes.containsKey("id") || !fieldsAndTypes.containsKey("gmt_create")
				|| !fieldsAndTypes.containsKey("gmt_modified")) {
			logger.error("table must contain id, gmt_create and gmt_modified");
			return false;
		} else {
			Map<String, String> otherFieldsAndTypes = new HashMap<String, String>();
			for (Map.Entry<String, String> entry : fieldsAndTypes.entrySet()) {
				if ("id".equals(entry.getKey()) || "gmt_create".equals(entry.getKey())
						|| "gmt_modified".equals(entry.getKey())) {
					continue;
				} else {
					otherFieldsAndTypes.put(entry.getKey(), entry.getValue());
				}
			}
			table.setFieldsAndTypes(otherFieldsAndTypes);
			return true;
		}
	}

	public static Map<String, String> getPlaceholders(Table table) {
		Map<String, String> placeholders = new HashMap<String, String>();
		placeholders.put("basePackageName", System.getProperty("basePackageName"));
		placeholders.put("dalPackageName", System.getProperty("dalPackageName"));
		placeholders.put("tableNickName", getTableNickName(table.getTableName()));
		placeholders.put("privateFields", getPrivateFields(table.getFieldsAndTypes()));
		placeholders.put("publicMethods", getPublicMethods(table.getFieldsAndTypes()));
		placeholders.put("tableRealName", table.getTableName());
		placeholders.put("columnsAndProperties", getColumnsAndProperties(table.getFieldsAndTypes()));
		placeholders.put("columns", getColumns(table.getFieldsAndTypes()));
		placeholders.put("sqlWhereStatements", getSqlWhereStatements(table.getFieldsAndTypes()));
		placeholders.put("createStatements", getCreateStatements(table.getFieldsAndTypes()));
		placeholders.put("batchCreateStatements", getBatchCreateStatements(table.getFieldsAndTypes()));
		placeholders.put("updateStatements", getUpdateStatements(table.getFieldsAndTypes()));
		return placeholders;
	}

	public static class Word {

		public static String capitalize(String word) {
			char[] charArray = word.toCharArray();
			charArray[0] -= 32;
			return String.valueOf(charArray);
		}

		public static String deCapitalize(String word) {
			char[] charArray = word.toCharArray();
			charArray[0] += 32;
			return String.valueOf(charArray);
		}

		public static String joint(String word) {
			return joint(word, "_");
		}

		public static String joint(String word, String regex) {
			String[] slices = word.split(regex);
			StringBuffer buffer = new StringBuffer();
			for (String slice : slices) {
				buffer.append(capitalize(slice));
			}
			return buffer.toString();
		}

		public static String toLowerCamelCase(String word) {
			return deCapitalize(joint(word));
		}

		public static String toUpperCamelCase(String word) {
			return joint(word);
		}

	}

	private static String getTableNickName(String tableName) {
		return Word.toUpperCamelCase(tableName);
	}

	private static String getPrivateFields(Map<String, String> fieldsAndTypes) {
		final String template = "private ${type} ${field};";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<Map.Entry<String, String>> iterator = fieldsAndTypes.entrySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			Map.Entry<String, String> fieldAndType = iterator.next();
			placesholders.put("field", Word.toLowerCamelCase((fieldAndType.getKey())));
			placesholders.put("type", fieldAndType.getValue());
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER_KEY + TAB_KEY);
			}
		}
		return buffer.toString();

	}

	private static String getPublicMethods(Map<String, String> fieldsAndTypes) {
		final String template = "public ${type} get${FieldClass}() {" + ENTER_KEY + TAB_KEY + TAB_KEY
				+ "return ${Field};" + ENTER_KEY + TAB_KEY + "}" + ENTER_KEY + ENTER_KEY + TAB_KEY
				+ "public void set${FieldClass}(${type} ${Field}) {" + ENTER_KEY + TAB_KEY + TAB_KEY
				+ "this.${Field} = ${Field};" + ENTER_KEY + TAB_KEY + "}";

		StringBuffer buffer = new StringBuffer();
		for (Iterator<Map.Entry<String, String>> iterator = fieldsAndTypes.entrySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			Map.Entry<String, String> fieldAndType = iterator.next();
			placesholders.put("FieldClass", Word.toUpperCamelCase(fieldAndType.getKey()));
			placesholders.put("Field", Word.toLowerCamelCase(fieldAndType.getKey()));
			placesholders.put("type", fieldAndType.getValue());
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER_KEY + ENTER_KEY + TAB_KEY);
			}
		}
		return buffer.toString();
	}

	private static String getColumnsAndProperties(Map<String, String> fieldsAndTypes) {
		final String template = "<result column=\"${field}\" property=\"${Field}\" />";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			String field = iterator.next();
			placesholders.put("Field", Word.toLowerCamelCase(field));
			placesholders.put("field", field);
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER_KEY + TAB_KEY + TAB_KEY);
			}
		}
		return buffer.toString();
	}

	private static String getColumns(Map<String, String> fieldsAndTypes) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			buffer.append(iterator.next());
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getSqlWhereStatements(Map<String, String> fieldsAndTypes) {
		final String template = "<if test=\"${Field} != null\"> AND ${field} = #{${Field}} </if>";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			String field = iterator.next();
			placesholders.put("Field", Word.toLowerCamelCase(field));
			placesholders.put("field", field);
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER_KEY + TAB_KEY + TAB_KEY);
			}
		}
		return buffer.toString();
	}

	private static String getCreateStatements(Map<String, String> fieldsAndTypes) {
		final String template = "#{${Field}}";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			String field = iterator.next();
			placesholders.put("Field", Word.toLowerCamelCase(field));
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getBatchCreateStatements(Map<String, String> fieldsAndTypes) {
		final String template = "#{item.${Field}}";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			String field = iterator.next();
			placesholders.put("Field", Word.toLowerCamelCase(field));
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getUpdateStatements(Map<String, String> fieldsAndTypes) {
		final String template = "<if test=\"${Field} != null\">" + ENTER_KEY + TAB_KEY + TAB_KEY + TAB_KEY
				+ ", ${field} = #{this.${Field}}" + ENTER_KEY + TAB_KEY + TAB_KEY + "</if>";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<String> iterator = fieldsAndTypes.keySet().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			String field = iterator.next();
			placesholders.put("Field", Word.toLowerCamelCase(field));
			placesholders.put("field", field);
			buffer.append(rendering(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER_KEY + TAB_KEY + TAB_KEY);
			}
		}
		return buffer.toString();
	}

}
