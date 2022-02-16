package com.qualitesoft.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import io.github.bonigarcia.wdm.WebDriverManager;


public class InitializeTest {

	protected static WebDriver driver = null;
	public static String browser = null;
	public static String env = null;
	public static String retryCount = null;
	public static String project = null;
	public static String existingbrand = null;
	public static String templateused = null;
	public static String testname = null;
	public static String URL = null;
	public static String manageproductfile = null;
	public static String managefullorderfile = null;
	public static String managequickorderfile = null;
	public static String fcusername = null;
	public static String fcpassword = null;
	public static String plaquery = null;
	public static String crusername = null;
	public static String crpassword = null;
	public static String crorderId = null;
	public static String keyword = null;
	public static String statkeyword = null;
	public static String brandname=null;
	public static String brandid=null;
	public static String GuestEmailid=null;
	public static String loginuser=null;
	public static String Row=null;
	public static String emailcreated=null;
	public static String suiteName=null;
	public static String carrierName=null;
	public static double productPrice ;
	public static String shipping;
	public static String amazonTax=null;
	public static String xpathRow=null;
	public static String fastShipping = null;
	public static String baseRateRow=null;
	public static String tax;
	public static String tax1;
	public static String emailAddress = null;
	public static String password = null;
	public static String menuSubMenu = null;
	public static String searchUser = null;
	public static String enableOption = null;
	public static String weight = null;
	public static String couponValue=null;
	public static String taxRate;
	public static String userType;
	public static String testData;
	public static String carrierDetailsOptions=null;
	public static String Productname;
	public static String userPermissions = null;
	public static boolean isRecording = false;
	
	@BeforeTest
	public void initialize(ITestContext context) throws Exception {
		browser = context.getCurrentXmlTest().getParameter("browser");
		env = context.getCurrentXmlTest().getParameter("env");
		URL = context.getCurrentXmlTest().getParameter("URL");
		retryCount = context.getCurrentXmlTest().getParameter("retryCount");
		project = context.getCurrentXmlTest().getParameter("project");
		existingbrand = context.getCurrentXmlTest().getParameter("existingbrand");
		templateused = context.getCurrentXmlTest().getParameter("templateused");
		manageproductfile = context.getCurrentXmlTest().getParameter("manageproductfile");
		managefullorderfile = context.getCurrentXmlTest().getParameter("managefullorderfile");
		managequickorderfile = context.getCurrentXmlTest().getParameter("managequickorderfile");
		fcusername = context.getCurrentXmlTest().getParameter("fcusername");
		fcpassword = context.getCurrentXmlTest().getParameter("fcpassword");
		plaquery= context.getCurrentXmlTest().getParameter("plaquery");
		crusername= context.getCurrentXmlTest().getParameter("crusername");
		crpassword= context.getCurrentXmlTest().getParameter("crpassword");
		keyword= context.getCurrentXmlTest().getParameter("keyword");
		brandname=context.getCurrentXmlTest().getParameter("brandname");
		loginuser = context.getCurrentXmlTest().getParameter("loginuser");
		testname = context.getCurrentXmlTest().getName();
		Row = context.getCurrentXmlTest().getParameter("Row");
		carrierName=context.getCurrentXmlTest().getParameter("CarrierName");
		suiteName = context.getCurrentXmlTest().getSuite().getName();
		menuSubMenu = context.getCurrentXmlTest().getParameter("menu_Submenu");
		tax = context.getCurrentXmlTest().getParameter("tax");
		tax1 = context.getCurrentXmlTest().getParameter("tax1");
		ScreenShot.createScreenshotFolder(testname);
		xpathRow=context.getCurrentXmlTest().getParameter("xpathRow");
		amazonTax=context.getCurrentXmlTest().getParameter("amazonTax");
		fastShipping=context.getCurrentXmlTest().getParameter("fastShipping");
		baseRateRow=context.getCurrentXmlTest().getParameter("baseRateRow");
		shipping=context.getCurrentXmlTest().getParameter("shipping");
		searchUser = context.getCurrentXmlTest().getParameter("searchUser");
		enableOption = context.getCurrentXmlTest().getParameter("enableOption");
		weight = context.getCurrentXmlTest().getParameter("weight");
		couponValue = context.getCurrentXmlTest().getParameter("couponValue");
		taxRate = context.getCurrentXmlTest().getParameter("taxRate");
		userType = context.getCurrentXmlTest().getParameter("userType");
		testData = context.getCurrentXmlTest().getParameter("testData");
		carrierDetailsOptions=context.getCurrentXmlTest().getParameter("CarrierDetailsOptions");
		emailAddress = context.getCurrentXmlTest().getParameter("emailAddress");
		password = context.getCurrentXmlTest().getParameter("password");
		userPermissions = context.getCurrentXmlTest().getParameter("userPermissions");
		isRecording = Boolean.parseBoolean(System.getProperty("isRecording"));
		Log.info("*****************************"+ testname + " started.");
	}
	
	@Parameters({ "browser", "URL" })
	@BeforeSuite
	public WebDriver setUp(String browser, String URL) throws IOException {
		driver = launchBrowser(browser);
		launchURL(URL);
		return driver;
	}

	@AfterSuite
	public void tearDown() {
		Log.info("******************Test Ended************************");
		driver.quit();
	}

	public WebDriver launchBrowser(String browser) {
		try {
			if(browser.equalsIgnoreCase("chrome")) {
				//set chrome options
				ChromeOptions options = new ChromeOptions();
				options.setAcceptInsecureCerts(true);
				options.addArguments("--start-maximized");
				
				//Launch browser
				WebDriverManager.chromedriver().clearResolutionCache().setup();
				driver = new ChromeDriver(options);
			} else if(browser.equalsIgnoreCase("firefox")) {
				//Launch browser
				WebDriverManager.firefoxdriver().clearResolutionCache().setup();
				driver = new FirefoxDriver();
			} else if(browser.equalsIgnoreCase("iexplorer")) {
				//Launch browser
				WebDriverManager.iedriver().clearResolutionCache().setup();
				driver = new InternetExplorerDriver();
			} else {
				throw new InvalidArgumentException("Invalid browser name");
			}
			Log.info(browser + " browser launched successfully.");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("return document.readyState").equals("complete");
		} catch (Exception e) {
			Log.error("Not able to launch browser: " + e.getMessage());
			throw e;
		}
		return driver;
	}

	public void launchURL(String URL) {
		try {
			driver.get(URL);
			Log.info(URL + " URL launched successfully.");
		} catch (Exception e) {
			Log.error("Not able to launch URL: " + e.getMessage());
			throw e;
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}
}
