package com.rawad.gamehelpers.game;

import com.rawad.gamehelpers.utils.ClassMap;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected ClassMap<Proxy> proxies;
	
	protected GameEngine gameEngine;
	
	protected World world;
	
	protected SimpleBooleanProperty debug;
	
	/** Time a single tick lasts in milliseconds. */
	private long tickTime;
	private long totalTime;
	private long remainingTime;
	
	private boolean running;
	private boolean paused;
	private boolean stopRequested;
	
	public Game() {
		
		tickTime = 50;
		
		proxies = new ClassMap<Proxy>();
		
	}
	
	protected void init() {
		
		gameEngine = new GameEngine();
		
		world = new World();
		
		stopRequested = false;
		
		running = true;
		
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
			
			if(!isPaused()) {
				synchronized(world.getEntities()) {
					gameEngine.tick(world.getEntities());// Populates GameSystem objects with entities to work with.
				}
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
			
			running = false;
			stopRequested = false;
			
		}
		
	}
	
	public abstract String getName();
	
	public GameEngine getGameEngine() {
		return gameEngine;
	}
	
	public void setWorld(World world) {
		if(world == null) return;
		
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}
	
	public ClassMap<Proxy> getProxies() {
		return proxies;
	}
	
	public SimpleBooleanProperty debugProperty() {
		if(debug == null) debug = new SimpleBooleanProperty(false);
		return debug;
	}
	
	public void setDebug(boolean debug) {
		debugProperty().set(debug);
	}
	
	public boolean isDebug() {
		return debugProperty().get();
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
