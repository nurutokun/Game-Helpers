package com.rawad.gamehelpers.gamemanager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.rawad.gamehelpers.files.FileParser;
import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.gamestates.StateManager;
import com.rawad.gamehelpers.renderengine.MasterRender;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected StateManager sm;
	
	protected FileParser fileParser;
	protected HashMap<Class<? extends FileType>, FileType> files;
	
	protected MasterRender masterRender;
	
	protected boolean debug;
	
	public Game() {
		
		fileParser = new FileParser();
		files = new HashMap<Class<? extends FileType>, FileType>();
		
		masterRender = new MasterRender();
		
	}
	
	/**
	 * For use by client only.
	 * 
	 */
	public void init() {
		
		sm = new StateManager(this);
		
		debug = false;
		
	}
	
	public abstract void update(long timePassed);
	
	public abstract void render(Graphics2D g);
	
	public abstract BufferedImage getIcon();
	
	public FileParser getFileParser() {
		return fileParser;
	}
	
	public HashMap<Class<? extends FileType>, ? extends FileType> getFiles() {
		return files;
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
