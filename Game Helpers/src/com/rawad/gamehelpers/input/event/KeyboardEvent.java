package com.rawad.gamehelpers.input.event;

import com.rawad.gamehelpers.input.KeyboardInput;

public class KeyboardEvent extends InputEvent {
	
	private String[] typedKeys;
	
	public KeyboardEvent(String[] typedKeys) {
		super();
		
		this.typedKeys = typedKeys;
		
	}
	
	public KeyboardEvent() {
		this(KeyboardInput.getTypedKeys());
	}
	
	public String[] getTypedKeys() {
		return typedKeys;
	}
	
}
