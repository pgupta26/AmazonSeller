package com.qualitesoft.core;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitTool {

	
	public static WebElement waitForElementPresentAndDisplay(WebDriver driver, final By by, int timeOutInSeconds) {

		WebElement element;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			Log.info("Element by " + by + " found.");
			return element; // return the element
		} catch (Exception e) {
			Log.warn("not able to find element by (" + by + ") in '" + timeOutInSeconds + "' seconds");
			throw e;
		}
	}
	
	public static WebElement waitForElementPresentAndDisplaySoft(WebDriver driver, final By by, int timeOutInSeconds) {

		WebElement element;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			Log.info("Element by " + by + " found.");
			return element; // return the element
		} catch (Exception e) {
			Log.warn("not able to find element by (" + by + ") in '" + timeOutInSeconds + "' seconds");
			return null;
		}
	}
	
	public static List<WebElement> waitForElementsPresentAndDisplay(WebDriver driver, final By by, int timeOutInSeconds) {

		List<WebElement> elements;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
			Log.info("Elements by " + by + " found.");
			return elements; // return the element
		} catch (Exception e) {
			Log.warn("not able to find elements by (" + by + ") in '" + timeOutInSeconds + "' seconds");
			return null;
		}
	}

	public static WebElement waitForElementToBeClickable(WebDriver driver, final By by, int timeOutInSeconds) {

		WebElement element;
		try {
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			element = wait.until(ExpectedConditions.elementToBeClickable(by));
			Log.info("Element by " + by + " found.");
			return element; // return the element
		} catch (Exception e) {
			Log.warn("not able to find element by (" + by + ") in '" + timeOutInSeconds + "' seconds");
			return null;
		}
	}

	public static void sleep(int seconds) {

		try {
			long milliseconds = seconds * 1000;
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			Log.warn(e.getMessage());
		}
	}

	public static boolean isElementPresentAndDisplay(WebDriver driver, By by) {

		try {
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			Log.warn("Element (" + by + ") not found.");
			return false;
		}
	}
	
	public static WebElement returnWebElement(WebDriver driver, By by) {

		try {
			return driver.findElement(by);
		} catch (Exception e) {
			Log.warn("Element (" + by + ") not found.");
			throw e;
		}
	}
}
