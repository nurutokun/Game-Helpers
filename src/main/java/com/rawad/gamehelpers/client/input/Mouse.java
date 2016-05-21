package com.rawad.gamehelpers.client.input;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import com.rawad.gamehelpers.log.Logger;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.layout.Region;

public final class Mouse {
	
	private static Robot bot;
	
	private static Cursor cursor;
	
	private static Region region;
	
	private static Point mouseLocation;
	
	private static double x;
	private static double y;
	
	private static double clampX;
	private static double clampY;
	
	private static double dx;
	private static double dy;
	
	private static boolean clamped;
	
	private Mouse() {}
	
	static {
		
		try {
			
			bot = new Robot();
			
		} catch(AWTException ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; Robot wasn't initialized");
		}
		
	}
	
	public static void update(Parent parent) {
		
		mouseLocation = MouseInfo.getPointerInfo().getLocation();
		
		x = mouseLocation.getX();
		y = mouseLocation.getY();
		
		Region region = null;
		
		try {
			region = (Region) parent;
		} catch(NullPointerException | ClassCastException ex) {
			Logger.log(Logger.DEBUG, "Parent isn't a region.");
			ex.printStackTrace();
		}
		
		if(region != null) {
			
			Mouse.region = region;
			
			Point2D relativeMouseLocation = region.screenToLocal(mouseLocation.getX(), mouseLocation.getY());
			
			x = relativeMouseLocation.getX();
			y = relativeMouseLocation.getY();
			
		}
		
		if(clamped && bot != null) {
			
			clampX = region.getWidth() / 2d;
			clampY = region.getHeight() / 2d;
			
			dx = x - clampX;
			dy = y - clampY;
			
			Point2D pos = region.localToScreen(clampX, clampY);
			
			bot.mouseMove((int) (pos.getX()), (int) (pos.getY()));
			
		}
	}
	
	public static void clamp() {
		
		Mouse.clamped = true;
		
		Mouse.clampX = region.getWidth() / 2d;
		Mouse.clampY = region.getHeight() / 2d;
		
		setCursor(Cursors.BLANK);
		
	}
	
	public static void unclamp() {
		
		Mouse.clamped = false;
		
		setCursor(Cursors.DEFAULT);
		
	}
	
	public static double getX() {
		return x;
	}
	
	public static double getY() {
		return y;
	}
	
	public static double getDx() {
		return dx;
	}
	
	public static double getDy() {
		return dy;
	}
	
	public static double getClampX() {
		return clampX;
	}
	
	public static double getClampY() {
		return clampY;
	}
	
	public static boolean isClamped() {
		return clamped;
	}
	
	public static void setCursor(Cursors cursor) {
		Mouse.cursor = cursor.getValue();
		
		if(region != null) region.setCursor(getCursor());
		
	}
	
	public static Cursor getCursor() {
		return Mouse.cursor;
	}
	
}
