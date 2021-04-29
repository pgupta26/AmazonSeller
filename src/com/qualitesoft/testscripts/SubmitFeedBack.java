package com.qualitesoft.testscripts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.ScreenShot;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;
import com.qualitesoft.core.Xls_Reader;

public class SubmitFeedBack extends InitializeTest {

	@Test
	@Parameters({"filename","startrecord","endrecord"})
	public void submitFeedBack(String filename, int startrecord, int endrecord) {
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
		Xls_Reader xr=new Xls_Reader("binaries/"+filename);

		for(int i=startrecord;i<=endrecord;i++)
		{
			try {
				String feedBackUrl=xr.getCellData("Sheet1","Feedback URL", i).trim();
				System.out.println(feedBackUrl);

				//feedBackUrl = "https://"+feedBackUrl1;
				driver.get(feedBackUrl);
				Thread.sleep(3000);

				//Request a review
				SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//span[@data-test-id='request-a-review-button']"), 30));

				//Check if review already submitted
				Thread.sleep(3000);
				boolean isYesButton = WaitTool.isElementPresentAndDisplay(driver, By.xpath("//kat-button[@label='Yes']"));
				System.out.println("Is Yes Button Present: "+isYesButton);

				boolean isReviewAlreadySubmitted = WaitTool.isElementPresentAndDisplay(driver, By.xpath("//div[text()='You have already requested a review for this order.']"));
				System.out.println("Is Review Already Submitted: "+isReviewAlreadySubmitted);

				boolean isNotEligible = WaitTool.isElementPresentAndDisplay(driver, By.xpath("//div[text()='You can’t use this feature to request a review outside the 5-30 day range after the order delivery date.']"));
				System.out.println("Is Not Eligible: "+isNotEligible);

				if(isYesButton) {

					//Confirmation
					WebElement element = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-button[@label='Yes']"), 60);
					((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

					Thread.sleep(3000);
					boolean isReviewRequested = WaitTool.isElementPresentAndDisplay(driver, By.xpath("//kat-statusindicator[@label='A review will be requested for this order.']"));
					System.out.println("Is Review Requested: "+isReviewRequested);

					if(isReviewRequested) {
						Date date = new Date();
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						// Use PST time zone to format the date in
						df.setTimeZone(TimeZone.getTimeZone("PST"));

						String formattedDate = df.format(date);
						System.out.println("After formatting: " + formattedDate);
						xr.setCellData("Sheet1", "Submitted On", i, formattedDate);
						Log.info("Date Time Updated Successfully for row :"+i);
						System.out.println("Date Time Updated Successfully for row: "+i);
					}

					int randomTime = (int) (Math.random()*10);
					Thread.sleep(randomTime*500);

				} else if (isReviewAlreadySubmitted) {
					xr.setCellData("Sheet1", "Submitted On", i, "Review Already Submitted");
				} else if (isNotEligible) {
					xr.setCellData("Sheet1", "Submitted On", i, "Not Eligible For Review");
				} else {	
					xr.setCellData("Sheet1", "Submitted On", i, "Something Went Wrong");
				}
			}catch(Exception ex) {
				xr.setCellData("Sheet1", "Submitted On", i, "Something Went Wrong");
			}
		}
	}
}
