package com.qualitesoft.testscripts;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;
import com.qualitesoft.core.Xls_Reader;

import io.github.sukgu.Shadow;

public class LateDeliveryNotification extends InitializeTest {
	
	public void selectContactReason() {
			WebElement otherRationButton = WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-radiobutton[@label='Other']"), 10);
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
			int rowsCount = xr.getRowCount("Sheet1");
			Log.info("Total Rows Count: "+rowsCount);
			
			String emailTemplate="Hi,\r\n" + 
					"\r\n" + 
					"Your package is still on the way however may arrive later than expected. We are working to have this delivered as soon as possible.\r\n" + 
					"\r\n" + 
					"Your order is currently estimated to arrive on \"DATE FROM COLUMN G\". If your item does not arrive by this date, and you no longer want to wait, feel free to message us to receive a refund.\r\n" + 
					"\r\n" + 
					"We sincerely apologize for any inconvenience and appreciate your patience in this matter.\r\n" + 
					"\r\n" + 
					"\r\n" + 
					"Kind Regards,\r\n" + 
					"Cymax Customer Support ";

			for(int i=startrecord;i<=endrecord;i++) {
				
				try {
					
					String PONumber=xr.getCellData("Sheet1","PONumber", i).trim();
					Log.info("Row Number: "+i+" Order ID: "+PONumber);
					
					String fedEx_EDD=xr.getCellData("Sheet1","FedEx_EDD", i).trim();
					String str = emailTemplate.replace("\"DATE FROM COLUMN G\"", fedEx_EDD);
					
					String OrderDetailPage = "https://sellercentral.amazon.com/orders-v3/order/"+PONumber;
					SeleniumFunction.getCurrentWindow(driver);
					driver.get(OrderDetailPage);
					WaitTool.sleep(3);
					
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
					WaitTool.sleep(5);
					
					//Click on Other option button
					this.selectContactReason();

					//Send Complete Message
					WaitTool.sleep(1);
					Shadow shadow = new Shadow(driver);
					WebElement element = shadow.findElement("textarea[part='textarea']");
					element.sendKeys(str);
					WaitTool.sleep(1);
					
					//Click Send Button
					SeleniumFunction.click(
							WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-button[@label='Send']"), 10));
					WaitTool.sleep(3);
					Log.info("Notification for Row Number: "+i+" sent successsfully.");
					xr.setCellData("Sheet1", "Status", i, "Pass");
					
					//Close current tab
					driver.close();

					//Switch to parent window
					WaitTool.sleep(1);
					SeleniumFunction.getCurrentWindow(driver);
					WaitTool.sleep(2);
					
				}catch(Exception e) {
					xr.setCellData("Sheet1", "Status", i, "Fail");
					Set<String> windows = driver.getWindowHandles();
					if(windows.size()==2)
						driver.close();
				}	
			}	
		}catch(Exception ex) {
			Log.info("Not able to find input file at specified path.");
			ex.printStackTrace();
		}
	}

}
