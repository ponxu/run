package com.ponxu.run.log;

public abstract class Log {
	public static final int TRACE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int FATAL = 6;

	private String name;
	private int level = DEBUG;

	public Log(String name) {
		this.name = name;
	}

	public abstract void log(int level, Object message, Object... args);

	public abstract void log(int level, Object message, Throwable t,
			Object... args);

	public String getName() {
		return name;
	}

	public void setLevel(String levelName) {
		if ("TRACE".equalsIgnoreCase(levelName)) {
			this.level = TRACE;
		} else if ("DEBUG".equalsIgnoreCase(levelName)) {
			this.level = DEBUG;
		} else if ("INFO".equalsIgnoreCase(levelName)) {
			this.level = INFO;
		} else if ("WARN".equalsIgnoreCase(levelName)) {
			this.level = WARN;
		} else if ("ERROR".equalsIgnoreCase(levelName)) {
			this.level = ERROR;
		} else if ("FATAL".equalsIgnoreCase(levelName)) {
			this.level = FATAL;
		} else {
			this.level = DEBUG;
		}
	}

	public void trace(Object message, Object... args) {
		if (level <= TRACE)
			log(TRACE, message, args);
	}

	public void trace(Object message, Throwable t, Object... args) {
		if (level <= TRACE)
			log(TRACE, message, t, args);
	}

	public void debug(Object message, Object... args) {
		if (level <= DEBUG)
			log(DEBUG, message, args);
	}

	public void debug(Object message, Throwable t, Object... args) {
		if (level <= DEBUG)
			log(DEBUG, message, t, args);
	}

	public void info(Object message, Object... args) {
		if (level <= INFO)
			log(INFO, message, args);
	}

	public void info(Object message, Throwable t, Object... args) {
		if (level <= INFO)
			log(INFO, message, t, args);
	}

	public void warn(Object message, Object... args) {
		if (level <= WARN)
			log(WARN, message, args);
	}

	public void warn(Object message, Throwable t, Object... args) {
		if (level <= WARN)
			log(WARN, message, t, args);
	}

	public void error(Object message, Object... args) {
		if (level <= ERROR)
			log(ERROR, message, args);
	}

	public void error(Object message, Throwable t, Object... args) {
		if (level <= ERROR)
			log(ERROR, message, t, args);
	}

	public void fatal(Object message, Object... args) {
		if (level <= FATAL)
			log(FATAL, message, args);
	}

	public void fatal(Object message, Throwable t, Object... args) {
		if (level <= FATAL)
			log(FATAL, message, t, args);
	}

	// ===================================================
	protected static String format(Object message, Object... args) {
		if (message == null) {
			return null;
		}

		if (args == null || args.length == 0)
			return message.toString();
		else
			return String.format(message.toString(), args);
	}
}
