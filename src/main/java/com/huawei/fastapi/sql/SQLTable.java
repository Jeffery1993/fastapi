package com.huawei.fastapi.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.huawei.fastapi.core.Placeholder;
import com.huawei.fastapi.exception.IllegalTableException;
import com.huawei.fastapi.util.StringUtils;

public class SQLTable {

	private String tableName;
	private List<SQLField> fields;
	private Map<String, String> placeholders;

	public SQLTable() {

	}

	public SQLTable(String tableName) {
		this.tableName = tableName;
	}

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
	 * Add field.
	 * 
	 * @param field
	 */
	public void addField(SQLField field) {
		if (this.fields == null) {
			this.fields = new ArrayList<SQLField>();
		}
		this.fields.add(field);
	}

	/**
	 * Add fields.
	 * 
	 * @param fields
	 */
	public void addFields(List<SQLField> fields) {
		if (this.fields == null) {
			this.fields = new ArrayList<SQLField>();
		}
		this.fields.addAll(fields);
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
	public String getTableClassName() {
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
	public String getTableObjectName() {
		return StringUtils.decapitalize(getTableClassName());
	}

	/**
	 * Get tableName all in lower case.
	 * 
	 * @return
	 */
	public String getTablePackageName() {
		return tableName.replaceAll("[^A-Za-z]", "");
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
	 * Check if contains id, gmt_create and gmt_modified.
	 */
	public void checkFields() throws IllegalTableException {
		if (getFieldByName("id") == null) {
			throw new IllegalTableException("Table must contain id");
		}
		if (getFieldByName("gmt_create") == null) {
			throw new IllegalTableException("Table must contain gmt_create");
		}
		if (getFieldByName("gmt_modified") == null) {
			throw new IllegalTableException("Table must contain gmt_modified");
		}
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

	/**
	 * Get placeholder map of the table.
	 * 
	 * @return
	 */
	public Map<String, String> getPlaceholders() {
		if (placeholders == null) {
			placeholders = Placeholder.getPlaceholders(this);
		}
		return placeholders;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("TableName [" + tableName + "]");
		sb.append(System.lineSeparator());
		for (int i = 0; i < fields.size(); i++) {
			sb.append("Field " + (i + 1) + ": " + fields.get(i).toString());
			if (i != fields.size() - 1) {
				sb.append(System.lineSeparator());
			}
		}
		return sb.toString();
	}

}
