package com.huawei.fastapi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.core.APIGenerator;
import com.huawei.fastapi.core.BaseGenerator;
import com.huawei.fastapi.core.DAOGenerator;
import com.huawei.fastapi.exception.InvalidResourceException;
import com.huawei.fastapi.sql.SQLResolver;
import com.huawei.fastapi.sql.SQLTable;
import com.huawei.fastapi.util.StringUtils;

public class FastAPI {

	private static final Logger logger = LoggerFactory.getLogger(FastAPI.class);

	private String resource;
	private List<SQLTable> tables;

	public FastAPI() {
		this(System.in);
		getSQLTables();
	}

	public FastAPI(InputStream is) {
		Scanner scanner = new Scanner(is);
		StringBuffer sb = new StringBuffer();
		String line = null;
		while (scanner.hasNextLine() && !StringUtils.isEmpty(line = scanner.nextLine())) {
			sb.append(line);
		}
		resource = sb.toString();
		scanner.close();
		getSQLTables();
	}

	public FastAPI(String resource) {
		this.resource = resource;
		getSQLTables();
	}

	public FastAPI(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file + "' exists but is a directory");
			}
			if (!file.canRead()) {
				throw new IOException("File '" + file + "' cannot be read");
			} else {
				resource = FileUtils.readFileToString(file);
			}
		} else {
			throw new FileNotFoundException("File '" + file + "' does not exist");
		}
		getSQLTables();
	}

	private void getSQLTables() {
		this.tables = SQLResolver.getSQLTables(resource);
		if (tables.isEmpty()) {
			throw new InvalidResourceException("Invalid resource: " + resource);
		}
	}

	public void start(boolean override) {
		try {
			new BaseGenerator().createFiles(false);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		for (SQLTable table : tables) {
			DAOGenerator daoGenerator = new DAOGenerator(table);
			try {
				daoGenerator.createFiles(override);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				continue;
			}

			APIGenerator apiGenerator = new APIGenerator(table);
			try {
				apiGenerator.createFiles(override);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
	}

}
