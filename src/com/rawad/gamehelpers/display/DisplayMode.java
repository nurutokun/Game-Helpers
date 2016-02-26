package com.rawad.gamehelpers.display;

import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.game.Game;

public abstract class DisplayMode {
	
	protected Game game;
	
	public DisplayMode() {
		
	}
	
	public void create(Game game) {
		this.game = game;
		
	}
	
	public abstract void destroy();
	
	public abstract void show();
	
	/**
	 * Because of the way textures are rendered, the game icon can't be displayed right away.
	 * 
	 * @param icon
	 */
	public abstract void setIcon(BufferedImage icon);
	
}
