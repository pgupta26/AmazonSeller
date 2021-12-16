package com.qualitesoft.testscripts;

import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;

import com.qualitesoft.core.ScreenShot;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;
import com.qualitesoft.pageobjects.LoginPage;



public class LoginCR extends InitializeTest  {

	@Test
	public void testLoginCR()  {

		LoginPage loginPage = new LoginPage(driver);
		WaitTool.sleep(5);
		loginPage.clickSignIn();

		SeleniumFunction.sendKeys(loginPage.emailField(),crusername);
		SeleniumFunction.sendKeys(loginPage.passwordfield(),crpassword);
		ScreenShot.takeScreenShot(driver, "Login Page ");
		SeleniumFunction.click(loginPage.signinbutton());
		WaitTool.sleep(3);

		System.out.println("Waiting for otp to appear------------");
		WaitTool.sleep(30);
		System.out.println("Wait Over................");
		
		//Select an account
		SeleniumFunction.click(loginPage.selectCountry());
		SeleniumFunction.click(loginPage.selectAccount());

	}
}

