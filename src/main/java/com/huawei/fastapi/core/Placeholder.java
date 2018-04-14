package com.huawei.fastapi.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.huawei.fastapi.sql.SQLField;
import com.huawei.fastapi.sql.SQLTable;
import com.huawei.fastapi.util.RenderUtils;
import com.huawei.fastapi.util.StringUtils;

public class Placeholder {

	public static final String TAB = "\t";
	public static final String ENTER = "\n";

	public static Map<String, String> getPlaceholders(SQLTable table) {
		Map<String, String> placeholders = new HashMap<String, String>();
		/*
		 * package name
		 */
		placeholders.put("basePackageName", PathManager.BASE_PACKAGE_NAME);
		placeholders.put("dalPackageName", PathManager.DAL_PACKAGE_NAME);
		placeholders.put("apiPackageName", PathManager.API_PACKAGE_NAME);

		/*
		 * table name
		 */
		placeholders.put("tableRealName", table.getTableName());
		placeholders.put("tableClassName", table.getTableClassName());
		placeholders.put("tableObjectName", table.getTableObjectName());
		placeholders.put("tablePackageName", table.getTablePackageName());

		/*
		 * fields and methods
		 */
		placeholders.put("privateFields", getPrivateFields(table));
		placeholders.put("publicMethods", getPublicMethods(table));
		placeholders.put("columnsAndProperties", getColumnsAndProperties(table));
		placeholders.put("columns", getColumns(table));

		/*
		 * SQL statements
		 */
		placeholders.put("sqlWhereStatements", getSqlWhereStatements(table));
		placeholders.put("createStatements", getCreateStatements(table));
		placeholders.put("batchCreateStatements", getBatchCreateStatements(table));
		placeholders.put("updateStatements", getUpdateStatements(table));
		return placeholders;
	}

	private static String getPrivateFields(SQLTable table) {
		final String template1 = "private ${fieldType} ${fieldObjectName};";
		final String template2 = "@ApiModelProperty(value = \"${fieldComment}\")" + ENTER + TAB + template1;
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldType", field.getFieldType());
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			if (StringUtils.isEmpty(field.getFieldComment())) {
				buffer.append(RenderUtils.render(template1, placesholders));
			} else {
				placesholders.put("fieldComment", field.getFieldComment());
				buffer.append(RenderUtils.render(template2, placesholders));
			}
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
			placesholders.put("fieldClassName", field.getFieldClassName());
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + ENTER + TAB);
			}
		}
		return buffer.toString();
	}

	private static String getColumnsAndProperties(SQLTable table) {
		final String template = "<result column=\"${fieldName}\" property=\"${fieldObjectName}\" />";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			placesholders.put("fieldName", field.getFieldName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + TAB + TAB);
			}
		}
		return buffer.toString();
	}

	private static String getColumns(SQLTable table) {
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			SQLField field = iterator.next();
			buffer.append(field.getFieldName());
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getSqlWhereStatements(SQLTable table) {
		final String template = "<if test=\"${fieldObjectName} != null\"> AND ${fieldName} = #{${fieldObjectName}} </if>";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			placesholders.put("fieldName", field.getFieldName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + TAB + TAB);
			}
		}
		return buffer.toString();
	}

	private static String getCreateStatements(SQLTable table) {
		final String template = "#{${fieldObjectName}}";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getBatchCreateStatements(SQLTable table) {
		final String template = "#{item.${fieldObjectName}}";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(", ");
			}
		}
		return buffer.toString();
	}

	private static String getUpdateStatements(SQLTable table) {
		final String template = "<if test=\"${fieldObjectName} != null\">" + ENTER + TAB + TAB + TAB
				+ ", ${fieldName} = #{this.${fieldObjectName}}" + ENTER + TAB + TAB + "</if>";
		StringBuffer buffer = new StringBuffer();
		for (Iterator<SQLField> iterator = table.getOtherFields().iterator(); iterator.hasNext();) {
			Map<String, String> placesholders = new HashMap<String, String>();
			SQLField field = iterator.next();
			placesholders.put("fieldObjectName", field.getFieldObjectName());
			placesholders.put("fieldName", field.getFieldName());
			buffer.append(RenderUtils.render(template, placesholders));
			if (iterator.hasNext()) {
				buffer.append(ENTER + TAB + TAB);
			}
		}
		return buffer.toString();
	}

}
