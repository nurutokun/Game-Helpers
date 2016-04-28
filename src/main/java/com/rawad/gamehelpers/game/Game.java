package com.rawad.gamehelpers.game;

import java.util.HashMap;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected Proxy clientOrServer;
	
	protected HashMap<Class<? extends FileParser>, FileParser> fileParsers;
	protected HashMap<Class<? extends Loader>, Loader> loaders;
	
	protected GameHelpersLoader gameHelpersLoader;
	
	protected SimpleBooleanProperty debug;
	
	/** Can be stopped by setting to null. */
	private Background background;
	
	/** Time a single tick lasts in milliseconds. */
	private long tickTime;
	private long totalTime;
	private long remainingTime;
	
	private boolean running;
	private boolean stopRequested;
	
	public Game() {
		
		tickTime = 50;
		
		stopRequested = false;
		
		fileParsers = new HashMap<Class<? extends FileParser>, FileParser>();
		loaders = new HashMap<Class<? extends Loader>, Loader>();
		
		gameHelpersLoader = new GameHelpersLoader();// Loaders moved to constructor from init() method for icon loading.
		
		loaders.put(GameHelpersLoader.class, gameHelpersLoader);
		
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
		
		running = true;
		
	}
	
	public final void update(long timePassed) {
		
		if(background != null) {
			background.update(timePassed);
		}
		
		totalTime = timePassed + remainingTime;
		
		while(totalTime >= tickTime) {// Works better than for-loop; other one keeps tickTime and doesn't make totalTime 0.
			
			totalTime -= tickTime;
			
			clientOrServer.tick();
			
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
	
	/**
	 * Can be optionally overriden.
	 * 
	 * @return
	 */
	public String getSettingsFileName() {
		return "settings";
	}
	
	public void setBackground(Background background) {
		this.background = background;
	}
	
	public Background getBackground() {
		return background;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void requestStop() {
		stopRequested = true;
	}
	
}
