package com.rawad.gamehelpers.display;

import com.rawad.gamehelpers.gamemanager.Game;

public abstract class DisplayMode {
	
	protected Game game;
	
	public DisplayMode() {
		
	}
	
	public void create(Game game) {
		this.game = game;
		
	}
	
	public abstract void destroy();
	
	public abstract void show();
	
}
