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

public class HarishLateShipmentNotification extends InitializeTest {

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
					+ "Thank you for placing an order with Cymax on Amazon.\r\n"
					+ "\r\n"
					+ "We wanted to reach out to you to apologize for the late shipment that you received from Hirsh Industries.  This is because the shipment was not scanned into the FedEx system before it was shipped causing the delay.   \r\n"
					+ "\r\n"
					+ "We also show that this was delivered to you without any issues reported.\r\n"
					+ "\r\n"
					+ "Once again, please accept our most sincere apologies for any inconveniences.\r\n"
					+ "\r\n"
					+ "If you do have any issues to report, please let us know and we would be happy to assist you.\r\n"
					+ "\r\n"
					+ "Kind Regards,\r\n"
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
