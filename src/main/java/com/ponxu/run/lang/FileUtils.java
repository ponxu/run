package com.ponxu.run.lang;

import java.io.InputStream;

/**
 * 文件辅助
 * 
 * @author xwz
 */
public class FileUtils {
	/** 在类路径下打开文件 */
	public static InputStream fromClassPath(String fileName) {
		if (!fileName.startsWith("/"))
			fileName = "/" + fileName;
		return FileUtils.class.getResourceAsStream(fileName);
	}

	public String writeTo(InputStream in, String targetFileName) {
		return targetFileName;
	}

	public void loopLine(String fileName, ILineHandler handler) {
	}

	public static interface ILineHandler {
		public void handle(String line);
	}
}
