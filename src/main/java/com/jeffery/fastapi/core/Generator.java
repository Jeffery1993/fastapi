package com.jeffery.fastapi.core;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffery.fastapi.core.api.APIGenerator;
import com.jeffery.fastapi.core.dao.DAOGenerator;
import com.jeffery.fastapi.core.sql.SQLResource;
import com.jeffery.fastapi.core.sql.SQLTable;

public class Generator {

	private static final Logger logger = LoggerFactory.getLogger(Generator.class);

	private List<SQLTable> tables;

	public Generator(SQLResource resource) {
		tables = resource.getSQLTables();
	}

	public void start() {
		for (SQLTable table : tables) {
			DAOGenerator daoGenerator = new DAOGenerator(table);
			try {
				daoGenerator.createFiles();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				continue;
			}

			APIGenerator apiGenerator = new APIGenerator(table);
			try {
				apiGenerator.createFiles();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
	}

}
