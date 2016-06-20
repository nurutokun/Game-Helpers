package com.rawad.gamehelpers.game;

/**
 * Used to define either a client or server.
 * 
 * @author Rawad
 *
 */
public abstract class Proxy {
	
	protected Game game;
	
	protected boolean readyToUpdate;
	
	public void init(Game game) {
		this.game = game;
		
		readyToUpdate = false;
		
	}
	
	public abstract void tick();
	
	public abstract void stop();
	
	public Game getGame() {
		return game;
	}
	
}
