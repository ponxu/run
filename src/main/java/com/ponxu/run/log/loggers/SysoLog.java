package com.ponxu.run.log.loggers;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ponxu.run.log.Log;

public class SysoLog extends Log {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS");

	private static String getLevel(int level) {
		switch (level) {
		case TRACE:
			return "[TRACE]";
		case DEBUG:
			return "[DEBUG]";
		case INFO:
			return "[INFO-]";
		case WARN:
			return "[WARN-]";
		case ERROR:
			return "[ERROR]";
		case FATAL:
			return "[FATAL]";
		}
		return "[INFO-]";
	}

	private static String now() {
		return sdf.format(new Date(System.currentTimeMillis()));
	}

	public SysoLog(String name) {
		super(name);
	}

	private void syso(int level, Object message, Throwable t, Object... args) {
		StringBuilder sb = new StringBuilder(now());
		sb.append(" ").append(getLevel(level)).append(" ");
		sb.append(format(message, args));
		System.out.println(sb.toString());
		if (t != null) {
			t.printStackTrace();
		}
	}

	@Override
	public void log(int level, Object message, Object... args) {
		syso(level, message, null, args);
	}

	@Override
	public void log(int level, Object message, Throwable t, Object... args) {
		syso(level, message, t, args);
	}
}
