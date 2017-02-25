package com.rawad.gamehelpers.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.Listener;
import com.rawad.gamehelpers.utils.ClassMap;

public class GameEngine {
	
	protected final ClassMap<GameSystem> gameSystems = new ClassMap<GameSystem>();
	
	private final Map<Object, ArrayList<Listener>> listeners = new HashMap<Object, ArrayList<Listener>>();
	
	private final List<Event> eventQueue = new ArrayList<Event>();
	
	public void tick(List<Entity> entities) {
		
		for(GameSystem gameSystem: gameSystems.values()) {
			
			gameSystem.getCompatibleEntities().clear();
			
			ArrayList<Class<? extends Component>> compatibleComponentTypes = gameSystem.getCompatibleComponentTypes();
			
			ArrayList<Entity> compatibleEntities = new ArrayList<Entity>();
			
			for(Entity entity: entities) {
				
				if(Entity.contains(entity, compatibleComponentTypes)) compatibleEntities.add(entity);
				
			}
			
			gameSystem.getCompatibleEntities().addAll(compatibleEntities);
			
		}
		
		// Separate from calculating entities for system-system communication.
		for(GameSystem gameSystem: gameSystems.values()) {
			gameSystem.tick();
		}
		
		for(Event e: eventQueue) {
			processEvent(e);
		}
		
		eventQueue.clear();
		
	}
	
	public void setGameSystems(ClassMap<GameSystem> gameSystems) {
		
		this.gameSystems.clear();
		
		for(GameSystem gameSystem: gameSystems.values()) {
			gameSystem.setGameEngine(this);
			this.gameSystems.put(gameSystem);
		}
		
	}
	
	public void registerListener(Object eventType, Listener l) {
		
		ArrayList<Listener> compListeners = listeners.get(eventType);
		
		if(compListeners == null) {
			compListeners = new ArrayList<Listener>();
			listeners.put(eventType, compListeners);
		}
		
		compListeners.add(l);
		
	}
	
	/**
	 * Queues an {@code Event} for processing at the end of this tick.
	 * 
	 * @param e
	 */
	public void queueEvent(Event e) {
		eventQueue.add(e);
	}
	
	/**
	 * Submits an {@code Event} for immediate processing.
	 * 
	 * @param e
	 */
	public void submitEvent(Event e) {
		this.processEvent(e);
	}
	
	private void processEvent(Event e) {
		
		if(listeners.containsKey(e.getEventType())) {
			for(Listener l: listeners.get(e.getEventType())) {
				if(l != null) l.onEvent(e);
			}
		}
		
	}
	
	public ClassMap<GameSystem> getGameSystems() {
		return gameSystems;
	}
	
}
