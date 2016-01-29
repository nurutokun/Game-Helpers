package com.rawad.gamehelpers.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	
	public static final int DEBUG = 0;
	public static final int WARNING = 1;
	public static final int SEVERE = 2;
	
	private static final SimpleDateFormat sdf;
	
	private static String buffer;
	
	static {
		buffer = "";
		
		sdf = new SimpleDateFormat("KK:mm:ss");
		
	}
	
	public static void log(int code, String message) {
		
		String output = "[" + sdf.format(Calendar.getInstance().getTime()) + "] " + "[" + Thread.currentThread().getName() + "]" 
				+ " Code: " + code + " Message: " + message;
		
		System.out.println(output);
		
		buffer += output + "\n";
		
	}
	
	public static String getBuffer() {
		
		String temp = buffer;
		
		buffer = "";
		
		return temp;
		
	}
	
}
