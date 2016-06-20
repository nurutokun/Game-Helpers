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
	
	/**
	 * Provides an extra layer of control for initializing, especially useful now that there is support for multiple
	 * {@code Proxy} instances.
	 * 
	 * @param game
	 */
	public void preInit(Game game) {
		this.game = game;		
	}
	
	public void init(Game game) {
		
		readyToUpdate = false;
		
	}
	
	public abstract void tick();
	
	public abstract void stop();
	
	public Game getGame() {
		return game;
	}
	
}
