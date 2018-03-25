package com.jeffery.fastapi.base.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jeffery.fastapi.core.exception.NoSuchPlaceholderException;

public class RenderUtils {

	private static final String MATCH_PLACEHOLDER = "\\$\\{(.*?)\\}";

	public static String render(String str, Map<String, String> placeholders) {
		Pattern p = Pattern.compile(MATCH_PLACEHOLDER);
		Matcher m = p.matcher(str);
		StringBuffer buffer = new StringBuffer();
		while (m.find()) {
			String placeholder = placeholders.get(m.group(1));
			if (StringUtils.isEmpty(placeholder)) {
				throw new NoSuchPlaceholderException("No such placeholder " + m.group(1));
			}
			m.appendReplacement(buffer, placeholder);
		}
		m.appendTail(buffer);
		return buffer.toString();
	}

}
