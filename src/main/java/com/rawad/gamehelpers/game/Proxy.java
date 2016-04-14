package com.rawad.gamehelpers.game;

/**
 * Used to define either client or server.
 * 
 * @author Rawad
 *
 */
public interface Proxy {
	
	public abstract void init(Game game);
	
	public abstract void tick();
	
	public abstract void stop();
	
	public IController getController();
	
}
