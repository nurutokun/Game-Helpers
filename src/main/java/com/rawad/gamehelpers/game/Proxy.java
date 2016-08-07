package com.rawad.gamehelpers.game;

import java.lang.Thread.UncaughtExceptionHandler;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ALoader;
import com.rawad.gamehelpers.utils.ClassMap;

/**
 * Used to define either a client or server.
 * 
 * @author Rawad
 *
 */
public abstract class Proxy {
	
	protected Game game;
	
	protected ClassMap<FileParser> fileParsers;
	protected ClassMap<ALoader> loaders;
	
	protected boolean update;
	
	/**
	 * Provides an extra layer of control for initializing, especially useful now that there is support for multiple
	 * {@code Proxy} instances.
	 * 
	 * @param game
	 */
	public void preInit(Game game) {
		this.game = game;
		
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable ex) {
				Logger.log(Logger.SEVERE, "Uncaught Exception.");
				ex.printStackTrace();
			}
		});
		
		fileParsers = new ClassMap<FileParser>();
		loaders = new ClassMap<ALoader>();
		// Add default entity file parser here.
		
		fileParsers.put(new EntityFileParser());
		
		update = false;
		
	}
	
	public abstract void init();
	
	public abstract void tick();
	
	public abstract void stop();
	
	public Game getGame() {
		return game;
	}
	
	public ClassMap<ALoader> getLoaders() {
		return loaders;
	}
	
	public ClassMap<FileParser> getFileParsers() {
		return fileParsers;
	}
	
	public boolean shouldUpdate() {
		return update;
	}
	
}
