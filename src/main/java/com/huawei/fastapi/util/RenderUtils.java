package com.huawei.fastapi.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.huawei.fastapi.exception.NoSuchPlaceholderException;

public class RenderUtils {

	private static final String MATCH_PLACEHOLDER = "\\$\\{(.*?)\\}";

	public static String render(String str, Map<String, String> placeholders) {
		Pattern p = Pattern.compile(MATCH_PLACEHOLDER);
		Matcher m = p.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while (m.find()) {
			String placeholder = placeholders.get(m.group(1));
			if (StringUtils.isEmpty(placeholder)) {
				throw new NoSuchPlaceholderException("No such placeholder: " + m.group(1));
			}
			m.appendReplacement(buffer, placeholder);
		}
		m.appendTail(buffer);
		return buffer.toString();
	}

	public static String render(String regex, String str, String placeholder) throws NoSuchPlaceholderException {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		StringBuffer buffer = new StringBuffer();
		if (m.find()) {
			String appended = m.group(1) + placeholder + m.group(3);
			m.appendReplacement(buffer, appended);
		} else {
			throw new NoSuchPlaceholderException("Placeholder not found for: " + regex);
		}
		while (m.find()) {
			String appended = m.group(1) + placeholder + m.group(3);
			m.appendReplacement(buffer, appended);
		}
		m.appendTail(buffer);
		return buffer.toString();
	}

}
