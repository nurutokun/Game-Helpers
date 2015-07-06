package com.rawad.gamehelpers.display;

import java.awt.Component;

import com.rawad.gamehelpers.input.EventHandler;

public abstract class DisplayMode {
	
	protected EventHandler l;
	
	public DisplayMode() {
		l = new EventHandler();
	}
	
	public abstract void create();
	
	public abstract void destroy();
	
	public abstract void repaint();
	
	/**
	 * Just so that the {@code Robot} in the {@code MouseInut} class has a position to set relative to.
	 * 
	 * @return
	 */
	public abstract Component getCurrentWindow();
	
}
