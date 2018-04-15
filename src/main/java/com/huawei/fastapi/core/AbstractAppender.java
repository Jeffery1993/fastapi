package com.huawei.fastapi.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.sql.SQLTable;

public abstract class AbstractAppender {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String TAB = "\t";
	public static final String ENTER = "\r\n";

	protected SQLTable table;

	/**
	 * Append fields to files.
	 * 
	 * @throws IOException
	 */
	public void appendFiles() throws IOException {
		List<File> fileList = getFileList();
		for (File file : fileList) {
			String input = FileUtils.readFileToString(file);
			String output = appendFields(input);
			FileUtils.writeStringToFile(file, output);
		}
	}

	/**
	 * Get list of files to be appended.
	 * 
	 * @return
	 */
	abstract List<File> getFileList();

	/**
	 * Process appending.
	 * 
	 * @param input
	 * @return
	 */
	abstract String appendFields(String input);

}
