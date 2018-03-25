package com.jeffery.fastapi.core.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class SQLType {

	private static final String DEFAULT_TYPE = "String";
	private static final Map<String, List<String>> map;

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
		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			if (entry.getValue().contains(sqlType)) {
				return entry.getKey();
			}
		}
		return DEFAULT_TYPE;
	}

}
