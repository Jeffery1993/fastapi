package com.jeffery.fastapi.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

	public static String readFileAsString(String fileName) {
		return readFileAsString(fileName, true);
	}

	public static String readFileAsString(String fileName, boolean lineSeparated) {
		if (StringUtils.isEmpty(fileName)) {
			logger.error("fileName is empty");
			return StringUtils.EMPTY_STRING;
		}
		StringBuffer content = new StringBuffer();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line);
				if (lineSeparated) {
					content.append(System.lineSeparator());
				}
			}
			bufferedReader.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return StringUtils.EMPTY_STRING;
		}
		return content.toString();
	}

	public static boolean writeStringToFile(String fileName, String content) {
		if (StringUtils.isEmpty(fileName)) {
			logger.error("fileName is empty");
			return false;
		}
		try {
			FileWriter fileWriter = new FileWriter(fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(content);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

}
