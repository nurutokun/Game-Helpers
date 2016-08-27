package com.rawad.gamehelpers.utils;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.rawad.gamehelpers.log.Logger;

public final class Util {
	
	/** New-line string that should be used anytime a new-line is required. This should work with almost every operating
	 * system and allows for consistency. */
	public static final String NL = "\r\n";
	
	/** Holds the number of virtual processors available to the JVM. */
	public static final int PROCESSORS = Runtime.getRuntime().availableProcessors();
	
	private Util() {}
	
	/**
	 * Returns {@code false} if either {@code a} or {@code b} are {@code null} or they are not equal and {@code true} if 
	 * they are equal. Can be considered an 'exclusive equals'.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		
		if(a == null || b == null) return false;
		if(a.equals(b)) return true;
		
		return false;
		
	}
	
	public static double clamp(double value, double min, double max) {
		
		if(value < min) {
			return min;
		} else if(value > max) {
			return max;
		}
		
		return value;
		
	}
	
	public static HashMap<String, String> parseCommandLineArguments(String... args) {
		
		HashMap<String, String> commands = new HashMap<String, String>();
		
		for(String command: args) {
			
			String[] tokens = command.split("=");
			
			try {
				commands.put(tokens[0], tokens[1]);
			} catch(Exception ex) {
				Logger.log(Logger.WARNING, "Proper command usage: \"Identifier=Value\"");
			}
			
		}
		
		return commands;
		
	}
	
	public static String getDefaultDirectory(String os) {
		
		String re = System.getProperty("user.dir");
		
		os = os.toUpperCase();
		
		if(os.contains("WIN")) {
			re = System.getenv("APPDATA");
		} else if(os.contains("NUX")) {
			re = System.getProperty("user.home");
		} else if(os.contains("MAC")) {
			re = System.getProperty("user.home") + "/Library/Application Support";
		}
		
		return re;
		
	}
	
	public static String getStringFromLines(String regex, boolean addRegexToEnd, ArrayList<String> lines) {
		return Util.getStringFromLines(regex, addRegexToEnd, lines.toArray(new String[lines.size()]));
	}
	
	public static String getStringFromLines(String regex, boolean addRegexToEnd, String... lines) {
		
		String re = "";
		
		for(int i = 0; i < lines.length; i++) {
			
			if(lines[i] == null || lines[i].isEmpty()) continue;// Questionable...
			
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
			return 0d;
		}
		
	}
	
	public static float parseFloat(String potentialFloat) {
		
		try {
			return Float.parseFloat(potentialFloat);
		} catch(Exception ex) {
			return 0f;
		}
		
	}
	
	public static <T extends Closeable> void silentClose(T streamToClose) {
		
		try {
			streamToClose.close();
		} catch(IOException | NullPointerException ex) {
			Logger.log(Logger.WARNING, "The stream: " + streamToClose + " could not be closed");
		}
		
	}
	
	@SafeVarargs
	public static <T> T[] append(T[] arr, T... lastElements) {
		final int length = arr.length;
		
		arr = Arrays.copyOf(arr, length + lastElements.length);
		
		for(int i = 0; i < lastElements.length; i++) {
			arr[length + i] = lastElements[i];
		}
		
	    return arr;
	}
	
	@SafeVarargs
	public static <T> T[] prepend(T[] arr, T... firstElements) {
		final int length = firstElements.length;
		
		firstElements = Arrays.copyOf(firstElements, length + arr.length);
		
		for(int i = length; i < firstElements.length; i++) {
			firstElements[i] = arr[i - length];
		}
		
	    return firstElements;
	}
	
	/**
	 * Should only give an error at the level of another class using this method. Putting a log on that shows that
	 * it's casting to the same object...
	 * 
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends A, A> T cast(A obj) {
		return (T) obj;
	}
	
}
