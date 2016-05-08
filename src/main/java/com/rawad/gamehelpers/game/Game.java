package com.rawad.gamehelpers.game;

import java.util.HashMap;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected Proxy clientOrServer;
	
	protected GameEngine gameEngine;
	
	protected World world;
	
	protected HashMap<Class<? extends FileParser>, FileParser> fileParsers;
	protected HashMap<Class<? extends Loader>, Loader> loaders;
	
	protected SimpleBooleanProperty debug;
	
	/** Time a single tick lasts in milliseconds. */
	private long tickTime;
	private long totalTime;
	private long remainingTime;
	
	private boolean running;
	private boolean stopRequested;
	
	public Game() {
		
		tickTime = 50;
		
		fileParsers = new HashMap<Class<? extends FileParser>, FileParser>();
		loaders = new HashMap<Class<? extends Loader>, Loader>();
		
		debug = new SimpleBooleanProperty(false);
		
	}
	
	/**
	 * <del>
	 * <b>Note:</b> it is up to the inheriting class to initialize the proxy.
	 * clientOrServer
	 * </del>
	 * 
	 * - Not anymore it isn't...
	 * 
	 */
	protected void init() {
		
		gameEngine = new GameEngine();
		
		world = new World();
		
		loaders.put(GameHelpersLoader.class, new GameHelpersLoader());
		
		stopRequested = false;
		
		running = true;
		
	}
	
	public final void update(long timePassed) {
		
		totalTime = timePassed + remainingTime;
		
		while(totalTime >= tickTime) {
			
			totalTime -= tickTime;
			
			gameEngine.tick(world.getEntitiesAsList());// Populates GameSystem objects with entities to work with.
			
			clientOrServer.tick();// Able to get info from game systems (e.g. for rendering).
			
		}
		
		remainingTime = totalTime;
		
		if(stopRequested) {
			
			clientOrServer.stop();
			
			running = false;
			stopRequested = false;
			
		}
		
	}
	
	public abstract int getIconLocation();
	
	public abstract void registerTextures();
	
	public GameEngine getGameEngine() {
		return gameEngine;
	}
	
	public void setProxy(Proxy clientOrServer) {
		this.clientOrServer = clientOrServer;
	}
	
	public Proxy getProxy() {
		return clientOrServer;
	}
	
	public <T extends Loader> T getLoader(Class<T> key) {
		return Util.cast(loaders.get(key));
	}
	
	public <T extends FileParser> T getFileParser(Class<T> key) {
		return Util.cast(fileParsers.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It get's it from that "<T extends FileType>"; whatever T extends.
	}
	
	public SimpleBooleanProperty debugProperty() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug.set(debug);
	}
	
	public boolean isDebug() {
		return debug.get();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void requestStop() {
		stopRequested = true;
	}
	
}
