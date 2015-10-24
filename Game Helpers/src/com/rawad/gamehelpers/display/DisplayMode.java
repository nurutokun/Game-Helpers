package com.rawad.gamehelpers.display;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.input.EventHandler;

public abstract class DisplayMode {
	
	protected EventHandler l;
	
	protected Game game;
	
	protected Graphics2D g;
	
	protected BufferedImage buffer;
	
	public DisplayMode() {
		l = new EventHandler();
	}
	
	public void create(Game game) {
		this.game = game;
	}
	
	public abstract void destroy();
	
	/**
	 * Should draw buffer of {@code Graphics} context onto screen.
	 * 
	 */
	public abstract void repaint();
	
	/**
	 * Used for updating the current {@code Graphics2D} context for the game to render on. Also, more compactly update 
	 * {@code Game} object from here.
	 * 
	 */
	public void update(long timePassed) {
		game.update(timePassed);
	}
	
	/**
	 * Just so that the {@code Robot} in the {@code MouseInut} class has a position to set relative to.
	 * 
	 * @return
	 */
	public abstract Component getCurrentWindow();
	
}
