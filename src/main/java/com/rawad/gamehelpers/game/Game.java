package com.rawad.gamehelpers.game;

import com.rawad.gamehelpers.utils.ClassMap;

public abstract class Game {
	
	protected ClassMap<Proxy> proxies = new ClassMap<Proxy>();
	
	protected GameEngine gameEngine;
	
	protected boolean debug;
	
	/** Time a single tick lasts in milliseconds. */
	protected long tickTime = 50;
	
	private long totalTime;
	private long remainingTime;
	
	private boolean running;
	private boolean paused;
	private boolean stopRequested;
	
	protected void init() {
		
		gameEngine = new GameEngine();
		
		stopRequested = false;
		
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
	
	public final void update(long timePassed) {
		
		totalTime = timePassed + remainingTime;
		
		while(totalTime >= tickTime) {
			
			totalTime -= tickTime;
			
			if(!isPaused() && gameEngine != null) {
				gameEngine.tick();// Populates GameSystem objects with entities to work with.
			}
			
			for(Proxy proxy: proxies.values()) {
				if(proxy.shouldUpdate()) proxy.tick();
			}
			
		}
		
		remainingTime = totalTime;
		
		if(stopRequested) {
			
			for(Proxy proxy: proxies.values()) {
				proxy.terminate();
			}
			
			setRunning(false);
			
			stopRequested = false;
			
		}
		
	}
	
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
	
	private boolean isPaused() {
		return paused;
	}
	
	public void requestStop() {
		stopRequested = true;
	}
	
}
