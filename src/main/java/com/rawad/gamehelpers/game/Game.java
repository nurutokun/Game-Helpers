package com.rawad.gamehelpers.game;

import com.rawad.gamehelpers.utils.ClassMap;

public abstract class Game {
	
	protected ClassMap<Proxy> proxies = new ClassMap<Proxy>();
	
	protected GameEngine gameEngine;
	
	protected boolean debug;
	
	protected boolean running = false;
	protected boolean paused = false;
	
	protected void init() {
		
		gameEngine = new GameEngine();
		
		for(Proxy proxy: proxies.values()) {
			proxy.preInit(this);
		}
		
		for(Proxy proxy: proxies.values()) {
			proxy.init();
		}
		
	}
	
	protected void terminate() {
		
		for(Proxy proxy: proxies.values()) {
			proxy.terminate();
		}
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract String getName();
	
	/**
	 * @param gameEngine the gameEngine to set
	 */
	public void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
	public GameEngine getGameEngine() {
		return gameEngine;
	}
	
	public ClassMap<Proxy> getProxies() {
		return proxies;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * @param running the running to set
	 */
	protected void setRunning(boolean running) {
		this.running = running;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void requestStop() {
		running = false;
	}
	
}
