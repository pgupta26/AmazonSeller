package com.qualitesoft.testscripts;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import com.qualitesoft.pageobjects.LoginPage;
import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.ScreenShot;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class LogOut extends InitializeTest{
	
	@Test
	public void testLogOut(){

		LoginPage loginPage = new LoginPage(driver);
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,-100050)", "");
		jse.executeScript("window.scrollBy(0,-100050)", "");
		jse.executeScript("window.scrollBy(0,-100050)", "");
		WaitTool.sleep(5);
		driver = getDriver();

		SeleniumFunction.hoverAction(driver,(loginPage.settings()));
		SeleniumFunction.click(loginPage.logout());
		WaitTool.sleep(5);
		ScreenShot.takeScreenShot(driver, "LoggedOut");	
	}
}

