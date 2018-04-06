package com.huawei.fastapi.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseGenerator extends AbstractGenerator {

	@Override
	public void createFiles(boolean override) throws IOException {
		super.createFiles(override);
		logger.info("Create basePackage successfully");
	}

	@Override
	Map<String, String> getPlaceholders() {
		Map<String, String> placeholders = new HashMap<String, String>();
		placeholders.put("basePackageName", PathManager.BASE_PACKAGE_NAME);
		placeholders.put("expPackageName", PathManager.EXP_PACKAGE_NAME);
		placeholders.put("utilPackageName", PathManager.UTIL_PACKAGE_NAME);
		return placeholders;
	}

	@Override
	List<String> getTemplateList() throws IOException {
		return getTemplateList("template/base");
	}

	@Override
	String getTargetPath(String tmpName) {
		String fileName = getRenderedFileName(tmpName, getPlaceholders());
		return PathManager.COMMON_PATH + fileName;
	}

}
