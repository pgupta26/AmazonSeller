package com.qualitesoft.core;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class InitializeTest {

	protected static WebDriver driver = null;
	public static String browser = null;
	public static String retryCount = null;
	public static String project = null;
	public static String existingbrand = null;
	public static String templateused = null;
	public static String testname = null;
	//public static String testname = null;
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
	public static String shipping; //= "Free";
	public static String amazonTax=null;
	public static String xpathRow=null;
	public static String fastShipping = null;
	public static String carriername=null;
	public static String baseRateRow=null;
	public static String tax;
	public static String tax1;
	public static String emailAddress = null;
	public static String menuSubMenu = null;
	public static String searchUser = null;
	public static String enableOption = null;
	public static String weight = null;
	public static String couponValue=null;
	public static String taxRate;
	public static String userType;
	public static String testData;

	@BeforeTest
	public void initialize(ITestContext context) {
		browser = context.getCurrentXmlTest().getParameter("browser");
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
		//crorderId= context.getCurrentXmlTest().getParameter("crorderId");
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
		//Logger Log = Logger.getLogger(Log.class.getName());
		fastShipping=context.getCurrentXmlTest().getParameter("fastShipping");
		carriername=context.getCurrentXmlTest().getParameter("carriername");
		baseRateRow=context.getCurrentXmlTest().getParameter("baseRateRow");
		shipping=context.getCurrentXmlTest().getParameter("shipping");
		searchUser = context.getCurrentXmlTest().getParameter("searchUser");
		enableOption = context.getCurrentXmlTest().getParameter("enableOption");
		weight = context.getCurrentXmlTest().getParameter("weight");
		couponValue = context.getCurrentXmlTest().getParameter("couponValue");
		taxRate = context.getCurrentXmlTest().getParameter("taxRate");
		userType = context.getCurrentXmlTest().getParameter("userType");
		testData = context.getCurrentXmlTest().getParameter("testData");
		Log.info("*****************************"+ testname + " started.");
	}
	

	@Parameters({ "browser", "URL" })
	@BeforeSuite
	public WebDriver setUp(String browser, String URL) {
		DOMConfigurator.configure("log4j.xml");
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
			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver", "binaries/geckodriver.exe");
				driver = new FirefoxDriver();
			}

			if (browser.equalsIgnoreCase("chrome")) {
				String download =System.getProperty("user.dir")+File.separator+"download";
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				chromePrefs.put("download.default_directory", download);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.setBinary("C://Program Files//Google//Chrome//Application//chrome.exe");
				options.addArguments("--disable-notifications");
				DesiredCapabilities cap = DesiredCapabilities.chrome();
				options.addArguments("test-type");
				options.addArguments("--start-maximized");
				cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
				cap.setCapability(ChromeOptions.CAPABILITY, options);
				System.setProperty("java.net.preferIPv4Stack", "true");
				System.setProperty("webdriver.chrome.driver", "binaries/chromedriver.exe");
				driver = new ChromeDriver(cap);
			}

			if (browser.equalsIgnoreCase("iexplorer")) {
				System.setProperty("webdriver.ie.driver", "binaries/IEDriverServer.exe");
				driver = new InternetExplorerDriver();
			}

			driver.manage().window().maximize();
			Log.info(browser + " browser launched successfully.");

		} catch (Exception e) {
			Log.error("Not able to launch browser: " + e.getMessage());
			throw e;
		}
		return driver;
	}

	public void launchURL(String URL) {
		try {
			driver.get(URL);
			Log.info(URL + "URL launched successfully.");
		} catch (Exception e) {
			Log.error("Not able to launch URL: " + e.getMessage());
			throw e;
		}
	}

	public static WebDriver getDriver() {
		return driver;
	}
}
