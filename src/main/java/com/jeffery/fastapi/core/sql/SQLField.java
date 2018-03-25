package com.jeffery.fastapi.core.sql;

import com.jeffery.fastapi.base.utils.StringUtils;

public class SQLField {

	private String fieldName;
	private String fieldType;
	private String fieldComment;

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
	public String getUpperCamelCaseFieldName() {
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
	public String getLowerCamelCaseFieldName() {
		return StringUtils.decapitalize(getUpperCamelCaseFieldName());
	}

	@Override
	public String toString() {
		return new StringBuffer().append(fieldName).append("\t").append("[").append(fieldType).append("]").toString();
	}

}
