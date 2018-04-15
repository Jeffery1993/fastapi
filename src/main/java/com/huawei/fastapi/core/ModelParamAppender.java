package com.huawei.fastapi.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.huawei.fastapi.sql.SQLTable;
import com.huawei.fastapi.util.RenderUtils;

public class ModelParamAppender extends AbstractAppender {

	public ModelParamAppender(SQLTable table) {
		this.table = table;
	}

	@Override
	public void appendFiles() throws IOException {
		super.appendFiles();
		logger.info("*** Append fields to model and param successfully ***");
	}

	@Override
	List<File> getFileList() {
		List<File> fileList = new ArrayList<File>();
		fileList.add(new File(PathManager.DAL_PATH + "model\\" + table.getTableClassName() + "Model.java"));
		fileList.add(new File(PathManager.DAL_PATH + "param\\" + table.getTableClassName() + "Param.java"));
		File param = new File(PathManager.API_PATH + table.getTablePackageName() + "\\param");
		if (param.exists() && param.isDirectory()) {
			File[] subs = param.listFiles();
			for (File sub : subs) {
				fileList.add(sub);
			}
		}
		File model = new File(PathManager.API_PATH + table.getTablePackageName() + "\\model");
		if (model.exists() && model.isDirectory()) {
			File[] subs = model.listFiles();
			for (File sub : subs) {
				fileList.add(sub);
			}
		}
		return fileList;
	}

	@Override
	String appendFields(String input) {

		/*
		 * Append to privateFields.
		 */
		final String regex1 = "(private.*?[\r\n])(\\s*[\r\n])*(\\s*public.*?[\r\n])";
		String privateFields = TAB + Placeholder.getPrivateFields(table) + ENTER + TAB + ENTER;
		String output = RenderUtils.render(regex1, input, privateFields);

		/*
		 * Append to publicMethods.
		 */
		final String regex2 = "(\\s*}\\s*[\r\n])(\\s*[\r\n])*(\\s*\\}\\s*[\r\n])";
		String publicMethods = TAB + Placeholder.getPublicMethods(table) + ENTER + TAB + ENTER;
		output = RenderUtils.render(regex2, output, publicMethods);

		return output;
	}

}
