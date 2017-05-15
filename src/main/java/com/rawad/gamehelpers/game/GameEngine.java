package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.gamehelpers.utils.ClassMap;

public class GameEngine {
	
	protected final ClassMap<GameSystem> gameSystems = new ClassMap<GameSystem>();
	
	private final EventManager eventManager = new EventManager();
	
	private final ArrayList<Entity> entities = new ArrayList<Entity>();
	
	public void tick() {
		
		synchronized(entities) {
			
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
			
			eventManager.processQueuedEvents();
			
		}
		
	}
	
	public void addGameSystem(GameSystem gameSystem) {
		gameSystems.put(gameSystem);
	}
	
	public <T extends GameSystem> T getGameSystem(Class<T> clazz) {
		return gameSystems.get(clazz);
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	/**
	 * @return the entities
	 */
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
}
