package com.huawei.fastapi.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.fastapi.sql.SQLTable;
import com.huawei.fastapi.util.RenderUtils;

public class DaoMapperAppender extends AbstractAppender {

	public DaoMapperAppender(SQLTable table) {
		this.table = table;
	}

	@Override
	public void appendFiles() throws IOException {
		super.appendFiles();
		logger.info("*** Append fields to xml successfully ***");
	}

	@Override
	List<File> getFileList() {
		List<File> fileList = new ArrayList<File>();
		File file = new File(PathManager.XML_PATH + table.getTableClassName() + "DaoMapper.xml");
		fileList.add(file);
		return fileList;
	}

	@Override
	String appendFields(String input) {

		/*
		 * Append to columnsAndProperties.
		 */
		final String regex1 = "(<result column=.*? property=.*? />\\s*[\r\n])(\\s*[\r\n])*(\\s*</resultMap>\\s*[\r\n])";
		String columnsAndProperties = TAB + TAB + Placeholder.getColumnsAndProperties(table) + ENTER;
		String output = RenderUtils.render(regex1, input, columnsAndProperties);

		/*
		 * Append to select columns.
		 */
		final String regex2 = "(SELECT\\s*[\r\n]?\\s*[\\w, ]+)([\r\n])*(\\s*FROM.*?[\r\n])";
		String columns = ", " + Placeholder.getColumns(table);
		output = RenderUtils.render(regex2, output, columns + ENTER);

		/*
		 * Append to sqlWhereStatements.
		 */
		final String regex3 = "(<if test=\".*?\"> AND .*? </if>\\s*)([\r\n])*(\\s*</sql>\\s*[\r\n])";
		String sqlWhereStatements = TAB + Placeholder.getSqlWhereStatements(table) + ENTER + TAB;
		output = RenderUtils.render(regex3, output, sqlWhereStatements);

		/*
		 * Append to insert columns.
		 */
		final String regex4 = "(INSERT INTO \\w+\\([^\\)]+)(\\s)*(\\))";
		output = RenderUtils.render(regex4, output, columns);

		/*
		 * Append to createStatements.
		 */
		final String regex5 = "(VALUES\\(now\\(\\), now\\(\\), [^\\)]+)(\\s)*(\\))";
		String createStatements = ", " + Placeholder.getCreateStatements(table);
		output = RenderUtils.render(regex5, output, createStatements);

		/*
		 * Append to batchCreateStatements.
		 */
		final String regex6 = "(<foreach.*?>\\s*[\r\n]?\\s*\\(now\\(\\), now\\(\\), [^\\)]+)(\\s)*(\\))";
		String batchCreateStatements = ", " + Placeholder.getBatchCreateStatements(table);
		output = RenderUtils.render(regex6, output, batchCreateStatements);

		/*
		 * Append to updateStatements.
		 */
		final String regex7 = "(</if>\\s*[\r\n])(\\s)*(\\s*WHERE\\s+id = #\\{this.id\\})";
		String updateStatements = TAB + TAB + Placeholder.getUpdateStatements(table) + ENTER + TAB + TAB;
		output = RenderUtils.render(regex7, output, updateStatements);

		return output;
	}

}
