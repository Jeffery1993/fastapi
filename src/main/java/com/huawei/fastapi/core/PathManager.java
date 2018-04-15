package com.huawei.fastapi.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.util.RegexUtils;

public class PathManager {

	private static final Logger logger = LoggerFactory.getLogger(PathManager.class);

	/*
	 * GroupId and artifactId.
	 */
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

	/*
	 * Get package name of target directory.
	 */
	private static final String BASE_DIR = "common.base";
	private static final String DAL_DIR = "common.dal";
	private static final String API_DIR = "api";
	private static final String XML_DIR = "mapper";

	/*
	 * Package name of target directory.
	 */
	public static final String BASE_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + BASE_DIR;
	public static final String DAL_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + DAL_DIR;
	public static final String API_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + API_DIR;

	/*
	 * Main path and resource path.
	 */
	private static final String ROOT_DIR = System.getProperty("user.dir");
	private static final String MAIN_DIR = "\\src\\main\\java";
	private static final String RES_DIR = "\\src\\main\\resources";
	private static final String GROUP_ARTIFACT_DIR = GROUP_ARTIFACT_ID.replaceAll("\\.", "\\\\");

	/*
	 * Absolute path of target directory.
	 */
	public static final String MAIN_PATH = ROOT_DIR + MAIN_DIR + "\\" + GROUP_ARTIFACT_DIR + "\\";
	public static final String BASE_PATH = MAIN_PATH + BASE_DIR.replaceAll("\\.", "\\\\") + "\\";
	public static final String DAL_PATH = MAIN_PATH + DAL_DIR.replaceAll("\\.", "\\\\") + "\\";
	public static final String API_PATH = MAIN_PATH + API_DIR + "\\";
	public static final String RES_PATH = ROOT_DIR + RES_DIR + "\\";
	public static final String XML_PATH = RES_PATH + XML_DIR + "\\";

}
