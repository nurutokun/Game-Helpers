package com.rawad.gamehelpers.game;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gamestates.StateManager;

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
	
	public boolean isDebug() {
		return debug;
	}
	
}
