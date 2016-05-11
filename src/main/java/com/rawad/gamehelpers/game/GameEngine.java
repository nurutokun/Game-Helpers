package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

public class GameEngine {
	
	protected final ArrayList<GameSystem> gameSystems = new ArrayList<GameSystem>();
	
	public void tick(ArrayList<Entity> entities) {
		
		for(GameSystem system: gameSystems) {
			
			system.getCompatibleEntities().clear();
			
			ArrayList<Class<? extends Component>> compatibleComponentTypes = system.getCompatibleComponentTypes();
			
			ArrayList<Entity> compatibleEntities = new ArrayList<Entity>();
			
			for(Entity entity: entities) {
				
				boolean compatible = true;
				
				for(Class<? extends Component> compatibleComponentType: compatibleComponentTypes) {
					
					if(entity.getComponent(compatibleComponentType) == null) {
						compatible = false;
						break;
					}
					
				}
				
				if(compatible) compatibleEntities.add(entity);
				
			}
			
			system.getCompatibleEntities().addAll(compatibleEntities);
			
		}
		
		// Separate from calculating entities for system-system communication?
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
		gameSystems.add(gameSystem);
	}
	
	public void setGameSystems(ArrayList<GameSystem> gameSystems) {
		this.gameSystems.clear();
		this.gameSystems.addAll(gameSystems);
	}
	
}
