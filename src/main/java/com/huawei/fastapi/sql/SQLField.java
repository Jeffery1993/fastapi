package com.huawei.fastapi.sql;

import com.huawei.fastapi.sql.SQLType.JavaType;
import com.huawei.fastapi.util.StringUtils;

public class SQLField {

	private String fieldName;
	private String fieldType;
	private String fieldComment;

	public SQLField() {

	}

	public SQLField(String fieldName) {
		this.fieldName = fieldName;
		this.fieldType = JavaType.STRING.getValue();
	}

	public SQLField(String fieldName, String fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
	}

	public SQLField(String fieldName, String fieldType, String fieldComment) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.fieldComment = fieldComment;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldComment() {
		return fieldComment;
	}

	public void setFieldComment(String fieldComment) {
		this.fieldComment = fieldComment;
	}

	/**
	 * Get fieldName in UpperCamelCase.
	 * 
	 * @return
	 */
	public String getFieldClassName() {
		String[] slices = fieldName.toLowerCase().split("[^A-Za-z]");
		StringBuffer sb = new StringBuffer();
		for (String slice : slices) {
			if (!"".equals(slice)) {
				sb.append(StringUtils.capitalize(slice));
			}
		}
		return sb.toString();
	}

	/**
	 * Get fieldName in LowerCamelCase.
	 * 
	 * @return
	 */
	public String getFieldObjectName() {
		return StringUtils.decapitalize(getFieldClassName());
	}

	@Override
	public String toString() {
		return new StringBuffer().append(fieldName).append("\t").append("[").append(fieldType).append("]").toString();
	}

}
