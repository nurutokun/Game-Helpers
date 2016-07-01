package com.rawad.gamehelpers.game;

import java.util.HashMap;

import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Blueprint;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ALoader;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;

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
		
		proxies = new ClassMap<Proxy>(true);
		
	}
	
	protected void init() {
		
		gameEngine = new GameEngine();
		
		world = new World();
		
		stopRequested = false;
		
		running = true;
		
		ALoader.addTask(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				Logger.log(Logger.DEBUG, "Loading entity blueprints...");
				
				try {
					
					EntityBlueprintLoadObject entityLoadObject = geEntityBlueprintLoadObject();
					
					for(Object entityKey: entityLoadObject.getEntityBindings().keySet()) {
						
						String entityName = entityLoadObject.getEntityBindings().get(entityKey);
						
						BlueprintManager.addBlueprint(entityKey, new Blueprint(EntityFileParser.parseEntityFile(
								entityLoadObject.getEntityFileContext(), entityName, entityLoadObject
								.getContextPaths())));
						
					}
					
					Logger.log(Logger.DEBUG, "Loaded all entity blueprints.");
					
				} catch(Exception ex) {
					Logger.log(Logger.WARNING, "Entity blueprint loading failed");
					ex.printStackTrace();
				}
				
				return null;
				
			}
		});
		
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
	
	protected abstract EntityBlueprintLoadObject geEntityBlueprintLoadObject();
	
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
	
	protected static final class EntityBlueprintLoadObject {
		
		/**
		 * Represents {@code Blueprint} objects bound to specific {@code Entity} names which are used as the file names 
		 * to load these blueprints from.
		 */
		private final HashMap<Object, String> entityBindings;
		private final Class<? extends Object> entityFileContext;
		/** Passed to {@code EntityBlueprintFileParser}. */
		private final String[] contextPaths;
		
		public EntityBlueprintLoadObject(HashMap<Object, String> entityBindings, Class<? extends Object> 
				entityFileContext, String[] contextPaths) {
			super();
			
			this.entityBindings = entityBindings;
			this.entityFileContext = entityFileContext;
			this.contextPaths = contextPaths;
			
		}
		
		public HashMap<Object, String> getEntityBindings() {
			return entityBindings;
		}
		
		public Class<? extends Object> getEntityFileContext() {
			return entityFileContext;
		}
		
		public String[] getContextPaths() {
			return contextPaths;
		}
		
	}
	
}
