package com.rawad.gamehelpers.display;

import java.awt.Component;

import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.renderengine.MasterRender;

public abstract class DisplayMode {
	
	protected EventHandler l;
	
	protected MasterRender render;
	
	public DisplayMode() {
		l = new EventHandler();
	}
	
	public void create(MasterRender render) {
		this.render = render;
	}
	
	public abstract void destroy();
	
	/**
	 * Draws buffer from current {@code render} instance onto screen.
	 * 
	 */
	public abstract void repaint();
	
	/**
	 * Just so that the {@code Robot} in the {@code MouseInut} class has a position to set relative to.
	 * 
	 * @return
	 */
	public abstract Component getCurrentWindow();
	
}
