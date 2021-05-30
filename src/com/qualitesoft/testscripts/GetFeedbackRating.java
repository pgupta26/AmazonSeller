package com.qualitesoft.testscripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.opencsv.CSVWriter;
import com.qualitesoft.core.InitializeTest;
import com.qualitesoft.core.Log;
import com.qualitesoft.core.SeleniumFunction;
import com.qualitesoft.core.WaitTool;

public class GetFeedbackRating extends InitializeTest {

	FileWriter outputfile;
	CSVWriter writer;
	String fileName;
	List<String[]> rows;

	@Test
	@Parameters({ "startdate", "enddate" })
	public void testGetFeedbackRating(String startdate, String enddate) throws IOException {

		// Hover over Performance tab
		SeleniumFunction.moveToElement(driver,
				WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Performance')]"), 20));
		WaitTool.sleep(2);

		// Click on Feedback link under Performance tab
		SeleniumFunction.click(
				WaitTool.waitForElementPresentAndDisplay(driver, By.xpath("//a[contains(text(),'Feedback')]"), 10));

		// Store data into excel
		String fileName = storeFeedbackRatingIntoExcel(startdate, enddate);
		
		// Transfer file is FTP location
		Path path = Paths.get(fileName);
		long bytes = Files.size(path);
		long size = bytes / 1024;
		if(size > 0) {
			ftpUpload(fileName);
		} else {
			Log.info("File not moved to FTP location because size is less than 0.");
		}
		
	}

	private static void showServerReply(FTPSClient ftpClient) {
		String[] replies = ftpClient.getReplyStrings();
		if (replies != null && replies.length > 0) {
			for (String aReply : replies) {
				System.out.println("SERVER: " + aReply);
			}
		}
	}

	public void ftpUpload(String fileName) {

		String server = "azure-cymax-ftp.cloudapp.net";
		int port = 21;
		String user = "QADEVOPS795";
		String pass = "dj$!dl9dsqdkl";
		String protocol = "TLS";

		FTPSClient ftpClient = new FTPSClient(protocol);

		try {

			ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
			ftpClient.connect(server, port);
			ftpClient.enterLocalPassiveMode();
			if (ftpClient.isConnected()) {
				Log.info("FTP Server connected");
			} else {
				Log.info("FTP Server not connected");
			}

			boolean result = ftpClient.login(user, pass);
			ftpClient.execPBSZ(0);
			ftpClient.execPROT("P");
			if (result) {
				Log.info("FTP log in");
			} else {
				Log.info("FTP log in failed");
			}
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			String splitFileName=fileName.split("/")[1];
			String firstRemoteFile = "Automation_Test"+File.separator+splitFileName;

			File firstLocalFile = new File(fileName);
			InputStream inputStream = new FileInputStream(firstLocalFile);

			Log.info("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			if (done) {
				Log.info("File is uploaded successfully.");
			}
			inputStream.close();
		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String storeFeedbackRatingIntoExcel(String startDate, String endDate) {

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
			rows = new ArrayList<String[]>();
			String[] row = null;
			
			// Read data and store into excel
			WaitTool.sleep(5);

			// Create dynamic file name
			Date myDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyyhhmmss");
			String currentDate = dateFormat.format(myDate);
			fileName = "binaries/" + "FeedbackDetails" + "_" + currentDate + ".csv";
			Log.info("Dynamic file name is: " + fileName);

			// create FileWriter object with file as parameter
	        outputfile = new FileWriter(fileName);
	  
	        // create CSVWriter object filewriter object as parameter
	        writer = new CSVWriter(outputfile);
	        
	        // adding header to csv
	        String[] header = { "Date", "Rating", "Order ID", "Comments" };
	        writer.writeNext(header);

			int pageCount = Integer.parseInt(SeleniumFunction.getText(
					driver.findElement(By.xpath("//kat-pagination/ul/li[@class='ellipsis']/following-sibling::li"))));
			Log.info("Total number of pages are: " + pageCount);

			outerloop: for (int pageCounter = 1; pageCounter <= pageCount; pageCounter++) {
				WaitTool.sleep(3);
				allRows = driver.findElements(By.xpath(
						"//kat-tab[@label='All ratings']//kat-table[@role='table']/kat-table-body/kat-table-row"));
				Log.info("Page Number: " + pageCounter + " Total Rows: " + allRows.size());

				for (int i = 1; i <= allRows.size(); i++) {
					allColumns = driver.findElements(By
							.xpath("//kat-tab[@label='All ratings']//kat-table[@role='table']/kat-table-body/kat-table-row["
									+ i + "]/kat-table-cell"));
					ratingdate = sdfo.parse(allColumns.get(0).getText());

					if (!(startdate.compareTo(ratingdate) * ratingdate.compareTo(enddate) >= 0)) {
						if (count == 1) {
							break outerloop;
						}
					} else {
						row = new String[4];
						row[0] = allColumns.get(0).getText();
						row[1] = allColumns.get(1).getText();
						row[2] = allColumns.get(2).getText();
						row[3] = allColumns.get(3).getText();
						rows.add(row);
						count = 1;
					}
				}

				// Click on next page icon
				String value = driver.findElement(By.xpath("//kat-pagination/ul/li[@class='nav nav-right']/kat-icon"))
						.getAttribute("class");
				if (!value.contains("right-end")) {
					SeleniumFunction.click(WaitTool.waitForElementPresentAndDisplay(driver,
							By.xpath("//kat-pagination/ul/li[@class='nav nav-right']/kat-icon"), 5));
				}
			}
			
			writer.writeAll(rows);
			writer.close();

		} catch (Exception ex) {
			try {
		        writer = new CSVWriter(outputfile);
				writer.writeAll(rows);
				writer.close();

			} catch (Exception e) {
				System.out.println("Error occurred while writing in file.....");
				e.printStackTrace();
			}
		}
		return fileName;
	}
}
