package com.qualitesoft.testscripts;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.ScreenShot;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;
import com.qualitesoft.core.Xls_Reader;

import io.github.sukgu.Shadow;

public class NoTracking extends InitializeTest {

	public void selectContactReason() {
		WebElement otherRationButton = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-radiobutton[@label='Other']"), 30);
		boolean flag = false;
		if(otherRationButton.getAttribute("disabled") == null) {
			flag = true;
		}

		if(flag) {
			SeleniumFunction.click(
					WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-radiobutton[@label='Other']"), 10));

		} else {
			SeleniumFunction.click(
					WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-radiobutton[@label='Notify of a problem with shipping your order']"), 10));
		}
	}

	@Test
	@Parameters({"filename","startrecord","endrecord"})
	public void lateDeliveryNotification(String filename, int startrecord, int endrecord) {

		try {

			Xls_Reader xr=new Xls_Reader("binaries/"+filename);

			String emailTemplate = "Hello valued customer, \r\n"
					+ "\r\n"
					+ "Thank you for placing an order with us at Cymax on Amazon.\r\n"
					+ "\r\n"
					+ "We wanted to reach out to you to inform you that the order you had placed with our supplier, “Hirsh Industries”, encountered logistical issues which will cause a delay in your purchase.\r\n"
					+ "You may notice multiple tracking numbers have been provided to you and/or the tracking number(s) provided to you show no movement or scans.  We are aware of this issue, and it is currently being corrected.\r\n"
					+ "As soon as we receive updates from the supplier, we will inform you of the corrected tracking information.\r\n"
					+ "While we do our absolute best to deliver your orders on time, we apologize for these rare exceptions with supply chain disruptions. In this case, please expect delays of up to one to two weeks for your order.  \r\n"
					+ "If you are able to wait for your order, please let us know and we can provide you with a 10% refund as our appreciation for your continued patience. \r\n"
					+ "\r\n"
					+ "If you do not wish to wait and would like us to cancel your order for a full refund, please let us know as soon as possible so that we can process your request.\r\n"
					+ "\r\n"
					+ "Please note if you plan to re-order the same product, it will incur the same or longer delays.\r\n"
					+ "Once again, please accept our most sincere apologies while we try to correct this issue and we greatly appreciate your understanding and patience.\r\n"
					+ "\r\n"
					+ "Cymax Customer Support\r\n"
					+ "";


			for(int i=startrecord;i<=endrecord;i++) {

				try {

					String PONumber=xr.getCellData("Sheet1","Amazon POs", i).trim();
					Log.info("Row Number: "+i+" Order ID: "+PONumber);

					String OrderDetailPage = "https://sellercentral.amazon.com/orders-v3/order/"+PONumber;
					SeleniumFunction.getCurrentWindow(driver);
					driver.get(OrderDetailPage);
					WaitTool.sleep(3);

					//Click Buyer Name Link
					if(!WaitTool.isElementPresentAndDisplay(driver, By.xpath("//a[@data-test-id='buyer-name-with-link']"))) {
						Log.warn("Buyer link not found in 1st attempt. Retrying with url: "+OrderDetailPage);
						driver.get(OrderDetailPage);
						WaitTool.sleep(3);
					}
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[@data-test-id='buyer-name-with-link']"), 30));
					Log.info("Buyer name clicked.");

					WaitTool.sleep(1);				
					//Switch to new tab
					SeleniumFunction.getCurrentWindow(driver);
					WaitTool.sleep(5);
					Log.info("Switch to new window");

					//Click on Other option button
					this.selectContactReason();
					Log.info("Other option button clicked.");

					//Send Complete Message
					WaitTool.sleep(1);
					Shadow shadow = new Shadow(driver);
					WebElement element = shadow.findElement("textarea[part='textarea']");
					element.sendKeys(emailTemplate);
					Log.info("Message inserted");

					//Click Send Button
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-button[@label='Send']"), 10));
					WaitTool.sleep(3);
					Log.info("Notification for Row Number: "+i+" sent successsfully.");

					//Update status in file
					xr.setCellData("Sheet1", "Status", i, "Pass");
					Log.info("Status pass updated");

					//Close current tab
					driver.close();
					Log.info("Window closed");

					//Switch to parent window
					WaitTool.sleep(1);
					SeleniumFunction.getCurrentWindow(driver);
					WaitTool.sleep(2);
					Log.info("Switch to parent window");
				}catch(Exception e) {
					xr.setCellData("Sheet1", "Status", i, "Fail");
					Log.error("Notification Failed for Row Number: "+i+ " Message: "+e.getMessage());
					ScreenShot.takeScreenShot(driver, "Notification Failed for Row Number_"+i);
					Set<String> windows = driver.getWindowHandles();
					if(windows.size()==2)
						driver.close();
				}
			}	

		}catch(Exception ex) {
			Log.error(ex.getMessage());
			Assert.fail(ex.getMessage());
		}
	}
}
