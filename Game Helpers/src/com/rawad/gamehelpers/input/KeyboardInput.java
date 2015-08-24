package com.rawad.gamehelpers.input;

import java.util.ArrayList;
import java.util.HashMap;

import com.rawad.gamehelpers.log.Logger;

/**
 * Buffering pressed keys and storing them in here doesn't work out too well for the player because the array list isn't constantly being
 * filled every frame, sadly. (key listener doesnt update which keys are pressed fast enough). We could, when adding things to the key
 * buffer, check if the key is down or not in the keyStates list but, that sounds too complicated.
 * 
 * @author Rawad
 *
 */
public class KeyboardInput {
	
	/** For single key pressed. */
	private static HashMap<Integer, Boolean> keyStates;
	
	/** Pressed Keys Buffer */
	private static ArrayList<Integer> pressedKeys;
	private static ArrayList<String> typedKeys;
	
	private static boolean setKeyBackUp;
	
	static {
		
		keyStates = new HashMap<Integer, Boolean>();
		
		pressedKeys = new ArrayList<Integer>();
		typedKeys = new ArrayList<String>();
		
		setKeyBackUp = true;
		
	}
	
	/**
	 * Sets the specified {@code key} to be pressed or not based on the {@code value} paramater. Also, if it pressed down, adds it to 
	 * the {@code pressedKeys} list of buffered key pressed.
	 * 
	 * @param key
	 * @param value
	 */
	protected static void setKeyDown(int key, boolean value) {
		// Protected so only EventHandler class from this package can access it.
		keyStates.put(key, value);
		
		if(value) {// Key is pressed down
			pressedKeys.add(key);
			
		}
		
	}
	
	/**
	 * Could add isKeyDown(int) method for convenience AFTER changing all the calls for it, to see if its really needed.
	 * 
	 * @param key
	 * @param setKeyBackUp
	 * @return
	 */
	public static boolean isKeyDown(int key, boolean setKeyBackUp) {
		
		try {
			
			boolean value = keyStates.get(key);
			
			if(setKeyBackUp) {
				setKeyDown(key, false);
			}
			
			return value;
			
		} catch(Exception ex) {
			return false;
		}
		
	}
	
	public static boolean isKeyDown(int key) {
		return isKeyDown(key, setKeyBackUp);
	}
	
	public static void setConsumeAfterRequest(boolean setKeyBackUp) {
		KeyboardInput.setKeyBackUp = setKeyBackUp;
	}
	
	/**
	 * Returns an integers representing which keys are currently down; makes them easy to iterate over.
	 * Also 'consumes' depending on whether or not {@code setKeyBackUp} is on or not.
	 * 
	 * @return
	 */
	@Deprecated
	public static int[] getPressedKeys() {
		
		int[] re = new int[pressedKeys.size()];
		
		for(int i = 0; i < pressedKeys.size(); i++) {
				
			try {
				re[i] = pressedKeys.get(i);
				
			} catch(ArrayIndexOutOfBoundsException ex) {
				Logger.log(Logger.SEVERE, "Got array index out of bounds while trying to get which keys are PRESSED. Index: " + i);
			}
			
		}
		
		KeyboardInput.clearPressedKeysBuffer();
		
		return re;
		
//		Iterator<Entry<Integer, Boolean>> it = keyStates.entrySet().iterator();
//		ArrayList<Integer> list = new ArrayList<Integer>();
//		
//		while(it.hasNext()) {
//			
//			Entry<Integer, Boolean> curEntry = it.next();
//			
//			if(curEntry.getValue()) {// For the non-null, but false values.
//				list.add(curEntry.getKey());
//				
//				if(setKeyBackUp) {
//					keyStates.put(curEntry.getKey(), false);
//				}
//				
//			}
//			
////			it.remove();// breaks everything...
//		}
//		
//		int[] values = new int[list.size()];
//		
//		for(int i = 0; i < values.length; i++) {
//			values[i] = list.get(i);
//		}
//		
//		return values;
		
	}
	
	public static void addTypedKey(char typedKey) {
		typedKeys.add(String.valueOf(typedKey));
	}
	
	public static String[] getTypedKeys() {
		
		String[] re = new String[typedKeys.size()];
		
		for(int i = 0; i < typedKeys.size(); i++) {
			
			try {
				re[i] = typedKeys.get(i);
			} catch(ArrayIndexOutOfBoundsException ex) {
				Logger.log(Logger.SEVERE, "Got array index out of bounds while trying to get which keys are TYPED. Index: " + i);
			}
			
		}
		
//		if(setKeyBackUp) {// Should always clear a buffer
			KeyboardInput.clearTypedKeysBuffer();
//		}
		
		return re;
		
	}
	
	public static void clearTypedKeysBuffer() {
		typedKeys.clear();
	}
	
	public static void clearPressedKeysBuffer() {
		pressedKeys.clear();
	}
	
}
