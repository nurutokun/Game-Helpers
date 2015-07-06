package com.rawad.gamehelpers.input;

public class MouseEvent {
	
	private int x;
	private int y;
	
	private int button;
	
	private boolean mouseButtonDown;
	private boolean consumed;
	
	public MouseEvent(int x, int y, int button) {
		
		this.x = x;
		this.y = y;
		
		this.button = button;
		
		consumed = false;
		
		this.mouseButtonDown = MouseInput.isButtonDown(button);
		
	}
	
	public MouseEvent(int x, int y) {
		this(x, y, MouseInput.LEFT_MOUSE_BUTTON);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}
	
	public int getButton() {
		return button;
	}
	
	public boolean isButtonDown() {
		return mouseButtonDown;
	}
	
	/**
	 * Mainly to be used by a gui component to indicate that it has "control" over the mouse input, in case it is overlaying another component
	 * or in case that kind of behaviour is required.
	 */
	public void consume() {
		consumed = true;
	}
	
	public boolean isConsumed() {
		return consumed;
	}
	
}
