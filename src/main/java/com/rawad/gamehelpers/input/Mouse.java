package com.rawad.gamehelpers.input;

import java.awt.AWTException;
import java.awt.Robot;

import com.rawad.gamehelpers.log.Logger;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

public class Mouse {
	
	private static Robot bot;
	
	private static Cursor cursor;
	
	private static Region region;
	
	private static double clampX;
	private static double clampY;
	
	private static double dx;
	private static double dy;
	
	private static boolean clamped;
	
	private static final EventHandler<MouseEvent> CLAMPER_HANDLER;
	
	static {
		
		try {
			
			bot = new Robot();
			
		} catch(AWTException ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; Robot wasn't initialized");
		}
		
		CLAMPER_HANDLER = mouseEvent -> {// TODO: Add an update method to make this here smoother + more responsive.
			
			if(clamped && bot != null) {
				
				clampX = region.getWidth() / 2d;
				clampY = region.getHeight() / 2d;
				
				dx = mouseEvent.getX() - clampX;
				dy = mouseEvent.getY() - clampY;
				
				Point2D pos = region.localToScreen(clampX, clampY);
				
				bot.mouseMove((int) (pos.getX()), (int) (pos.getY()));
				
				setCursor(Cursors.BLANK);
				
			} else {
				setCursor(Cursors.DEFAULT);
			}
			
			region.setCursor(cursor);
			
		};
		
	}
	
	public static void clamp(Region region) {
		
		Mouse.clamped = true;
		
		Mouse.clampX = region.getWidth() / 2d;
		Mouse.clampY = region.getHeight() / 2d;
		
		Mouse.region = region;
		
		Mouse.region.addEventHandler(MouseEvent.MOUSE_MOVED, CLAMPER_HANDLER);
		Mouse.region.addEventHandler(MouseEvent.MOUSE_DRAGGED, CLAMPER_HANDLER);
		
	}
	
	public static void unclamp() {
		
		Mouse.clamped = false;
		
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
	}
	
	public static Cursor getCursor() {
		return Mouse.cursor;
	}
	
}
