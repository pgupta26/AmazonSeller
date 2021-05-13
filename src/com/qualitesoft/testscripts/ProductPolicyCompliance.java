package com.qualitesoft.testscripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class ProductPolicyCompliance extends InitializeTest  {
	
	FileOutputStream outputStream;
	SXSSFWorkbook wb;
	String fileName;

	@Test
	public void testGetPolicyComplianceDetails() throws IOException {

		
		// Store data into excel
		storePolicyComplianceDetailsIntoExcel();
	}

	public String storePolicyComplianceDetailsIntoExcel() {

		try {
			
			// Hover over Performance tab
			SeleniumFunction.moveToElement(driver,
					WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Performance')]"), 20));
			WaitTool.sleep(2);

			// Click on Account Health under Performance tab
			SeleniumFunction.click(
					WaitTool.waitForElementPresentAndDisplay(driver, By.partialLinkText("Account Health"), 10));

			//
			SeleniumFunction.scrollDownByPixel(driver, "700");
			SeleniumFunction.click(
					WaitTool.waitForElementPresentAndDisplay(driver, By.partialLinkText("Restricted Product Policy Violations"), 10));

			String title;
			String asin;
			String actionTaken;
			

			List<WebElement> allRows = null;
			List<WebElement> allColumns = null;

			// Read data and store into excel
			WaitTool.sleep(5);

			// Create dynamic file name
			Date myDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmss");
			String currentDate = dateFormat.format(myDate);
			fileName = "binaries/" + "ProductPolicyComplianceData" + "_" + currentDate + ".xlsx";
			Log.info("Dynamic file name is: " + fileName);

			XSSFWorkbook workbook = new XSSFWorkbook();
			wb = new SXSSFWorkbook(workbook);
			wb.setCompressTempFiles(true);

			SXSSFSheet sh = (SXSSFSheet) wb.createSheet("ProductPolicyComplianceData");
			sh.setRandomAccessWindowSize(100);

			int rowCount = sh.createRow(0).getLastCellNum();
			Row row;

			//Get count of pages
			String str=driver.findElement(By.xpath("//kat-label[@variant='default']")).getAttribute("text").trim();
			str = str.substring(11, str.length());
			int pageCount = Integer.parseInt(str);
			Log.info("Total number of pages are: " + pageCount);
			
			for (int pageCounter = 1; pageCounter <= pageCount; pageCounter++) {
				WaitTool.sleep(3);
				
				allRows = WaitTool.waitForElementsPresentAndDisplay(driver, By.xpath("//div[@class='kat-row kat-no-gutters ahd-product-policy-table-row']"), 20);
				Log.info("Page Number: " + pageCounter + " Total Rows: " + allRows.size());

				for (int i = 1; i <= allRows.size(); i++) {
					allColumns = driver.findElements(By.xpath("(//div[@class='kat-row kat-no-gutters ahd-product-policy-table-row'])["+i+"]/div"));
					
					title =allColumns.get(2).findElement(By.xpath("(./span)[1]")).getText();
					asin =allColumns.get(2).findElement(By.xpath("(./span)[2]")).getText().split(":")[1].trim();
					actionTaken = allColumns.get(3).findElement(By.tagName("span")).getText();
					rowCount++;
					row = sh.createRow(rowCount);
					row.createCell(0).setCellValue(title);
					row.createCell(1).setCellValue(asin);
					row.createCell(2).setCellValue(actionTaken);					
				}

				// Click on next page icon
				if(WaitTool.isElementPresentAndDisplay(driver, By.name("chevron-right"))) {
					SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.name("chevron-right"), 10));

				}
			}

			outputStream = new FileOutputStream(fileName);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

		} catch (Exception ex) {
			try {
				outputStream = new FileOutputStream(fileName);
				wb.write(outputStream);
				wb.close();
				outputStream.close();

			} catch (Exception e) {
				System.out.println("Error occurred while writing in file.....");
				e.printStackTrace();
			}
		}
		return fileName;
	}


}
