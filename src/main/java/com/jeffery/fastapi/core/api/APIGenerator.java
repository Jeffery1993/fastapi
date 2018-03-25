package com.jeffery.fastapi.core.api;

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

public class APIGenerator {

	private static final Logger logger = LoggerFactory.getLogger(APIGenerator.class);

	private static final Map<String, String> TEMPLATE_NAME_MAP;
	static {
		TEMPLATE_NAME_MAP = new HashMap<String, String>();
		TEMPLATE_NAME_MAP.put("controller", "TableNameController.java.tmp");
		TEMPLATE_NAME_MAP.put("service", "TableNameService.java.tmp");
		TEMPLATE_NAME_MAP.put("model", "TableNameVO.java.tmp");
	}

	private SQLTable table;

	public APIGenerator(SQLTable table) {
		this.table = table;
	}

	public void createFiles() throws IOException {
		Map<String, String> placeholders = APIPlaceholder.getPlaceholders(table);
		for (Map.Entry<String, String> entry : TEMPLATE_NAME_MAP.entrySet()) {
			InputStream is = this.getClass().getResourceAsStream("/template/api/" + entry.getValue());
			String input = IOUtils.toString(is);
			String output = RenderUtils.render(input, placeholders);
			String targetPath = getTargetPath(entry.getKey(), entry.getValue(), table.getUpperCamelCaseTableName());
			FileUtils.writeStringToFile(new File(targetPath), output);
		}
		logger.info("Create API for '" + table.getTableName() + "' successfully");
	}

	private static String getTargetPath(String key, String value, String tableName) {
		return PathManager.getApiPath() + "\\" + tableName.toLowerCase() + "\\" + key + "\\"
				+ value.replace("TableName", tableName).replace(".tmp", "");
	}

}
