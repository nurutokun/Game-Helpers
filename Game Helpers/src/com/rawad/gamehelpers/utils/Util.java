package com.rawad.gamehelpers.utils;

import java.awt.Color;
import java.io.Closeable;
import java.io.IOException;

import com.rawad.gamehelpers.log.Logger;

public final class Util {
	
	public static final String NL = System.lineSeparator();
	
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	private Util() {}
	
	public static String getStringFromLines(String[] lines, String regex, boolean addRegexToEnd) {
		
		String re = "";
		
		for(int i = 0; i < lines.length; i++) {
			
			if(lines[i].isEmpty()) continue;// Questionable...
			
			re += lines[i];
			
			if(i >= lines.length - 1) {
				
				if(addRegexToEnd) {
					re += regex;
				}
				
			} else {
				
				re += regex;
				
			}
			
		}
		
		return re;
		
	}
	
	public static <T> T getNullSafe(T valueToCheck, T defaultValue) {
		
		if(valueToCheck != null) {
			return valueToCheck;
		}
		
		return defaultValue;
		
	}
	
	public static int parseInt(String potentialInt) {
		
		try {
			return Integer.parseInt(potentialInt);
		} catch(Exception ex) {
			return 0;
		}
		
	}
	
	public static double parseDouble(String potentialDouble) {
		
		try {
			return Double.parseDouble(potentialDouble);
		} catch(Exception ex) {
			return 0.0d;
		}
		
	}
	
	public static <T extends Closeable> void silentClose(T streamToClose) {
		
		try {
			streamToClose.close();
		} catch(IOException ex) {
			Logger.log(Logger.WARNING, "The stream: " + streamToClose.toString() + " could not be closed");
		}
		
	}
	
	/**
	 * Should only give an error at the level of another class using this method. Putting a log on that shows that
	 * it's casting to the same object...
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends A,A> T cast(A obj) {
//		Logger.log(Logger.DEBUG, "Casted: " + obj + ", to: " + ((T) obj));
		return (T) obj;
	}
	
}
