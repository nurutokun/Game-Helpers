package com.rawad.gamehelpers.input.event;

import com.rawad.gamehelpers.input.MouseInput;

public class MouseEvent extends InputEvent {
	
	private int x;
	private int y;
	
	private int button;
	
	private boolean mouseButtonDown;
	
	public MouseEvent(int x, int y, int button) {
		super();
		
		this.x = x;
		this.y = y;
		
		this.button = button;
		
		this.mouseButtonDown = MouseInput.isButtonDown(button);
		
	}
	
	public MouseEvent(int x, int y) {
		this(x, y, MouseInput.LEFT_MOUSE_BUTTON);
	}
	
	public MouseEvent() {
		this(MouseInput.getX(), MouseInput.getY());
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
	
}
