package com.qualitesoft.core;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShot extends InitializeTest {

	static String baseScreenShotsFolder;
	static BufferedImage image = null;	 

	public static void takeScreenShot(WebDriver driver, String fileName) {

		try {
			WaitTool.sleep(2);
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(baseScreenShotsFolder + "/" + fileName + ".png"));
			Log.info("Screenshot " + fileName + " successfully taken.");
		} catch (Exception e) {
			Log.warn("Not able to take " + fileName + " screen shot: " + e.getMessage());
		}
	}
	public static void takeFullScreenShot(String fileName) {

		Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		BufferedImage screenFullImage;
		try {
			screenFullImage = new Robot().createScreenCapture(screenRect);
			try {
				ImageIO.write(screenFullImage, "png", new File(baseScreenShotsFolder + "/" + fileName + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public  void takeScreenShotOnFailure(String fileName) {

		try {
			WebDriver driver = getDriver();
			WaitTool.sleep(2);
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(baseScreenShotsFolder + "/Failed/" + fileName + ".png"));
			Log.info("Faliure screenshot " + fileName + " successfully taken.");
		} catch (Exception e) {
			Log.warn("Not able to take " + fileName + " screen shot: " + e.getMessage());
		}
	}
	public static void takeScreenShotSystemExcel(String fileName) {

		try {
			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	    	ImageIO.write(image, "png", new File(baseScreenShotsFolder + "/" + fileName + ".png"));
			Log.info("Faliure screenshot " + fileName + " successfully taken.");
		} catch (Exception e) {
			Log.warn("Not able to take " + fileName + " screen shot: " + e.getMessage());
		}
	}
	public static String createScreenshotFolder(String testName) {
		try {
			baseScreenShotsFolder = System.getProperty("user.dir") + "/screen-shots/";
			SimpleDateFormat sdfmth = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
			Calendar cal = Calendar.getInstance();
			baseScreenShotsFolder = baseScreenShotsFolder + "/" + InitializeTest.project + "/" + testName + "/"
					+ sdfmth.format(cal.getTime()).replaceAll("\\s", "_");
			new File(baseScreenShotsFolder).mkdirs();
			return baseScreenShotsFolder;
		} catch (Exception e) {
			Log.warn("Not able to create scree-shot folder: " + e.getMessage());
			return null;
		}
	}
}
