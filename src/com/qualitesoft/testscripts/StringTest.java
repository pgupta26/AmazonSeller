package com.qualitesoft.testscripts;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import com.qualitesoft.core.Log;
import com.qualitesoft.core.Xls_Reader;

public class StringTest {
	
	public static void main(String[] args) throws ParseException {	
		DateFormat df=new SimpleDateFormat("MMM d");
		
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -40);
		System.out.println(df.format(cal.getTime()));
		cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String weekFirstDate=df.format(cal.getTime());
		
		cal.add(Calendar.DATE,6);
		String weekLastDate = df.format(cal.getTime());
		String dateRange = weekFirstDate+" - "+weekLastDate;
		System.out.println(dateRange);
		
		
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -75);
		cal.add( Calendar.DAY_OF_WEEK, (cal.get(Calendar.DAY_OF_WEEK)-2));
		//cal.add( Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK)-2)); 
		Date currentDate = dateFormat.parse(dateFormat.format(cal.getTime()));
		System.out.println(currentDate);
		System.out.println("Current Date: " +dateFormat.format(cal.getTime()));*/
	
		/*SimpleDateFormat sdfo = new SimpleDateFormat("MMMM dd, yyyy");
		Date portalDate = sdfo.parse("March 4, 2021");
		System.out.println("Current Date: " +dateFormat.format(portalDate.getTime()));
		
		if(portalDate.after(currentDate)) {
			System.out.println("Read Data");
		} else {
			System.out.println("Will not insert data");
		}*/
	}

}
