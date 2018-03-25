package com.jeffery.fastapi.core.common;

public class PathManager {

	private static final String DAL_DIR = "dal";
	private static final String API_DIR = "api";

	private static final String GROUP_ID;
	private static final String ARTIFACT_ID;
	static {
		GROUP_ID = "com.jeffery";
		ARTIFACT_ID = "template";
	}
	private static final String GROUP_ARTIFACT_ID = GROUP_ID + "." + ARTIFACT_ID;

	private static final String DAL_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + DAL_DIR;
	private static final String API_PACKAGE_NAME = GROUP_ARTIFACT_ID + "." + API_DIR;

	private static final String ROOT_DIR = System.getProperty("user.dir");
	private static final String MAIN_DIR = "\\src\\main\\java";
	private static final String RES_DIR = "\\src\\main\\resources";

	private static final String MAIN_PATH = ROOT_DIR + MAIN_DIR + "\\" + GROUP_ARTIFACT_ID.replaceAll("\\.", "\\\\");;
	private static final String RES_PATH = ROOT_DIR + RES_DIR;

	private static final String DAL_PATH = MAIN_PATH + "\\" + DAL_DIR;
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
