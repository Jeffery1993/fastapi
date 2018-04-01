package com.jeffery.fastapi.core.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.base.utils.RenderUtils;
import com.jeffery.fastapi.core.common.PathManager;
import com.jeffery.fastapi.core.sql.SQLTable;

public class DAOGenerator {

	private static final Logger logger = LoggerFactory.getLogger(DAOGenerator.class);

	private static final Map<String, String> TEMPLATE_NAME_MAP;
	static {
		TEMPLATE_NAME_MAP = new HashMap<String, String>();
		TEMPLATE_NAME_MAP.put("dao", "TableNameDao.java.tmp");
		TEMPLATE_NAME_MAP.put("mapper", "TableNameMapper.java.tmp");
		TEMPLATE_NAME_MAP.put("model", "TableNameModel.java.tmp");
		TEMPLATE_NAME_MAP.put("param", "TableNameParam.java.tmp");
		TEMPLATE_NAME_MAP.put("xml", "TableNameMapper.xml.tmp");
	}

	private SQLTable table;
	private Boolean overrideAll;

	public DAOGenerator(SQLTable table) {
		this.table = table;
	}

	public void createFiles() throws IOException {
		Map<String, String> placeholders = DAOPlaceholder.getPlaceholders(table);
		for (Map.Entry<String, String> entry : TEMPLATE_NAME_MAP.entrySet()) {
			String targetPath = getTargetPath(entry.getKey(), entry.getValue(), table.getUpperCamelCaseTableName());
			File file = new File(targetPath);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
				logger.info("Create directory: " + parent.getAbsolutePath());
			}
			if (file.exists()) {
				logger.info("File already exists: " + file.getName());
				if (overrideAll == null) {
					if (getUserOptions()) {
						String content = getRenderedContent(entry.getValue(), placeholders);
						FileUtils.writeStringToFile(file, content);
						logger.info("Override file: " + file.getAbsolutePath());
					}
				} else if (overrideAll) {
					String content = getRenderedContent(entry.getValue(), placeholders);
					FileUtils.writeStringToFile(file, content);
					logger.info("Override file: " + file.getAbsolutePath());
				} else {
					System.exit(0);
				}
			} else {
				String content = getRenderedContent(entry.getValue(), placeholders);
				FileUtils.writeStringToFile(file, content);
			}
		}
		logger.info("Create DAO for '" + table.getTableName() + "' successfully");
	}

	@SuppressWarnings("resource")
	private boolean getUserOptions() {
		boolean override = false;
		System.out.println("Do you want to override it? (Y/N)");
		Scanner scanner = new Scanner(System.in);
		if (scanner.hasNext()) {
			override = scanner.next().equalsIgnoreCase("Y");
		}
		System.out.println("Apply to all the files afterwards? (Y/N)");
		if (scanner.hasNext()) {
			if (scanner.next().equalsIgnoreCase("Y")) {
				overrideAll = override;
			}
		}
		return override;
	}

	private String getRenderedContent(String tmpName, Map<String, String> placeholders) throws IOException {
		InputStream is = this.getClass().getResourceAsStream("/template/dao/" + tmpName);
		String input = IOUtils.toString(is);
		String output = RenderUtils.render(input, placeholders);
		return output;
	}

	private String getTargetPath(String key, String value, String tableName) {
		if ("xml".equals(key)) {
			return PathManager.getResPath() + "\\mapper\\" + tableName + "Mapper.xml";
		} else {
			return PathManager.getDalPath() + "\\" + key + "\\"
					+ value.replace("TableName", tableName).replace(".tmp", "");
		}
	}

}
