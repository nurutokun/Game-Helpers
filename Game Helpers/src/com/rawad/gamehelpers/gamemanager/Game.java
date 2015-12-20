package com.rawad.gamehelpers.gamemanager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.gamestates.StateManager;
import com.rawad.gamehelpers.renderengine.MasterRender;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected StateManager sm;
	
	protected HashMap<Class<? extends FileParser>, FileParser> fileParsers;
	
	protected HashMap<String, Loader> loaders;
	
	protected MasterRender masterRender;
	
	protected GameHelpersLoader gameHelpersLoader;
	
	protected boolean debug;
	
	public Game() {
		
		fileParsers = new HashMap<Class<? extends FileParser>, FileParser>();
		
		loaders = new HashMap<String, Loader>();
		
		masterRender = new MasterRender();
		
	}
	
	// Should replace the two seperate init methods with just having this class be more neutral and having a client
	// instead of a game manager to hold this and other games and the viewport be exclusively for controlling the player
	/**
	 * For use by client only.
	 * 
	 */
	public void clientInit() {
		init();
		
		sm = new StateManager(this);
		
		debug = false;
		
	}
	
	/**
	 * For use by server only.
	 */
	public void serverInit() {
		init();
	}
	
	protected void init() {
		
		gameHelpersLoader = new GameHelpersLoader();
		
		loaders.put(GameHelpersLoader.BASE, gameHelpersLoader);// Loads things from game helpers folder
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	public abstract BufferedImage getIcon();
	
	public <T extends Loader> T getLoader(String key) {
		return Util.cast(loaders.get(key));
	}
	
	public <T extends FileParser> T getFileParser(Class<T> key) {
		return Util.cast(fileParsers.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It get's it from that "<T extends FileType>"; whatever T extends.
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
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
	
}
