package com.qualitesoft.testscripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class OrdersWithDefectUpdated extends InitializeTest  {

	FileOutputStream outputStream;
	SXSSFWorkbook wb;
	String fileName;

	@Test
	public void testGetOrderWithDefectsDetails() throws IOException {

		// Hover over Performance tab
		SeleniumFunction.moveToElement(driver,
				WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Performance')]"), 20));
		WaitTool.sleep(2);

		// Click on Account Health under Performance tab
		SeleniumFunction.click(
				WaitTool.waitForElementPresentAndDisplay(driver, By.partialLinkText("Account Health"), 10));

		//Hover over Report link
		SeleniumFunction.moveToElement(driver, WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[@href='https://sellercentral.amazon.com/performance/report/order-defects?ref=sp_st_nav_sphreports']"), 10));


		//Click on Orders with Defects link
		SeleniumFunction.click(
				WaitTool.waitForElementPresentAndDisplay(driver, By.partialLinkText("Orders with Defects"), 10));

		// Store data into excel
		storeOrdersWithDefectsIntoExcel();
	}

	public String storeOrdersWithDefectsIntoExcel() {

		try {

			// Create dynamic file name
			Date myDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmss");
			String currentDate = dateFormat.format(myDate);
			fileName = "binaries/" +currentDate+"_Order-with-defects.xlsx";
			Log.info("Dynamic file name is: " + fileName);

			//Date from which we fetch the data
			Calendar cal=Calendar.getInstance();
			cal.add(Calendar.DATE, -82);
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			DateFormat df=new SimpleDateFormat("MMM d");
			String weekFirstDate=df.format(cal.getTime());

			cal.add(Calendar.DATE,6);
			String weekLastDate = df.format(cal.getTime());
			String dateRange = weekFirstDate+" - "+weekLastDate;
			Log.info("Fetch Data till week: "+dateRange);


			List<WebElement> rows = null;
			XSSFWorkbook workbook = new XSSFWorkbook();
			wb = new SXSSFWorkbook(workbook);
			wb.setCompressTempFiles(true);

			SXSSFSheet sh = (SXSSFSheet) wb.createSheet("OrderWithDefects");
			sh.setRandomAccessWindowSize(100);

			//Create first row of the sheet
			Row firstRow = sh.createRow(0);
			firstRow.createCell(0).setCellValue("Order Id");
			firstRow.createCell(1).setCellValue("Order Date");
			firstRow.createCell(2).setCellValue("Fulfilled By");
			firstRow.createCell(3).setCellValue("Pre-fulfillment Cancellation");
			firstRow.createCell(4).setCellValue("Late Shipment");
			firstRow.createCell(5).setCellValue("Negative Feedback");
			firstRow.createCell(6).setCellValue("A-to-z Guarantee claim");
			firstRow.createCell(7).setCellValue("Chargeback claim");
			int rowCount= sh.getLastRowNum();
			Row row;

			boolean flag = true;
			int pageCounter = 1;
			while (flag) {
				WaitTool.sleep(3);
				
				for(int columnIndex=4; columnIndex<=8;columnIndex++) {
					rows = WaitTool.waitForElementsPresentAndDisplay(driver, By.xpath("//table[@id='sp-owd-table']/tbody/tr[@class='a-text-center sp-owd-table-data-row']/td["+columnIndex+"]/i[@title='Has defect']"), 10);
					if(rows != null && rows.size() > 0) {
						Log.info("Page Number: " + pageCounter + " Total Rows for column " +columnIndex+ " are: " +rows.size());
						for (int i = 0; i < rows.size(); i++) {
							rowCount++;
							String orderID=rows.get(i).findElement(By.xpath("./ancestor::tr/td[1]")).getText();
							String orderDate1=rows.get(i).findElement(By.xpath("./ancestor::tr/td[2]")).getText();
							String fulfilledBy=rows.get(i).findElement(By.xpath("./ancestor::tr/td[3]")).getText();
							row = sh.createRow(rowCount);
							row.createCell(0).setCellValue(orderID);
							row.createCell(1).setCellValue(orderDate1);
							row.createCell(2).setCellValue(fulfilledBy);
							if(columnIndex==4) {
								row.createCell(3).setCellValue("1");
								row.createCell(4).setCellValue("0");
								row.createCell(5).setCellValue("0");
							} else if(columnIndex==5) {
								row.createCell(3).setCellValue("0");
								row.createCell(4).setCellValue("1");
								row.createCell(5).setCellValue("0");
							} else if(columnIndex==6) {
								row.createCell(3).setCellValue("0");
								row.createCell(4).setCellValue("0");
								row.createCell(5).setCellValue("1");
							}else if(columnIndex==7) {
								row.createCell(3).setCellValue("0");
								row.createCell(4).setCellValue("0");
								row.createCell(5).setCellValue("1");
							}else if(columnIndex==8) {
								row.createCell(3).setCellValue("0");
								row.createCell(4).setCellValue("0");
								row.createCell(5).setCellValue("1");
							}
							
						}
					}
				}
 				/*rows = WaitTool.waitForElementsPresentAndDisplay(driver, By.xpath("//table[@id='sp-owd-table']/tbody/tr[@class='a-text-center sp-owd-table-data-row']/td[6]/i[@title='Has defect']"), 10);
				if(rows != null && rows.size() > 0) {
					Log.info("Page Number: " + pageCounter + " Total Rows for Negative Feedback: " + rows.size());
					for (int i = 0; i < rows.size(); i++) {
						rowCount++;
						String orderID=rows.get(i).findElement(By.xpath("./ancestor::tr/td[1]")).getText();
						String orderDate1=rows.get(i).findElement(By.xpath("./ancestor::tr/td[2]")).getText();
						String fulfilledBy=rows.get(i).findElement(By.xpath("./ancestor::tr/td[3]")).getText();
						row = sh.createRow(rowCount);
						row.createCell(0).setCellValue(orderID);
						row.createCell(1).setCellValue(orderDate1);
						row.createCell(2).setCellValue(fulfilledBy);
						row.createCell(3).setCellValue("X");
						row.createCell(4).setCellValue(" ");
						row.createCell(5).setCellValue(" ");
					}
				}

				rows = WaitTool.waitForElementsPresentAndDisplay(driver, By.xpath("//table[@id='sp-owd-table']/tbody/tr[@class='a-text-center sp-owd-table-data-row']/td[7]/i[@title='Has defect']"), 10);
				if(rows != null && rows.size() > 0) {
					Log.info("Page Number: " + pageCounter + " Total Rows for A-to-z Guarantee claim: " + rows.size());
					for (int i = 0; i < rows.size(); i++) {
						rowCount++;
						String orderID=rows.get(i).findElement(By.xpath("./ancestor::tr/td[1]")).getText();
						String orderDate1=rows.get(i).findElement(By.xpath("./ancestor::tr/td[2]")).getText();
						String fulfilledBy=rows.get(i).findElement(By.xpath("./ancestor::tr/td[3]")).getText();
						row = sh.createRow(rowCount);
						row.createCell(0).setCellValue(orderID);
						row.createCell(1).setCellValue(orderDate1);
						row.createCell(2).setCellValue(fulfilledBy);
						row.createCell(3).setCellValue(" ");
						row.createCell(4).setCellValue("X");
						row.createCell(5).setCellValue(" ");
					}
				}

				rows = WaitTool.waitForElementsPresentAndDisplay(driver, By.xpath("//table[@id='sp-owd-table']/tbody/tr[@class='a-text-center sp-owd-table-data-row']/td[8]/i[@title='Has defect']"), 10);
				if(rows != null && rows.size() > 0) {
					Log.info("Page Number: " + pageCounter + " Total Rows for Chargeback claim: " + rows.size());
					for (int i = 0; i < rows.size(); i++) {
						rowCount++;
						String orderID=rows.get(i).findElement(By.xpath("./ancestor::tr/td[1]")).getText();
						String orderDate1=rows.get(i).findElement(By.xpath("./ancestor::tr/td[2]")).getText();
						String fulfilledBy=rows.get(i).findElement(By.xpath("./ancestor::tr/td[3]")).getText();
						row = sh.createRow(rowCount);
						row.createCell(0).setCellValue(orderID);
						row.createCell(1).setCellValue(orderDate1);
						row.createCell(2).setCellValue(fulfilledBy);
						row.createCell(3).setCellValue(" ");
						row.createCell(4).setCellValue(" ");
						row.createCell(5).setCellValue("X");
					}
				}*/
				
				//Get actual date range from the portal
				SeleniumFunction.scrollDownUptoFooter(driver);
				String actualDateRange = SeleniumFunction.getText(WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//span[@id='sp-owd-table-previous-timeframe-announce']"), 10));
				actualDateRange = actualDateRange.substring(2);
				Log.info("Next Click On Date Range: "+actualDateRange);
				
				// Click on next page icon
				SeleniumFunction.scrollDownUptoFooter(driver);
				SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplaySoft(driver, By.xpath("//span[@id='sp-owd-table-previous-timeframe']"), 10));
				pageCounter++;
				
				if(actualDateRange.equals(dateRange))
					flag=false;
				
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
				Log.info(ex.getMessage());
			} catch (Exception e) {
				Log.info("Error occurred while writing in file.....");
				e.printStackTrace();
			}
		}
		return fileName;
	}
}
