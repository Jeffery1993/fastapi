package com.jeffery.fastapi.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.core.PlaceholderHandler.Word;
import com.jeffery.fastapi.core.SqlScriptResolver.Table;
import com.jeffery.fastapi.util.FileUtils;

public class SqlMapperGenerator {

	private static final Logger logger = LoggerFactory.getLogger(SqlMapperGenerator.class);

	private static final Map<String, String> TEMPLATE_NAME_MAP;
	static {
		TEMPLATE_NAME_MAP = new HashMap<String, String>();
		TEMPLATE_NAME_MAP.put("dao", "TableNameDao.java.tmp");
		TEMPLATE_NAME_MAP.put("mapper", "TableNameMapper.java.tmp");
		TEMPLATE_NAME_MAP.put("model", "TableNameModel.java.tmp");
		TEMPLATE_NAME_MAP.put("param", "TableNameParam.java.tmp");
		TEMPLATE_NAME_MAP.put("xml", "TableNameMapper.xml.tmp");
	}

	protected static boolean createFiles(String fileName) {
		List<Table> list = SqlScriptResolver.getResults(fileName);
		boolean flag = true;
		for (Table table : list) {
			if (!createFiles(table)) {
				flag = false;
			}
		}
		return flag;
	}

	protected static boolean createFiles(Table table) {
		if (!PlaceholderHandler.checkParam(table)) {
			logger.error("table is illegal");
			return false;
		}
		Map<String, String> placeholders = PlaceholderHandler.getPlaceholders(table);
		String tmpPath = System.getProperty("user.dir") + System.getProperty("tmps.dir");
		for (Map.Entry<String, String> entry : TEMPLATE_NAME_MAP.entrySet()) {
			String input = FileUtils.readFileAsString(tmpPath + entry.getValue());
			String output = PlaceholderHandler.rendering(input, placeholders);
			String targetPath = getTargetPath(entry.getKey(), Word.toUpperCamelCase(table.getTableName()));
			FileUtils.writeStringToFile(targetPath, output);
		}
		return true;
	}

	private static String getTargetPath(String key, String tableName) {
		String xmlPath = System.getProperty("user.dir") + System.getProperty("xmls.dir");
		String dalPath = System.getProperty("user.dir") + "\\src\\main\\java\\"
				+ System.getProperty("dalPackageName").replaceAll("\\.", "\\\\");
		if ("xml".equals(key)) {
			return xmlPath + tableName + "Mapper.xml";
		} else {
			return dalPath + "\\" + key + "\\" + tableName + Word.capitalize(key) + ".java";
		}
	}

	public static void main(String[] args) {
		System.setProperty("tmps.dir", "\\src\\test\\java\\tools\\templates\\");
		System.setProperty("xmls.dir", "\\src\\main\\resources\\mapper\\");
		System.setProperty("basePackageName", "com.jeffery.template.common.base");
		System.setProperty("dalPackageName", "com.jeffery.template.dal");
		createFiles(System.getProperty("user.dir") + "\\sql\\car.sql");
	}

}
