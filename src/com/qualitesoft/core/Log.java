package com.qualitesoft.core;

import org.apache.log4j.Logger;
import org.testng.Reporter;

public class Log {

	// Initialize Log4j logs
	private static Logger Log = Logger.getLogger(Log.class.getName()); //

	public static void info(String message) {
		Reporter.log(message);
		Log.info(message);
		System.out.println(message);
	}

	public static void warn(String message) {
		Reporter.log(message);
		Log.warn(message);
		System.out.println(message);
	}

	public static void error(String message) {
		Reporter.log(message);
		Log.error(message);
		System.out.println(message);
	}

	public static void fatal(String message) {
		Reporter.log(message);
		System.out.println(message);
		Log.fatal(message);
	}

	public static void debug(String message) {
		System.out.println(message);
		Reporter.log(message);
		Log.debug(message);
	}
}
