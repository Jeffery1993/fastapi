package com.jeffery.fastapi.core.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.jeffery.fastapi.base.utils.RenderUtils;
import com.jeffery.fastapi.core.common.PathManager;
import com.jeffery.fastapi.core.sql.SQLField;
import com.jeffery.fastapi.core.sql.SQLTable;

public class APIPlaceholder {

	public static final String TAB = "\t";
	public static final String ENTER = "\n";

	public static Map<String, String> getPlaceholders(SQLTable table) {
		Map<String, String> placeholders = new HashMap<String, String>();
		placeholders.put("apiPackageName", PathManager.getApiPackageName());
		placeholders.put("dalPackageName", PathManager.getDalPackageName());
		placeholders.put("tableClassName", table.getUpperCamelCaseTableName());
		placeholders.put("tableObjectName", table.getLowerCamelCaseTableName());
		placeholders.put("tablePackageName", table.getTableName().replaceAll("[^A-Za-z]", ""));
		placeholders.put("privateFields", getPrivateFields(table));
		placeholders.put("publicMethods", getPublicMethods(table));
		return placeholders;
	}

	private static String getPrivateFields(SQLTable table) {
		final String template = "private ${fieldType} ${fieldObjectName};";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldType", field.getFieldType());
			placesholders.put("fieldObjectName", field.getLowerCamelCaseFieldName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + TAB);
			}
		}
		return buffer.toString();

	}

	private static String getPublicMethods(SQLTable table) {
		final String template = "public ${fieldType} get${fieldClassName}() {" + ENTER + TAB + TAB
				+ "return ${fieldObjectName};" + ENTER + TAB + "}" + ENTER + ENTER + TAB
				+ "public void set${fieldClassName}(${fieldType} ${fieldObjectName}) {" + ENTER + TAB + TAB
				+ "this.${fieldObjectName} = ${fieldObjectName};" + ENTER + TAB + "}";

		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldType", field.getFieldType());
			placesholders.put("fieldClassName", field.getUpperCamelCaseFieldName());
			placesholders.put("fieldObjectName", field.getLowerCamelCaseFieldName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + ENTER + TAB);
			}
		}
		return buffer.toString();
	}

}
