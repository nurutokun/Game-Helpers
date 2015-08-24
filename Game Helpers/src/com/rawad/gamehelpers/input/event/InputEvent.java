package com.rawad.gamehelpers.input.event;


public abstract class InputEvent {
	
	private boolean consumed;
	
	public InputEvent() {
		
		consumed = false;
		
	}
	
	public void consume() {
		consumed = true;
	}
	
	public boolean isConsumed() {
		return consumed;
	}

}
