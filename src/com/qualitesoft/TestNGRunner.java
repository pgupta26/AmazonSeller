package com.qualitesoft;

import java.io.IOException;
import java.util.List;

import org.testng.TestNG;

import com.beust.jcommander.internal.Lists;

public class TestNGRunner {

	public static void main(String[] args) throws IOException {
		TestNG testng = new TestNG();
		List<String> suites = Lists.newArrayList();
		suites.add("./OrdersWithDefect.xml");
		testng.setTestSuites(suites);
		testng.run();
	}
}
