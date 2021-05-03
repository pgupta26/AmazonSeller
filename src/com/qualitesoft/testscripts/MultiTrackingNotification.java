package com.qualitesoft.testscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;
import com.qualitesoft.core.Xls_Reader;

import io.github.sukgu.Shadow;

public class MultiTrackingNotification extends InitializeTest {
	
	@Test
	@Parameters({"filename"})
	public void lateDeliveryNotification(String filename) {
		
		try {
			 
			Xls_Reader xr=new Xls_Reader("binaries/"+filename);
			int rowsCount = xr.getRowCount("Sheet1");
			Log.info("Total Rows Count: "+rowsCount);
			
			String str = "Hello.\r\n" + 
					"\r\n" + 
					"Regarding your order for Tvilum products, we are reaching out to you to inform you that your item shipped in multiple boxes.\r\n" + 
					"\r\n" + 
					"Due to Amazon's restrictions, we were only able to communicate one tracking number.\r\n" + 
					"\r\n" + 
					"Your FedEx tracking numbers for your order are:\r\n" + 
					"\r\n" + 
					"\"DATA FROM COLUMN D\" \r\n" + 
					"\r\n" + 
					" \r\n" + 
					"\r\n" + 
					"Thank you\r\n" + 
					"Cymax Customer Support";
			
			for(int i=2;i<=rowsCount;i++) {
				
				try {
					
					String PONumber=xr.getCellData("Sheet1","PONumber", i).trim();
					Log.info("Row Number: "+i+" Order ID: "+PONumber);
					
					String trackingNumbers=xr.getCellData("Sheet1","TrackingNumbers", i).trim();
					str = str.replace("DATA FROM COLUMN D", trackingNumbers);
					
					String OrderDetailPage = "https://sellercentral.amazon.com/orders-v3/order/"+PONumber;
					driver.get(OrderDetailPage);
					Thread.sleep(3000);
		
					/*// Hover over Orders tab
					SeleniumFunction.moveToElement(driver,
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Orders')]"), 20));
					WaitTool.sleep(2);

					// Click on Manage Orders link under Orders tab
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Manage Orders')]"), 10));
					
					//Send Order ID
					SeleniumFunction.sendKeys(WaitTool.waitForElementPresentAndDisplay(driver, By.id("myo-search-input"),10), orderID);
					
					//Click Search button
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.id("myo-search-button-announce"), 10));*/
					
					//Click Buyer Name Link
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[@data-test-id='buyer-name-with-link']"), 10));
					
					//Switch to new tab
					SeleniumFunction.getCurrentWindow(driver);
					WaitTool.sleep(3);
					
					//Click on Other option button
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-radiobutton[@label='Other']"), 10));
					
					//Send Complete Message
					Shadow shadow = new Shadow(driver);
					WebElement element = shadow.findElement("textarea[part='textarea']");
					element.sendKeys(str);
					
					//Click Send Button
					/*SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-button[@label='Send']"), 10));*/
					Log.info("Notification for Row Number: "+i+" sent successsfully.");
					xr.setCellData("Sheet1", "Status", i, "Pass");
					
					//Close current tab
					driver.close();
					
					//Switch to parent window
					SeleniumFunction.getCurrentWindow(driver);
					WaitTool.sleep(2);
					
				}catch(Exception e) {
					xr.setCellData("Sheet1", "Status", i, "Fail");
				}
			}	
			
		}catch(Exception ex) {
			Log.info("Not able to find input file at specified path.");
			ex.printStackTrace();
		}
	}
}
