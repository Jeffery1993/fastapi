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

public class Generator {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	private String resource;
	private List<SQLTable> tables;

	private boolean generateApi;

	/**
	 * Get resource from input stream.
	 */
	public Generator() {
		this(System.in);
		getSQLTables();
	}

	/**
	 * Get resource from input stream.
	 * 
	 * @param is
	 */
	public Generator(InputStream is) {
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

	/**
	 * Get resource from string directly.
	 * 
	 * @param resource
	 */
	public Generator(String resource) {
		this.resource = resource;
		getSQLTables();
	}

	/**
	 * Get resource from SQL file
	 * 
	 * @param file
	 * @throws IOException
	 */
	public Generator(File file) throws IOException {
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

	/**
	 * Resolve resource to SQL tables.
	 */
	private void getSQLTables() {
		this.tables = SQLResolver.getSQLTables(resource);
		if (tables.isEmpty()) {
			throw new InvalidResourceException("Invalid resource: " + resource);
		}
	}

	/**
	 * Set it to true if API are needed.
	 * 
	 * @param generateApi
	 */
	public void setGenerateApi(boolean generateApi) {
		this.generateApi = generateApi;
	}

	/**
	 * Create files.
	 * 
	 * @param override
	 */
	public void createFiles(boolean override) {
		try {
			new BaseGenerator().createFiles(false);
			for (SQLTable table : tables) {
				DAOGenerator daoGenerator = new DAOGenerator(table);
				daoGenerator.createFiles(override);

				if (generateApi) {
					APIGenerator apiGenerator = new APIGenerator(table);
					apiGenerator.createFiles(override);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
	}

}
