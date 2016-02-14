package com.rawad.gamehelpers.game;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	/** Time a single tick lasts in milliseconds. */
	private long tickTime;
	private long totalTime;
	private long remainingTime;
	
	protected Proxy clientOrServer;
	
	protected HashMap<Class<? extends FileParser>, FileParser> fileParsers;
	
	protected HashMap<Class<? extends Loader>, Loader> loaders;
	
	protected GameHelpersLoader gameHelpersLoader;
	
	/** Can be stopped by setting to null. */
	protected Background background;
	
	protected boolean debug;
	
	private boolean running;
	private boolean stopRequested;
	
	public Game() {
		
		tickTime = 200;
		
		stopRequested = false;
		
		fileParsers = new HashMap<Class<? extends FileParser>, FileParser>();
		
		loaders = new HashMap<Class<? extends Loader>, Loader>();
		
	}
	
	public void initGUI() {
		
		clientOrServer.initGUI();
		
	}
	
	/**
	 * <b>Note:</b> it is up to the inheriting class to initialize the proxy.
	 * 
	 * @param clientOrServer
	 */
	protected void init(Proxy clientOrServer) {
		
		running = true;
		
		this.clientOrServer = clientOrServer;
		
		debug = false;
		
		gameHelpersLoader = new GameHelpersLoader();
		
		loaders.put(GameHelpersLoader.class, gameHelpersLoader);// Loads things from game helpers folder
		
		background = Background.instance();
		
	}
	
	public final void update(long timePassed) {
		
		tickTime = 200;
		
		if(background != null) {
			background.update(timePassed);
		}
		
		totalTime += timePassed + remainingTime;
		
		for(; totalTime >= tickTime; totalTime -= tickTime) {
			
			tick();
			
		}
		
		remainingTime = totalTime;
		
		if(stopRequested) {
			stop();
			
			running = false;
			stopRequested = false;
			
		}
		
	}
	
	protected void tick() {
		clientOrServer.tick();
	}
	
	protected void stop() {
		clientOrServer.stop();
	}
	
	public abstract BufferedImage getIcon();
	
	public Proxy getProxy() {
		return clientOrServer;
	}
	
	public long getTickTime() {
		return tickTime;
	}
	
	public <T extends Loader> T getLoader(Class<T> key) {
		return Util.cast(loaders.get(key));
	}
	
	public <T extends FileParser> T getFileParser(Class<T> key) {
		return Util.cast(fileParsers.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It get's it from that "<T extends FileType>"; whatever T extends.
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Can be optionally overriden.
	 * 
	 * @return
	 */
	public String getSettingsFileName() {
		return "settings";
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
