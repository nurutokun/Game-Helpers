package com.rawad.gamehelpers.geometry;

public class Point2d {
	
	private double x;
	private double y;
	
	public Point2d(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Point2d() {
		this(0d, 0d);
	}
	
	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	
}
