package com.rawad.gamehelpers.geometry;

public abstract class Shape {
	
	public boolean contains(Point2d point) {
		return this.contains(point.getX(), point.getY());
	}
	
	public abstract boolean contains(double x, double y);
	
	// Could store all the vertices of this shape for the intersects method.
	
}
