package com.rawad.gamehelpers.utils;

import java.awt.EventQueue;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import javax.swing.text.JTextComponent;

import com.rawad.gamehelpers.log.Logger;

public final class Util {
	
	public static final String NL = "\r\n";// System.lineSeperator(); // Current one much better and more consistent.
	
	private Util() {}
	
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
	
	/**
	 * Should be used for setting text of swing text components, outside of EDT. Note that when the text is actually
	 * set is not known. Also note that custom GUI components do this automatically (e.g. <code>TextLabel</code>).
	 * 
	 * @param textComp
	 * @param textToSet
	 */
	public static void setTextSafely(final JTextComponent textComp, final String textToSet) {
		
		invokeLater(new Runnable() {
			
			@Override
			public void run() {
				textComp.setText(textToSet);
			}
			
		});
		
	}
	
	public static void invokeLater(Runnable runnable) {
		
		EventQueue.invokeLater(runnable);
		
	}
	
	public static void invokeAndWait(Runnable runnable) {
		
		try {
			EventQueue.invokeAndWait(runnable);
		} catch (InvocationTargetException | InterruptedException ex) {
			ex.printStackTrace();
			Logger.log(Logger.SEVERE, "Error occured while trying to run thread " + runnable + " the thread "
					+ "didn't seem to like that...");
		}
		
	}
	
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
		} catch(IOException | NullPointerException ex) {
			Logger.log(Logger.WARNING, "The stream: " + streamToClose + " could not be closed");
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
		return (T) obj;
	}
	
}
