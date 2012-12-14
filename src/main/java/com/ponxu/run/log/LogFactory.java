package com.ponxu.run.log;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import com.ponxu.run.lang.FileUtils;
import com.ponxu.run.log.loggers.Log4jLog;
import com.ponxu.run.log.loggers.SysoLog;

/**
 * 日志
 * 
 * @author xwz
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LogFactory {
	private static Class<?> logClass = SysoLog.class;
	private static Constructor<?> logConstructor;
	private static String levelName = "debug";

	static {
		// 初始化日志组件
		// 1 检查是否有Log4j
		try {
			Class.forName("org.apache.log4j.Logger");
			logClass = Log4jLog.class;
		} catch (/* ClassNotFound */Exception e) {
			// 2 默认
			try {
				InputStream in = FileUtils.fromClassPath("run.properties");
				Properties prop = new Properties();
				prop.load(in);

				String clsName = prop.getProperty("log");
				levelName = prop.getProperty("log_level");

				Class cls = Class.forName(clsName);

				if (cls.isAssignableFrom(Log.class)) {
					logClass = cls;
				} else {
					logClass = SysoLog.class;
				}

				in.close();
			} catch (Exception e1) {
				logClass = SysoLog.class;
			}
		}

		System.out.println("Logger: " + logClass);
	}

	public static Log getLog(String name) {
		try {

			if (logConstructor == null) {
				synchronized (LogFactory.class) {
					if (logConstructor == null)
						logConstructor = logClass.getConstructor(String.class);
				}
			}

			Log log = (Log) logConstructor.newInstance(name);
			log.setLevel(levelName);
			return log;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Log getLog(Class<?> clazz) {
		return getLog(clazz.getName());
	}

	public static Log getLog() {
		String currentClassName = Thread.currentThread().getStackTrace()[2]
				.getClassName();
		return getLog(currentClassName);
	}
}
