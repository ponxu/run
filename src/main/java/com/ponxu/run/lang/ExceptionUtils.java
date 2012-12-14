package com.ponxu.run.lang;

import java.util.Arrays;

public class ExceptionUtils {
	public static void makeRunTimeWhen(boolean flag, String message,
			Object... args) {
		if (flag) {
			message = String.format(message, args);
			RuntimeException e = new RuntimeException(message);
			throw correctStackTrace(e);
		}
	}

	public static void makeRunTime() {
		RuntimeException e = new RuntimeException();
		throw correctStackTrace(e);
	}

	public static void makeRunTime(String message, Object... args) {
		message = String.format(message, args);
		RuntimeException e = new RuntimeException(message);
		throw correctStackTrace(e);
	}

	public static void makeRuntime(Throwable cause) {
		RuntimeException e = new RuntimeException(cause);
		throw correctStackTrace(e);
	}

	public static void makeRuntime(String message, Throwable cause,
			Object... args) {
		message = String.format(message, args);
		RuntimeException e = new RuntimeException(message, cause);
		throw correctStackTrace(e);
	}

	/** 移除 Lang层堆栈信息 */
	private static RuntimeException correctStackTrace(RuntimeException e) {
		StackTraceElement[] s = e.getStackTrace();
		e.setStackTrace(Arrays.copyOfRange(s, 1, s.length));
		return e;
	}
}
