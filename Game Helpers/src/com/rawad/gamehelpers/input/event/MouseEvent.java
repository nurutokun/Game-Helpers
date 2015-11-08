package com.rawad.gamehelpers.input.event;

import com.rawad.gamehelpers.input.MouseInput;

public class MouseEvent extends InputEvent {
	
	private int x;
	private int y;
	
	private boolean leftMouseButtonDown;
	private boolean rightMouseButtonDown;
	
	public MouseEvent(int x, int y, boolean leftMouseButtonDown, boolean rightMouseButtonDown) {
		super();
		
		this.x = x;
		this.y = y;
		
		this.leftMouseButtonDown = leftMouseButtonDown;
		this.rightMouseButtonDown = rightMouseButtonDown;
		
	}
	
	public MouseEvent(int x, int y) {
		this(x, y, MouseInput.isButtonDown(MouseInput.LEFT_MOUSE_BUTTON), 
				MouseInput.isButtonDown(MouseInput.RIGHT_MOUSE_BUTTON));
		
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
	
	public boolean isLeftButtonDown() {
		return !isConsumed() && leftMouseButtonDown;
	}
	
	public boolean isRightButtonDown() {
		return !isConsumed() && rightMouseButtonDown;
	}
	
}
