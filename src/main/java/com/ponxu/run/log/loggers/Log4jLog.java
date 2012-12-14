package com.ponxu.run.log.loggers;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.ponxu.run.log.Log;

@SuppressWarnings("deprecation")
public class Log4jLog extends Log {
	private static final String FQCN = Log4jLog.class.getName();
	private static Priority traceLevel;
	private Logger logger;

	static {
		try {
			traceLevel = (Priority) Level.class.getDeclaredField("TRACE").get(
					null);
		} catch (Exception ex) {
			traceLevel = Priority.DEBUG;
		}
	}

	public Log4jLog(String name) {
		super(name);
		logger = Logger.getLogger(name);
	}

	private Priority getLevel(int level) {
		switch (level) {
		case TRACE:
			return traceLevel;
		case DEBUG:
			return Priority.DEBUG;
		case INFO:
			return Priority.INFO;
		case WARN:
			return Priority.WARN;
		case ERROR:
			return Priority.ERROR;
		case FATAL:
			return Priority.FATAL;
		}
		return Priority.DEBUG;
	}

	@Override
	public void log(int level, Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(level), message, null);
	}

	@Override
	public void log(int level, Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(level), message, t);
	}

	@Override
	public void debug(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void debug(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

	@Override
	public void trace(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void trace(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

	@Override
	public void info(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void info(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

	@Override
	public void warn(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void warn(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

	@Override
	public void error(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void error(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

	@Override
	public void fatal(Object message, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, null);
	}

	@Override
	public void fatal(Object message, Throwable t, Object... args) {
		message = format(message, args);
		logger.log(FQCN, getLevel(DEBUG), message, t);
	}

}
