package com.rawad.gamehelpers.log;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Logger {
	
	public static final int DEBUG = 0;
	public static final int WARNING = 1;
	public static final int SEVERE = 2;
	
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm:ss");
	
	private static ArrayList<PrintStream> printStreams = new ArrayList<PrintStream>();
	
	static {
		
		printStreams.add(System.out);
		
	}
	
	public static void log(int code, String message) {
		
		String output = "[" + timeFormat.format(Calendar.getInstance().getTime()) + "] " 
				+ "[" + Thread.currentThread().getName() + "]" + " Code: " + code + " Message: " + message;
		
		for(PrintStream printStream: printStreams) {
			printStream.println(output);
		}
		
	}
	
	public static ArrayList<PrintStream> getPrintStreams() {
		return printStreams;
	}
	
}
