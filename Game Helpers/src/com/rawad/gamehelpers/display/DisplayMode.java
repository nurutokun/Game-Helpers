package com.rawad.gamehelpers.display;

import java.awt.Component;

import com.rawad.gamehelpers.game_manager.Game;
import com.rawad.gamehelpers.input.EventHandler;

public abstract class DisplayMode {
	
	protected EventHandler l;
	
	protected Game game;
	
	public DisplayMode() {
		l = new EventHandler();
	}
	
	public void create(Game game) {
		this.game = game;
	}
	
	public abstract void destroy();
	
	public abstract void repaint();
	
	/**
	 * Just so that the {@code Robot} in the {@code MouseInut} class has a position to set relative to.
	 * 
	 * @return
	 */
	public abstract Component getCurrentWindow();
	
}
