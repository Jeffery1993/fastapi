package com.huawei.fastapi.util;

import java.io.File;

public class StringUtils {

	public static final String EMPTY_STRING = "";
	public static final String FILE_SEPARATOR = File.separator;
	public static final String LINE_SEPARATOR = System.lineSeparator();

	public static boolean isEmpty(String str) {
		return str == null || EMPTY_STRING.equals(str.trim());
	}

	public static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return s;
		} else {
			char ac[] = s.toCharArray();
			ac[0] = Character.toUpperCase(ac[0]);
			return new String(ac);
		}
	}

	public static String decapitalize(String s) {
		if (s == null || s.length() == 0) {
			return s;
		} else {
			char ac[] = s.toCharArray();
			ac[0] = Character.toLowerCase(ac[0]);
			return new String(ac);
		}
	}

}
