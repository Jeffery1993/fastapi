package com.huawei.fastapi.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SQLType {

	public enum JavaType {

		SHORT("Short"), INTEGER("Integer"), LONG("Long"), FLOAT("Float"), DOUBLE("Double"), STRING("String"), DATE(
				"java.util.Date");

		private String value;

		private JavaType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

	}

	private static final Map<JavaType, List<String>> map;

	static {
		map = new HashMap<JavaType, List<String>>();
		map.put(JavaType.SHORT, new ArrayList<String>() {
			{
				add("tinyint");
				add("smallint");
			}
		});
		map.put(JavaType.INTEGER, new ArrayList<String>() {
			{
				add("mediumint");
				add("int");
			}
		});
		map.put(JavaType.LONG, new ArrayList<String>() {
			{
				add("bigint");
			}
		});
		map.put(JavaType.FLOAT, new ArrayList<String>() {
			{
				add("float");
			}
		});
		map.put(JavaType.DOUBLE, new ArrayList<String>() {
			{
				add("double");
			}
		});
		map.put(JavaType.STRING, new ArrayList<String>() {
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
		map.put(JavaType.DATE, new ArrayList<String>() {
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
		for (Map.Entry<JavaType, List<String>> entry : map.entrySet()) {
			if (entry.getValue().contains(sqlType)) {
				return entry.getKey().getValue();
			}
		}
		return JavaType.STRING.getValue();
	}

}
