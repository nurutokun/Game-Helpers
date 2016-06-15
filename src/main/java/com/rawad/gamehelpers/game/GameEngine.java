package com.rawad.gamehelpers.game;

import java.util.ArrayList;
import java.util.HashMap;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.Util;

import javafx.collections.ObservableList;

public class GameEngine {
	
	protected final HashMap<Class<? extends GameSystem>, GameSystem> gameSystemsMap = new HashMap
			<Class<? extends GameSystem>, GameSystem>();
	protected final ArrayList<GameSystem> gameSystems = new ArrayList<GameSystem>();
	
	public void tick(ObservableList<Entity> entities) {
		
		for(GameSystem system: gameSystems) {
			
			system.getCompatibleEntities().clear();
			
			ArrayList<Class<? extends Component>> compatibleComponentTypes = system.getCompatibleComponentTypes();
			
			ArrayList<Entity> compatibleEntities = new ArrayList<Entity>();
			
			for(Entity entity: entities) {
				
				if(Entity.contains(entity, compatibleComponentTypes)) compatibleEntities.add(entity);
				
			}
			
			system.getCompatibleEntities().addAll(compatibleEntities);
			
		}
		
		// Separate from calculating entities for system-system communication.
		for(GameSystem gameSystem: gameSystems) {
			gameSystem.tick();
		}
		
	}
	
	/**
	 * {@param gameSystem} order maintained when updated in the {@code tick()} method.
	 * 
	 * @param gameSystem
	 */
	public void addGameSystem(GameSystem gameSystem) {
		
		gameSystemsMap.put(gameSystem.getClass(), gameSystem);
		gameSystems.add(gameSystem);
		
		gameSystem.setGameEngine(this);
		
	}
	
	public void setGameSystems(ArrayList<GameSystem> gameSystems) {
		this.gameSystems.clear();
		gameSystemsMap.clear();
		
		for(GameSystem gameSystem: gameSystems) {
			addGameSystem(gameSystem);
		}
		
	}
	
	public <T extends GameSystem> T getGameSystem(Class<T> gameSystemKey) {
		return Util.cast(gameSystemsMap.get(gameSystemKey));
	}
	
}
