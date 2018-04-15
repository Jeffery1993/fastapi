package com.huawei.fastapi.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.fastapi.sql.SQLTable;
import com.huawei.fastapi.util.RenderUtils;

public abstract class AbstractGenerator {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected SQLTable table;

	/**
	 * Create files.
	 * 
	 * @param override
	 * @return
	 * @throws IOException
	 */
	public boolean createFiles(boolean override) throws IOException {
		boolean flag = false;
		for (String tmpName : getTemplateList()) {
			String targetPath = getTargetPath(tmpName);
			String content = getRenderedContent(tmpName, getPlaceholders());

			File file = new File(targetPath);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
				logger.info("Create directory: " + parent.getAbsolutePath());
			}
			if (!file.exists()) {
				flag = true;
				FileUtils.writeStringToFile(file, content);
				logger.info("Create file: " + file.getAbsolutePath().replaceAll(".*\\\\", ""));
			} else {
				if (this.getClass().equals(BaseGenerator.class)) {
					continue;
				} else {
					String exist = FileUtils.readFileToString(file);
					if (override && !exist.equals(content)) {
						flag = true;
						FileUtils.writeStringToFile(file, content);
						logger.info("Override file: " + file.getAbsolutePath().replaceAll(".*\\\\", ""));
					}
				}
			}
		}
		return flag;
	}

	/**
	 * Get placeholder.
	 * 
	 * @return
	 */
	abstract Map<String, String> getPlaceholders();

	/**
	 * Get name list of templates.
	 * 
	 * @return
	 * @throws IOException
	 */
	abstract List<String> getTemplateList() throws IOException;

	/**
	 * Get name list of templates by prefix.
	 * 
	 * @param prefix
	 * @return
	 * @throws IOException
	 */
	List<String> getTemplateList(String prefix) throws IOException {
		URL fileURL = this.getClass().getResource("/template");
		String jarFileName = fileURL.getFile().replace("file:/", "").replace("!/template", "");
		JarFile jarFile = new JarFile(new File(jarFileName));
		Enumeration<JarEntry> entries = jarFile.entries();
		List<String> list = new ArrayList<String>();
		while (entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String fileName = jarEntry.getName();
			if (fileName.startsWith(prefix) && fileName.endsWith(".tmp")) {
				list.add("/" + fileName);
			}
		}
		jarFile.close();
		return list;
	}

	/**
	 * Get target path by tmpName.
	 * 
	 * @param tmpName
	 * @return
	 */
	abstract String getTargetPath(String tmpName);

	/**
	 * Get rendered content.
	 * 
	 * @param path
	 * @param placeholders
	 * @return
	 * @throws IOException
	 */
	protected String getRenderedContent(String path, Map<String, String> placeholders) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException("File not found: " + path);
		}
		String input = IOUtils.toString(is);
		String output = RenderUtils.render(input, placeholders);
		return output;
	}

	/**
	 * Get rendered fileName.
	 * 
	 * @param fileName
	 * @param placeholders
	 * @return
	 */
	protected String getRenderedFileName(String fileName, Map<String, String> placeholders) {
		return RenderUtils.render(fileName, placeholders).replace("/template/", "").replace(".tmp", "");
	}

}
