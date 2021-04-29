package com.qualitesoft.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class LoginPage {

	WebDriver driver;
	public LoginPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public WebElement OTPCode() {
		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.id("auth-mfa-otpcode"), 10);
		return webElement;
	}
	
	public void clickSignIn() {
		try {
			WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.cssSelector(".secondary"), 10);
			SeleniumFunction.click(webElement);
		}catch(Exception ex) {			
			WebElement webElement1 = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("(//div[@class='has-max-width'])[1]/div[2]/div[1]/div[1]/div/a/Strong"), 10);
			SeleniumFunction.click(webElement1);
		}
	}
	
	public WebElement SignInHomePage() {
		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.cssSelector(".secondary"), 10);
		return webElement;
	}

	public WebElement emailField(){

		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//input[@type='email']"), 10);
		return webElement;
	}

	public WebElement passwordfield(){

		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//input[@type='password']"), 10);
		return webElement;
	}


	public WebElement signinbutton(){

		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//input[@id='signInSubmit']"), 10);
		return webElement;
	}
	
	public WebElement outhSignIn(){

		WebElement webElement = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//input[@id='auth-signin-button']"), 10);
		return webElement;
	}

	public WebElement seemorelink() {
		return WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("By.linkText('See more fee discounts'))"), 30);
	}

	public WebElement settings() {

		return WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//*[@id=\"sc-quicklink-settings\"]/a"), 30);
	}
	public WebElement logout() {

		return WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//*[@id=\"sc-quicklink-settings\"]/ul/li[1]/a"), 30);
	}
	public WebElement pageTotal() {

		return WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//*[@id=\"sc-content-container\"]/div/my-app/div/div/home/div/div[2]/kat-tabs/kat-tab[1]/feedback-list/kat-pagination/ul/li[8]"), 30);
	}
	public WebElement clickIcon() {

		return WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//*[@id=\"sc-content-container\"]/div/my-app/div/div/home/div/div[2]/kat-tabs/kat-tab[1]/feedback-list/kat-pagination/ul/li[9]/kat-icon/i"), 30);
	}
}

