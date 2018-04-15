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
	public boolean createFiles(boolean override) throws IOException {
		boolean flag = super.createFiles(override);
		if (flag) {
			logger.info("*** Create DAO for '" + table.getTableName() + "'successfully ***");
		} else {
			logger.info("*** No DAO changes detected for '" + table.getTableName() + "' ***");
		}
		return flag;
	}

	@Override
	Map<String, String> getPlaceholders() {
		return table.getPlaceholders();
	}

	@Override
	List<String> getTemplateList() throws IOException {
		return getTemplateList("template/common/dal");
	}

	@Override
	String getTargetPath(String tmpName) {
		String fileName = getRenderedFileName(tmpName, getPlaceholders());
		if (fileName.endsWith(".xml")) {
			return PathManager.XML_PATH + fileName.replaceAll(".*/", "");
		} else {
			return PathManager.MAIN_PATH + fileName;
		}
	}

}
