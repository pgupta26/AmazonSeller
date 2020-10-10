package com.qualitesoft.testscripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

public class GetFeedbackRating extends InitializeTest {
	
	FileOutputStream outputStream;
	SXSSFWorkbook wb;
	String fileName;

	@Test
	@Parameters({"startdate","enddate"})
	public void testGetFeedbackRating(String startdate, String enddate) {

		//Hover over Performance tab
		SeleniumFunction.moveToElement(driver, WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Performance')]"), 20));
		WaitTool.sleep(2);

		//Click on Feedback link under Performance tab
		SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Feedback')]"), 10));

		//Store data into excel
		storeFeedbackRatingIntoExcel(startdate,enddate);
	}

	public void storeFeedbackRatingIntoExcel(String startDate, String endDate) {

		try {
			// Create SimpleDateFormat object 
			SimpleDateFormat sdfo = new SimpleDateFormat("MM/dd/yyyy"); 

			// Get the two dates to be compared 
			Date startdate = sdfo.parse(startDate); 
			Date enddate = sdfo.parse(endDate);
			Date ratingdate;
			int count = 0;

			List<WebElement> allRows = null;
			List<WebElement> allColumns = null;

			//Read data and store into excel
			WaitTool.sleep(5);

			//Create dynamic file name
			Date myDate=new Date();
			SimpleDateFormat dateFormat=new SimpleDateFormat("MMddyyyyhhmmss");
			String currentDate=dateFormat.format(myDate);
			fileName="binaries/"+"FeedbackDetails"+"_"+currentDate+".xlsx";
			Log.info("Dynamic file name is: "+fileName);

			XSSFWorkbook workbook = new XSSFWorkbook();		
			wb = new SXSSFWorkbook(workbook); 
			wb.setCompressTempFiles(true);

			SXSSFSheet sh = (SXSSFSheet) wb.createSheet("FeedbackDetails");
			sh.setRandomAccessWindowSize(100);

			//XSSFSheet sheet = workbook.getSheet("Sheet1");
			int rowCount=sh.createRow(0).getLastCellNum();
			Row row;

			int pageCount=Integer.parseInt(SeleniumFunction.getText(driver.findElement(By.xpath("//kat-pagination/ul/li[@class='ellipsis']/following-sibling::li"))));
			Log.info("Total number of pages are: "+pageCount);

				outerloop:
				for(int pageCounter=1; pageCounter<=pageCount; pageCounter++) 
				{
					WaitTool.sleep(3);
					allRows=driver.findElements(By.xpath("//kat-tab[@label='All ratings']//kat-table[@role='table']/kat-table-body/kat-table-row"));
					System.out.println("Page Number "+pageCounter+"Total Rows: "+allRows.size());

					for(int i=1; i<=allRows.size(); i++) {
						allColumns = driver.findElements(By.xpath("//kat-tab[@label='All ratings']//kat-table[@role='table']/kat-table-body/kat-table-row["+i+"]/kat-table-cell"));
						ratingdate = sdfo.parse(allColumns.get(0).getText());

						if (startdate.compareTo(ratingdate) > 0 || enddate.compareTo(ratingdate) < 0) { 
							if(count == 1){
								break outerloop;
							}
						} else {
							rowCount++;
							row=sh.createRow(rowCount);
							System.out.println(allColumns.get(0).getText());
							System.out.println(allColumns.get(1).getText());
							System.out.println(allColumns.get(2).getText());
							System.out.println(allColumns.get(3).getText());
							row.createCell(0).setCellValue(allColumns.get(0).getText());
							row.createCell(1).setCellValue(allColumns.get(1).getText());
							row.createCell(2).setCellValue(allColumns.get(2).getText());
							row.createCell(3).setCellValue(allColumns.get(3).getText());
							count=1;
						}
					}

					//Click on next page icon
					String value = driver.findElement(By.xpath("//kat-pagination/ul/li[@class='nav nav-right']/kat-icon")).getAttribute("class");
					if(!value.contains("right-end")) {
						SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//kat-pagination/ul/li[@class='nav nav-right']/kat-icon"), 5));
					}
				}	

			outputStream = new FileOutputStream(fileName);
			wb.write(outputStream);
			wb.close();
			outputStream.close();

		}catch(Exception ex) {
			try {
				outputStream = new FileOutputStream(fileName);
				wb.write(outputStream);
				wb.close();
				outputStream.close();
				
			}catch(Exception e) {
				System.out.println("Error occurred while writing in file.....");
				e.printStackTrace();
			}
		}
	}
}

