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

	public static void help() {
		String DEMO = "package tools;\n\n" + "import java.io.File;\n" + "import java.io.IOException;\n\n"
				+ "import com.jeffery.fastapi.core.Generator;\n"
				+ "import com.jeffery.fastapi.core.sql.SQLResource;\n\n" + "public class Test {\n\n"
				+ "\tpublic static void main(String[] args) throws IOException {\n"
				+ "\t\tString path = System.getProperty(\"user.dir\") + \"/sql/car.sql\";\n"
				+ "\t\tGenerator generator = new Generator(new SQLResource(new File(path)));\n"
				+ "\t\tgenerator.start();\n" + "\t}\n\n" + "}\n";
		System.out.println(DEMO);
	}

}
