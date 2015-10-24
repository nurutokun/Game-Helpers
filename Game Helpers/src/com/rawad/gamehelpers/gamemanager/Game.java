package com.rawad.gamehelpers.gamemanager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.rawad.gamehelpers.files.FileParser;
import com.rawad.gamehelpers.files.FileType;
import com.rawad.gamehelpers.gamestates.StateManager;

public abstract class Game {
	
	protected StateManager sm;
	
	protected FileParser fileParser;
	protected HashMap<Class<? extends FileType>, FileType> files;
	
	protected boolean debug;
	
	public Game() {
		
		fileParser = new FileParser();
		files = new HashMap<Class<? extends FileType>, FileType>();
		
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
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
}
