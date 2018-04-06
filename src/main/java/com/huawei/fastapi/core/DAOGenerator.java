package com.huawei.fastapi.core;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.huawei.fastapi.sql.SQLTable;

public class DAOGenerator extends AbstractGenerator {

	public DAOGenerator(SQLTable table) {
		this.table = table;
	}

	@Override
	public void createFiles(boolean override) throws IOException {
		super.createFiles(override);
		logger.info("Create DAO for '" + table.getTableName() + "'successfully");
	}

	@Override
	Map<String, String> getPlaceholders() {
		return table.getPlaceholders();
	}

	@Override
	List<String> getTemplateList() throws IOException {
		return getTemplateList("template/dal");
	}

	@Override
	String getTargetPath(String tmpName) {
		String fileName = getRenderedFileName(tmpName, getPlaceholders());
		if (fileName.endsWith(".xml")) {
			return PathManager.XML_PATH + fileName.replaceAll(".*/", "");
		} else {
			return PathManager.COMMON_PATH + fileName;
		}
	}

}
