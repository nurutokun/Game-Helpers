package com.rawad.gamehelpers.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

public class KeyboardInput {
	
	private static HashMap<Integer, Boolean> keyStates;
	
	private static boolean setKeyBackUp;
	
	static {
		
		keyStates = new HashMap<Integer, Boolean>();
		
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
	 * Returns an iterable set of {@code Entry} objects that can be efficiently iterated over to check for all the keys that are down.
	 * 
	 * @return
	 */
	public static int[] getPressedKeys() {
		
		Iterator<Entry<Integer, Boolean>> it = keyStates.entrySet().iterator();
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		while(it.hasNext()) {
			
			list.add(it.next().getKey());
			
			it.remove();
		}
		
		int[] values = new int[list.size()];
		
		for(int i = 0; i < values.length; i++) {
			values[i] = list.get(i);
		}
		
		return values;
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
