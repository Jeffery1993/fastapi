package com.jeffery.fastapi.core.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.jeffery.fastapi.base.utils.StringUtils;

public class SQLTable {

	private String tableName;
	private List<SQLField> fields;

	public SQLTable(String tableName, List<SQLField> fields) {
		this.tableName = tableName;
		this.fields = fields;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<SQLField> getFields() {
		return fields;
	}

	public void setFields(List<SQLField> fields) {
		this.fields = fields;
	}

	/**
	 * Sort fields by fieldName.
	 */
	public void sort() {
		Collections.sort(fields, new Comparator<SQLField>() {

			public int compare(SQLField o1, SQLField o2) {
				return o1.getFieldName().compareToIgnoreCase(o2.getFieldName());
			}

		});
	}

	/**
	 * Get tableName in UpperCamelCase.
	 * 
	 * @return
	 */
	public String getUpperCamelCaseTableName() {
		String[] slices = tableName.toLowerCase().split("[^A-Za-z]");
		StringBuffer sb = new StringBuffer();
		for (String slice : slices) {
			if (!"".equals(slice)) {
				sb.append(StringUtils.capitalize(slice));
			}
		}
		return sb.toString();
	}

	/**
	 * Get tableName in LowerCamelCase.
	 * 
	 * @return
	 */
	public String getLowerCamelCaseTableName() {
		return StringUtils.decapitalize(getUpperCamelCaseTableName());
	}

	/**
	 * Get field by fieldName.
	 * 
	 * @param fieldName
	 * @return
	 */
	public SQLField getFieldByName(String fieldName) {
		for (SQLField field : fields) {
			if (fieldName.equalsIgnoreCase(field.getFieldName())) {
				return field;
			}
		}
		return null;
	}

	/**
	 * Get other fields except id, gmt_create and gmt_modified.
	 * 
	 * @return
	 */
	public List<SQLField> getOtherFields() {
		List<SQLField> otherFields = new ArrayList<SQLField>();
		for (SQLField field : fields) {
			String fieldName = field.getFieldName();
			if (!"id".equalsIgnoreCase(fieldName) && !"gmt_create".equalsIgnoreCase(fieldName)
					&& !"gmt_modified".equalsIgnoreCase(fieldName)) {
				otherFields.add(field);
			}
		}
		return otherFields;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TableName: " + tableName);
		sb.append(System.lineSeparator());
		for (int i = 0; i < fields.size(); i++) {
			sb.append("field " + (i + 1) + ": " + fields.get(i).toString());
			if (i != fields.size() - 1) {
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

}
