package com.rawad.gamehelpers.game;

import java.util.ArrayList;
import java.util.List;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.ClassMap;

public class GameEngine {
	
	protected final ClassMap<GameSystem> gameSystems = new ClassMap<GameSystem>();
	
	public void tick(List<Entity> entities) {
		
		for(GameSystem system: gameSystems.values()) {
			
			system.getCompatibleEntities().clear();
			
			ArrayList<Class<? extends Component>> compatibleComponentTypes = system.getCompatibleComponentTypes();
			
			ArrayList<Entity> compatibleEntities = new ArrayList<Entity>();
			
			for(Entity entity: entities) {
				
				if(Entity.contains(entity, compatibleComponentTypes)) compatibleEntities.add(entity);
				
			}
			
			system.getCompatibleEntities().addAll(compatibleEntities);
			
		}
		
		// Separate from calculating entities for system-system communication.
		for(GameSystem gameSystem: gameSystems.values()) {
			gameSystem.tick();
		}
		
	}
	
	public void setGameSystems(ClassMap<GameSystem> gameSystems) {
		
		this.gameSystems.clear();
		
		for(GameSystem gameSystem: gameSystems.values()) {
			this.gameSystems.put(gameSystem);
			gameSystem.setGameEngine(this);
		}
		
	}
	
	public ClassMap<GameSystem> getGameSystems() {
		return gameSystems;
	}
	
}
