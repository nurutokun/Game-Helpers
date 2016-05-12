package com.rawad.gamehelpers.geometry;

import javafx.beans.property.SimpleDoubleProperty;

public class Rectangle extends Shape {
	
	private SimpleDoubleProperty x;
	private SimpleDoubleProperty y;
	
	private SimpleDoubleProperty width;
	private SimpleDoubleProperty height;
	
	public Rectangle(double x, double y, double width, double height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	@Override
	public boolean contains(double x, double y) {
		if(x >= getX() && x < getX() + getWidth() && y >= getY() && y < getY() + getHeight()) return true;
		return false;
	}
	
	public SimpleDoubleProperty xProperty() {
		if(x == null) x = new SimpleDoubleProperty();
		return x;
	}
	
	public void setX(double x) {
		xProperty().set(x);
	}
	
	public double getX() {
		return xProperty().get();
	}
	
	public SimpleDoubleProperty yProperty() {
		if(y == null) y = new SimpleDoubleProperty();
		return y;
	}
	
	public void setY(double y) {
		yProperty().set(y);
	}
	
	public double getY() {
		return yProperty().get();
	}
	
	public SimpleDoubleProperty widthProperty() {
		if(width == null) width = new SimpleDoubleProperty();
		return width;
	}
	
	public void setWidth(double width) {
		widthProperty().set(width);
	}
	
	public double getWidth() {
		return widthProperty().get();
	}
	
	public SimpleDoubleProperty heightProperty() {
		if(height == null) height = new SimpleDoubleProperty();
		return height;
	}
	
	public void setHeight(double height) {
		heightProperty().set(height);
	}
	
	public double getHeight() {
		return heightProperty().get();
	}
	
}
