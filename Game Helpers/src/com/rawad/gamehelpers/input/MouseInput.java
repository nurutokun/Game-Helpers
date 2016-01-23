package com.rawad.gamehelpers.input;

import java.awt.AWTException;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.util.HashMap;

import com.rawad.gamehelpers.display.Cursors;
import com.rawad.gamehelpers.log.Logger;

public class MouseInput {
	
	public static final int LEFT_MOUSE_BUTTON = 0;
	public static final int RIGHT_MOUSE_BUTTON = 1;
	public static final int MIDDLE_MOUSE_BUTTON = 2;
	
	private static final int DOWN_INDEX = 0;
	private static final int CLICK_INDEX = 1;// Prevents click, drag-on-to-surface, release to count as a click
	
	private static HashMap<Integer, Boolean[]> mouseStates;
	
	private static Robot bot;
	
	private static Cursor cursor;
	
	private static double scaleX;
	private static double scaleY;
	
	private static int x;
	private static int y;
	
	private static int dx;
	private static int dy;
	
	private static int clampX;// Both used for when mouse is clamped.
	private static int clampY;
	
	private static int mouseWheelPosition;
	
	private static boolean clamped;
	
	static {
		
		mouseStates = new HashMap<Integer, Boolean[]>();
		
		mouseStates.put(LEFT_MOUSE_BUTTON, new Boolean[]{false, false});
		mouseStates.put(RIGHT_MOUSE_BUTTON, new Boolean[]{false, false});
		mouseStates.put(MIDDLE_MOUSE_BUTTON, new Boolean[]{false, false});
		
		scaleX = 1;
		scaleY = 1;
		
	}
	
	public static void update(Container window, long timePassed) {
		
		if(bot == null) {
			
			try {
				bot = new Robot();
				
			} catch(AWTException ex) {
				Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; Robot wasn't initialized");
				return;
			}
			
		}
		
		if(isClamped() && window.isShowing()) {
			
			setCursor(Cursors.BLANK);
			
			int scaledClampX = (int) (clampX * scaleX);
			int scaledClampY = (int) (clampY * scaleY);
			
//			Logger.log(Logger.DEBUG, "regular clamp x,y: " + clampX + ", " + clampY +
//					" | scaled clamp x,y: " + scaledClampX + ", " + scaledClampY + " | x,y: " + x + ", " + y);
			
			dx = x - clampX;
			dy = y - clampY;
			
			Point locationOnScreen = window.getLocationOnScreen();
			
			bot.mouseMove(scaledClampX + locationOnScreen.x, scaledClampY + locationOnScreen.y);
			
		}
		
		window.setCursor(cursor);
		
		setCursor(Cursors.DEFAULT);
		
	}
	
	public static boolean isButtonDown(int key) {
		
		try {
			
			return mouseStates.get(key)[DOWN_INDEX];
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to check for button that wasn't a button");
			return false;
		}
		
	}
	
	/**
	 * Not really used by any other class outside of this package so... protected!
	 * 
	 * @param key
	 * @param value
	 */
	protected static void setButtonDown(int key, boolean value) {
		
		try {
			
			setIndexedValue(key, DOWN_INDEX, value);
			
		} catch(Exception ex) {
			// key isn't valid
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to set button that wasn't a button");
		}
		
	}
	
	@Deprecated
	public static boolean isButtonClicked(int key) {
		
		try {
			
			return mouseStates.get(key)[CLICK_INDEX];
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to check for button that wasn't a button");
			return false;
		}
		
	}
	
	public static void setButtonClicked(int key, boolean value) {
		
		try {
			
			setIndexedValue(key, CLICK_INDEX, value);
			
		} catch(Exception ex) {
			// key isn't valid
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; tried to set button that wasn't a button");
		}
		
	}
	
	private static void setIndexedValue(int mouseType, int index, boolean value) {
		
		Boolean[] currentValues = mouseStates.get(mouseType);
		
		currentValues[index] = value;
		
		mouseStates.put(mouseType, currentValues);
		
	}
	
	public static int getMouseWheelPosition() {
		
		int value = mouseWheelPosition;
		
		mouseWheelPosition = 0;
		
		return value;
	}
	
	public static void setMouseWheelPosition(int mouseWheelPosition) {
		MouseInput.mouseWheelPosition = mouseWheelPosition;		
	}
	
	public static void setClamped(boolean clamped, int clampX, int clampY) {
		
		if(bot != null) {
			MouseInput.clamped = clamped;
			
			MouseInput.clampX = clampX;
			MouseInput.clampY = clampY;
			
			// Avoids initial mouse "jump".
			MouseInput.setX(clampX);
			MouseInput.setY(clampY);
			
		}
		
	}
	
	public static boolean isClamped() {
		return clamped;
	}
	
	public static void setCursor(Cursors cursor) {
		setCursor(cursor.getValue());
	}
	
	public static void setCursor(Cursor cursor) {
		MouseInput.cursor = cursor;
	}
	
	public static int getX(boolean scale) {
		if(isClamped()) {
			return dx;
		} else {
			if(scale) {
				return (int) (x * scaleX);
			} else {
				return x;
			}
		}
	}
	
	public static void setX(int x) {
		MouseInput.x = x;
	}
	
	public static int getY(boolean scale) {
		if(isClamped())	 {
			return dy;
		} else {
			if(scale) {
				return (int) (y * scaleY);
			} else {
				return y;
			}
		}
	}
	
	public static void setY(int y) {
		MouseInput.y = y;
	}
	
	public static void setScaleX(double scaleX) {
		MouseInput.scaleX = scaleX;
	}
	
	public static double getScaleX() {
		return scaleX;
	}
	
	public static void setScaleY(double scaleY) {
		MouseInput.scaleY = scaleY;
	}
	
	public static double getScaleY() {
		return scaleY;
	}
	
}
