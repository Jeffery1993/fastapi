package com.jeffery.fastapi.core.dao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

	public DAOGenerator(SQLTable table) {
		this.table = table;
	}

	public void createFiles() throws IOException {
		Map<String, String> placeholders = DAOPlaceholder.getPlaceholders(table);
		for (Map.Entry<String, String> entry : TEMPLATE_NAME_MAP.entrySet()) {
			InputStream is = this.getClass().getResourceAsStream("/template/dao/" + entry.getValue());
			String input = IOUtils.toString(is);
			String output = RenderUtils.render(input, placeholders);
			String targetPath = getTargetPath(entry.getKey(), entry.getValue(), table.getUpperCamelCaseTableName());
			FileUtils.writeStringToFile(new File(targetPath), output);
		}
		logger.info("Create DAO for '" + table.getTableName() + "' successfully");
	}

	private static String getTargetPath(String key, String value, String tableName) {
		if ("xml".equals(key)) {
			return PathManager.getResPath() + "\\mapper\\" + tableName + "Mapper.xml";
		} else {
			return PathManager.getDalPath() + "\\" + key + "\\"
					+ value.replace("TableName", tableName).replace(".tmp", "");
		}
	}

}
