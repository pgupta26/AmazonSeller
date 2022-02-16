package com.qualitesoft.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Reporter;

public class Log {

	// Initialize Log4j logs
	private static Logger Log = LogManager.getLogger(Log.class.getName());
	
	public static void info(String message) {
		Reporter.log(message);
		Log.info(message);
	}

	public static void warn(String message) {
		Reporter.log(message);
		Log.warn(message);
	}

	public static void error(String message) {
		Reporter.log(message);
		Log.error(message);
	}

	public static void fatal(String message) {
		Reporter.log(message);
		Log.fatal(message);
	}

	public static void debug(String message) {
		Reporter.log(message);
		Log.debug(message);
	}
}
