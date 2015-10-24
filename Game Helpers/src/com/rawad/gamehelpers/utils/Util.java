package com.rawad.gamehelpers.utils;

import java.awt.Color;

public final class Util {
	
	public static final String NL = System.lineSeparator();
	
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	private Util() {}
	
	public static String getStringFromLines(String[] lines, String regex, boolean addRegexToEnd) {
		
		String re = "";
		
		for(int i = 0; i < lines.length; i++) {
			
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
	
}