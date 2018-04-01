package com.jeffery.fastapi.core.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.base.utils.RegexUtils;

public class PathManager {

	private static final Logger logger = LoggerFactory.getLogger(PathManager.class);

	private static final String DAL_DIR = "common.dal";
	private static final String API_DIR = "api";

	private static final String GROUP_ID;
	private static final String ARTIFACT_ID;
	static {
		File pom = new File("pom.xml");
		if (!pom.exists()) {
			logger.error("File 'pom.xml' does not exist");
			System.exit(1);
		}
		String content = null;
		try {
			content = FileUtils.readFileToString(pom);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}

		String matchGroupId = "<groupId>(.*?)</groupId>";
		String matchArtifactId = "<artifactId>(.*?)</artifactId>";
		GROUP_ID = RegexUtils.matchAndFindOnce(content, matchGroupId, 1);
		ARTIFACT_ID = RegexUtils.matchAndFindOnce(content, matchArtifactId, 1);
	}

	private static final String GROUP_ARTIFACT_ID = GROUP_ID + "." + ARTIFACT_ID;
	private static final String DAL_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + DAL_DIR;
	private static final String API_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + API_DIR;

	private static final String ROOT_DIR = System.getProperty("user.dir");
	private static final String MAIN_DIR = "\\src\\main\\java";
	private static final String RES_DIR = "\\src\\main\\resources";

	private static final String MAIN_PATH = ROOT_DIR + MAIN_DIR + "\\" + GROUP_ARTIFACT_ID.replaceAll("\\.", "\\\\");
	private static final String RES_PATH = ROOT_DIR + RES_DIR;

	private static final String DAL_PATH = MAIN_PATH + "\\" + DAL_DIR.replaceAll("\\.", "\\\\");
	private static final String API_PATH = MAIN_PATH + "\\" + API_DIR;

	public static String getMainPath() {
		return MAIN_PATH;
	}

	public static String getResPath() {
		return RES_PATH;
	}

	public static String getDalPath() {
		return DAL_PATH;
	}

	public static String getApiPath() {
		return API_PATH;
	}

	public static String getDalPackageName() {
		return DAL_PACKAGE_NAME;
	}

	public static String getApiPackageName() {
		return API_PACKAGE_NAME;
	}

}
