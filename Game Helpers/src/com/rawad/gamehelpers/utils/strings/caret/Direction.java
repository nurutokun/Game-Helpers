package com.rawad.gamehelpers.utils.strings.caret;

public enum Direction {
	
	UP(0, -1),
	DOWN(0, 1),
	RIGHT(1, 0),
	LEFT(-1, 0);
	
	private final int dx;
	private final int dy;
	
	private int scaleX;
	private int scaleY;
	
	private Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
		
		scaleX = 1;
		scaleY = 1;
		
	}
	
	public void scaleX(int scale) {
		scaleX = scale;
	}
	
	public void scaleY(int scale) {
		scaleY = scale;
	}
	
	public int getDX() {
		return dx * scaleX;
	}
	
	public int getDY() {
		return dy * scaleY;
	}
	
}
