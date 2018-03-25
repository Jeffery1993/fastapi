package com.jeffery.fastapi.base.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	public static String matchAndFindOnce(String str, String regex) {
		return matchAndFindOnce(str, regex, 0);
	}

	public static String matchAndFindOnce(String str, String regex, int group) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		if (m.find()) {
			return m.group(group);
		} else {
			return null;
		}
	}

	public static List<String> matchAndFindAll(String str, String regex) {
		return matchAndFindAll(str, regex, 0);
	}

	public static List<String> matchAndFindAll(String str, String regex, int group) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		List<String> list = new ArrayList<String>();
		while (m.find()) {
			list.add(m.group(group));
		}
		return list;
	}

}
