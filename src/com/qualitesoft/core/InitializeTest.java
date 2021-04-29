package com.qualitesoft.core;

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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

public class InitializeTest {

	public WebDriver driver = null;
	public static String browser = null;
	public static String retryCount = null;
	public static String project = null;
	public static String existingbrand = null;
	public static String templateused = null;
	private static String testname = null;
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
	
	@BeforeSuite
	public void logConfigure() {
		DOMConfigurator.configure("log4j.xml");
	}

	@BeforeClass
	public WebDriver setUp(ITestContext context) {
		project = context.getCurrentXmlTest().getParameter("project");
		browser = context.getCurrentXmlTest().getParameter("browser");
		URL = context.getCurrentXmlTest().getParameter("URL");
		crusername= context.getCurrentXmlTest().getParameter("crusername");
		crpassword= context.getCurrentXmlTest().getParameter("crpassword");
		testname = context.getCurrentXmlTest().getName();

		System.out.println("Test Started: "+context.getCurrentXmlTest().getName());
		
		 if(context.getCurrentXmlTest().getName().equals("Test Submit Feedback - TestData2"))
         {
           WaitTool.sleep(60);
         }
		 

		driver = launchBrowser(browser);
		launchURL(URL);
		Log.info(testname + " started.");
		return driver;
	}

	@AfterClass
	public void tearDown() {

		Log.info("Test Ended");
		driver.quit();
	}

	public WebDriver launchBrowser(String browser) {

		try {
			if (browser.equalsIgnoreCase("firefox")) {
				System.setProperty("webdriver.gecko.driver", "binaries/geckodriver.exe");
				driver = new FirefoxDriver();
			}

			if (browser.equalsIgnoreCase("chrome")) {
				HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
				chromePrefs.put("profile.default_content_settings.popups", 0);
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("prefs", chromePrefs);
				options.setBinary("C://Program Files//Google//Chrome//Application//chrome.exe");
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
			Log.info(URL+ " URL launched successfully.");
		} catch (Exception e) {
			Log.error("Not able to launch URL: " + e.getMessage());
			throw e;
		}
	}

	public WebDriver getDriver() {

		return driver;
	}
}
