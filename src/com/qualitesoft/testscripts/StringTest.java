package com.qualitesoft.testscripts;

public class StringTest {
	
	public static void main(String[] args) {
		String str="Hi,\r\n" + 
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
		
		str = str.replace("\"DATE FROM COLUMN G\"", "2021-05-01");
		System.out.println(str);
	}

}
