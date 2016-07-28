package com.rawad.gamehelpers.game;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.resources.Loader;
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
	protected ClassMap<Loader> loaders;
	
	protected boolean readyToUpdate;
	
	/**
	 * Provides an extra layer of control for initializing, especially useful now that there is support for multiple
	 * {@code Proxy} instances.
	 * 
	 * @param game
	 */
	public void preInit(Game game) {
		this.game = game;
		
		fileParsers = new ClassMap<FileParser>();
		loaders = new ClassMap<Loader>();
		
	}
	
	public void init(Game game) {
		
		readyToUpdate = false;
		
	}
	
	public abstract void tick();
	
	public abstract void stop();
	
	public Game getGame() {
		return game;
	}
	
	public ClassMap<Loader> getLoaders() {
		return loaders;
	}
	
	public ClassMap<FileParser> getFileParsers() {
		return fileParsers;
	}
	
}
