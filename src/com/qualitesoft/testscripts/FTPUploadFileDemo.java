package com.qualitesoft.testscripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;

public class FTPUploadFileDemo {
	
	private static void showServerReply(FTPSClient ftpClient) {
	    String[] replies = ftpClient.getReplyStrings();
	    if (replies != null && replies.length > 0) {
	        for (String aReply : replies) {
	            System.out.println("SERVER: " + aReply);
	        }
	    }
	}
	

	public static void main(String[] args) {

		String server = "azure-cymax-ftp.cloudapp.net";
		String user = "QADEVOPS795";
		String pass = "dj$!dl9dsqdkl";
		String protocol = "TLS";  

		FTPSClient  ftpClient = new FTPSClient (protocol);
		try {
			ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
			ftpClient.connect(server, 21);
			showServerReply(ftpClient);
			ftpClient.enterLocalPassiveMode();

			if(ftpClient.isConnected()) {
				System.out.println("Server connected successfully.");
			} else {
				System.out.println("Not able to connect");
			}
			
			boolean result =ftpClient.login(user, pass);
			showServerReply(ftpClient);
			 
			if(result) {
				System.out.println("Log in Successfully.");
			} else {
				System.out.println("Not able to login");
			}
			
			ftpClient.execPBSZ(0);
			ftpClient.execPROT("P");
			
			/*boolean directory = ftpClient.makeDirectory("test");
			if(directory) {
				System.out.println("directory created successfully.");
			} else {
				System.out.println("not able to create directory");
			}*/
			
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			String firstRemoteFile = "Automation_Test/FeedbackDetails.xlsx";
			
			File firstLocalFile = new File("E:/QualiteSoft/AmazonSeller/binaries/FeedbackDetails.xlsx");
			InputStream inputStream = new FileInputStream(firstLocalFile);

			System.out.println("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			showServerReply(ftpClient);

			if (done) {
				System.out.println("The first file is uploaded successfully.");
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

}
