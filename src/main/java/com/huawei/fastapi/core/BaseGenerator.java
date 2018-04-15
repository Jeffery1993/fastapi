package com.huawei.fastapi.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseGenerator extends AbstractGenerator {

	@Override
	public boolean createFiles(boolean override) throws IOException {
		boolean flag = super.createFiles(override);
		if (flag) {
			logger.info("*** Create basePackage successfully ***");
		}
		return flag;
	}

	@Override
	Map<String, String> getPlaceholders() {
		Map<String, String> placeholders = new HashMap<String, String>();
		placeholders.put("basePackageName", PathManager.BASE_PACKAGE_NAME);
		return placeholders;
	}

	@Override
	List<String> getTemplateList() throws IOException {
		return getTemplateList("template/common/base");
	}

	@Override
	String getTargetPath(String tmpName) {
		String fileName = getRenderedFileName(tmpName, getPlaceholders());
		return PathManager.MAIN_PATH + fileName;
	}

}
