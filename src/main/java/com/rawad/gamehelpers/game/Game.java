package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	protected ClassMap<Proxy> proxies;
	
	protected GameEngine gameEngine;
	
	protected World world;
	
	protected ClassMap<FileParser> fileParsers;
	protected ClassMap<Loader> loaders;
	
	protected Thread loadingThread;
	protected Runnable loadingRunnable;
	
	protected ArrayList<Task<Integer>> tasks = new ArrayList<Task<Integer>>();// Might pose issue upon re-init.
	
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
		
		proxies = new ClassMap<Proxy>(true);
		
		fileParsers = new ClassMap<FileParser>();
		loaders = new ClassMap<Loader>();
		
		debug = new SimpleBooleanProperty(false);
		
	}
	
	protected void init() {
		
		gameEngine = new GameEngine();
		
		world = new World();
		
		loaders.put(new GameHelpersLoader());
		
		stopRequested = false;
		
		running = true;
		
		loadingThread = new Thread(getLoadingRunnable(), "Loading Thread");
		loadingThread.setDaemon(true);
		loadingThread.start();
		
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
			
			for(Proxy proxy: proxies.getOrderedMap()) {
				if(proxy.readyToUpdate) proxy.tick();
			}
			
		}
		
		remainingTime = totalTime;
		
		if(stopRequested) {
			
			for(Proxy proxy: proxies.getOrderedMap()) {
				proxy.stop();
			}
			
			running = false;
			stopRequested = false;
			
		}
		
	}
	
	public abstract int getIconLocation();
	
	public abstract void registerTextures();
	
	public Runnable getLoadingRunnable() {
		
		if(loadingRunnable == null) {
			
			loadingRunnable = () -> {
				
				while(isRunning()) {
					
					synchronized(tasks) {
						if(tasks.size() > 0) {
							Task<Integer> task = tasks.get(0);
							try {
								task.run();
								tasks.remove(0);
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					
				}
			};
			
		}
		
		return loadingRunnable;
		
	}
	
	public void addTask(Task<Integer> taskToLoad) {
		synchronized(tasks) {
			tasks.add(taskToLoad);
		}
	}
	
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
	
	public ClassMap<Loader> getLoaders() {
		return loaders;
	}
	
	public ClassMap<FileParser> getFileParsers() {
		return fileParsers;
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
