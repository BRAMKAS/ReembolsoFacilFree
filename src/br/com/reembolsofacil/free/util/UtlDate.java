package br.com.reembolsofacil.free.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UtlDate {

	public static Date getDate(int day, int month, int year){
		Calendar c = Calendar.getInstance();
		c.set(year, month+1, day);
		return c.getTime();
	}
	
	public static Date getDate(int day, int month, int year, int hour, int minute){
		Calendar c = Calendar.getInstance();
		c.set(year, month+1, day, hour, minute);
		return c.getTime();
	}
	
	public static String getStringFromDate(Date d){
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(d);
	}
	
	public static String getStringFromDate(Date d, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(d);
	}
	
	public static Date getDateFromString(String date){
		Date dt = null;
		try {
			dt = new SimpleDateFormat().parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}  
		return dt;
	}
	
	public static Date getDateFromString(String date, String pattern){
		Date dt = null;
		try {
			dt = new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}  
		return dt;
	}
	
	public static String getMedium(Date d, Locale locale){
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);  
		return df.format(d);
	}
	
	public static String getShort(Date d, Locale locale){
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);  
		return df.format(d);
	}
	
	public static String getLong(Date d, Locale locale){
		DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, locale);  
		return df.format(d);
	}
}
