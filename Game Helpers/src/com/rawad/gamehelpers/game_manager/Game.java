package com.rawad.gamehelpers.game_manager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.game_states.StateManager;

public abstract class Game {
	
	protected StateManager sm;
	
	protected boolean debug;
	
	public Game() {
		
	}
	
	public void init() {
		sm = new StateManager();
		
		debug = false;
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	public abstract BufferedImage getIcon();
	
	public boolean isDebug() {
		return debug;
	}
	
}
