package com.rawad.gamehelpers.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.rawad.gamehelpers.log.Logger;

public class KeyboardInput {
	
	private static HashMap<Integer, Boolean> keyStates;
	
	private static ArrayList<String> typedKeys;
	
	private static boolean setKeyBackUp;
	
	static {
		
		keyStates = new HashMap<Integer, Boolean>();
		
		typedKeys = new ArrayList<String>();
		
		setKeyBackUp = true;
		
	}
	
	protected static void setKeyDown(int key, boolean value) {
		// Protected so only EventHandler class from this package can access it.
		keyStates.keySet();
		keyStates.put(key, value);
		
	}
	
	/**
	 * Could make it public, for single calls with setKeyUp = false, but not until that is required.
	 * 
	 * @param key
	 * @param setKeyUp
	 * @return
	 */
	private static boolean isKeyDown(int key, boolean setKeyUp) {
		
		try {
			
			boolean value = keyStates.get(key);
			
			if(setKeyUp) {
				setKeyDown(key, false);
			}
			
			return value;
			
		} catch(Exception ex) {
			return false;
		}
		
	}
	
	/**
	 * Defaults to setting key up after being requested.
	 * 
	 * @param key
	 * @return
	 */
	public static boolean isKeyDown(int key) {
		return isKeyDown(key, setKeyBackUp);
	}
	
	/**
	 * Returns an integers representing which keys are currently down; makes them easy to iterate over.
	 * Also 'consumes' depending on whether or not {@code setKeyBackUp} is on or not.
	 * 
	 * @return
	 */
	public static int[] getPressedKeys() {
		
		Iterator<Entry<Integer, Boolean>> it = keyStates.entrySet().iterator();
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		while(it.hasNext()) {
			
			Entry<Integer, Boolean> curEntry = it.next();
			
			if(curEntry.getValue()) {// For the non-null, but false values.
				list.add(curEntry.getKey());
				
				if(setKeyBackUp) {
					keyStates.put(curEntry.getKey(), false);
				}
				
			}
			
			it.remove();
		}
		
		int[] values = new int[list.size()];
		
		for(int i = 0; i < values.length; i++) {
			values[i] = list.get(i);
		}
		
		return values;
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
				Logger.log(Logger.SEVERE, "Got array idnex out of bounds while trying to get which keys are typed. Index: " + i);
			}
			
		}
		
		if(setKeyBackUp) {
			KeyboardInput.clearTypedKeysBuffer();
		}
		
		return re;
		
	}
	
	public static void clearTypedKeysBuffer() {
		typedKeys.clear();
	}
	
	/**
	 * Returns an iterable buffer of all the keys pressed down since last time this method was called/keys were updated. 
	 * 
	 * @return
	 */
	@Deprecated
	public static boolean[] getkeyBuffer() {
		
		Set<Integer> states = keyStates.keySet();
		
		Iterator<Integer> it = states.iterator();
		
		int max = 0;
		
		while(it.hasNext()) {
			int i = it.next();
			
			if(i > max) {
				max = i;
			}
			
		}
		
		boolean[] buffer = new boolean[max + 1];
		
		it = states.iterator();
		
		while(it.hasNext()) {
			
			int i = it.next();
			
			if(keyStates.get(i)) {
				buffer[i] = keyStates.get(i);
				
			}
			
			it.remove();
			
		}
		
		return buffer;
		
	}
	
	/**
	 * Sets whether or not calling the isKeyDown() method will "consume" the key or not by setting it as up. Convenience method for
	 * when needing to make multiple isKeyDown() calls; makes it so that the boolean variable doesn't have to be added to the end everytime.
	 * 
	 * @param setKeyBackUp
	 */
	public static void setConsumeAfterRequest(boolean setKeyBackUp) {
		KeyboardInput.setKeyBackUp = setKeyBackUp;
	}
	
}
