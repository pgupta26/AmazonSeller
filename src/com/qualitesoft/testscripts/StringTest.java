package com.qualitesoft.testscripts;

import com.qualitesoft.core.Log;
import com.qualitesoft.core.Xls_Reader;

public class StringTest {
	
	public static void main(String[] args) {
		
		String str = "ASIN: B07B3P9V92".split(":")[1].trim();
		 System.out.println(str);
		
	}

}
