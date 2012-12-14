package com.ponxu.run.log.loggers;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ponxu.run.log.Log;

public class JDKLog extends Log {
	private Logger logger;

	public JDKLog(String name) {
		super(name);
		logger = Logger.getLogger(name);
	}

	private Level getLevel(int level) {
		switch (level) {
		case TRACE:
			return Level.FINEST;
		case DEBUG:
			return Level.FINE;
		case INFO:
			return Level.INFO;
		case WARN:
			return Level.WARNING;
		case ERROR:
		case FATAL:
			return Level.SEVERE;
		}

		return Level.INFO;
	}

	private void log(Level level, String msg, Throwable ex) {
		if (logger.isLoggable(level)) {
			// Hack (?) to get the stack trace.
			Throwable dummyException = new Throwable();
			StackTraceElement locations[] = dummyException.getStackTrace();
			// Caller will be the third element
			String cname = "unknown";
			String method = "unknown";
			if (locations != null && locations.length > 3) {
				StackTraceElement caller = locations[3];
				cname = caller.getClassName();
				method = caller.getMethodName();
			}

			if (ex == null) {
				logger.logp(level, cname, method, msg);
			} else {
				logger.logp(level, cname, method, msg, ex);
			}
		}

	}

	@Override
	public void log(int level, Object message, Object... args) {
		log(getLevel(level), format(message, args), null);
	}

	@Override
	public void log(int level, Object message, Throwable t, Object... args) {
		log(getLevel(level), format(message, args), t);
	}
}
