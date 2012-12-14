package com.ponxu.run.log.loggers;

import com.ponxu.run.log.Log;

public class NoOpLog extends Log {

	public NoOpLog(String name) {
		super(name);
	}

	@Override
	public void log(int level, Object message, Object... args) {
	}

	@Override
	public void log(int level, Object message, Throwable t, Object... args) {
	}
}
