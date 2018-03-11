package com.jeffery.fastapi.util;

public class StringUtils {

	public static final String EMPTY_STRING = "";

	public static boolean isEmpty(String str) {
		return str == null || EMPTY_STRING.equals(str.trim());
	}

}
