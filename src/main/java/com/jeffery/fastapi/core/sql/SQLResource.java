package com.jeffery.fastapi.core.sql;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.jeffery.fastapi.base.utils.StringUtils;
import com.jeffery.fastapi.core.exception.InvalidResourceException;

public class SQLResource {

	private String resource;

	public SQLResource() {
		this(System.in);
	}

	public SQLResource(InputStream is) {
		Scanner scanner = new Scanner(is);
		StringBuffer sb = new StringBuffer();
		String line = null;
		while (scanner.hasNextLine() && !StringUtils.isEmpty(line = scanner.nextLine())) {
			sb.append(line);
		}
		resource = sb.toString();
		scanner.close();
	}

	public SQLResource(String resource) {
		this.resource = resource;
	}

	public SQLResource(File file) throws IOException {
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
	}

	public List<SQLTable> getSQLTables() {
		List<SQLTable> tables = SQLResolver.getSQLTables(resource);
		if (tables.isEmpty()) {
			throw new InvalidResourceException("Invalid resource: " + resource);
		}
		return tables;
	}

}
