package com.qualitesoft.testscripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class GetContentDetails extends InitializeTest {

	public void storeDataIntoExcel() throws IOException {
		
		List<WebElement> allRows = null;
		List<WebElement> allColumns = null;
		
		//Read data and store into excel
		WaitTool.sleep(10);
		
		//Create dynamic file name
		Date myDate=new Date();
		SimpleDateFormat dateFormat=new SimpleDateFormat("MMddyyyyhhmmss");
		String currentDate=dateFormat.format(myDate);
		String fileName="binaries/"+"ContentDetails"+"_"+currentDate+".xlsx";
		Log.info("Dynamic file name is: "+fileName);
		
		XSSFWorkbook workbook = new XSSFWorkbook();		
		SXSSFWorkbook wb = new SXSSFWorkbook(workbook); 
        wb.setCompressTempFiles(true);
        
        SXSSFSheet sh = (SXSSFSheet) wb.createSheet("ContentDetails");
        sh.setRandomAccessWindowSize(100);
        
		//XSSFSheet sheet = workbook.getSheet("Sheet1");
        int rowCount=sh.createRow(0).getLastCellNum();
		Row row;
		
		int pageCount=Integer.parseInt(SeleniumFunction.getText(driver.findElement(By.xpath("//li[@class='awsui-table-pagination-dots']/following-sibling::li[1]/button"))));
		Log.info("Total number of pages are: "+pageCount);
		
		for(int pageCounter=1; pageCounter<=pageCount; pageCounter++) 
		{
			WaitTool.sleep(5);
			allRows=driver.findElements(By.xpath("//*[@id='app-main']/div[3]/div/awsui-table/div/div[3]/table/descendant::tr"));
			System.out.println("Page Number "+pageCounter+" Total Rows: "+allRows.size());
			
			//for(WebElement row : allRows) {
			for(int i=1; i< allRows.size(); i++) {
				
				row=sh.createRow(++rowCount);
				
				allColumns = allRows.get(i).findElements(By.xpath("td/span"));

				row.createCell(0).setCellValue(allColumns.get(0).getText());
				row.createCell(1).setCellValue(allColumns.get(1).getText());
				row.createCell(2).setCellValue(allColumns.get(2).getText());
				row.createCell(3).setCellValue(allColumns.get(3).getText());
				row.createCell(4).setCellValue(allColumns.get(4).getText());
				row.createCell(5).setCellValue(allColumns.get(5).getText());
			}

			//Click on next page icon
			SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.className("awsui-table-pagination-next-page"), 5));
		}	
				 
         FileOutputStream outputStream = new FileOutputStream(fileName);
         wb.write(outputStream);
         wb.close();
         outputStream.close();
	}

	@Test
	public void testGetContentDetails() throws IOException {

		//Click on A+ Content Manager link
		SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver, By.linkText("A+ Content Manager"), 10));
		
		//Store data into excel
		storeDataIntoExcel();
	}
}

