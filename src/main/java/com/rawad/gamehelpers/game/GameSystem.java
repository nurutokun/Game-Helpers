package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

// Graphics system (Updates rendering components), physics system (updates moving/collision components), game logic, GUI/input
public abstract class GameSystem {
	
	protected GameEngine gameEngine;
	
	protected final ArrayList<Class<? extends Component>> compatibleComponentTypes;
	
	protected ArrayList<Entity> compatibleEntities;
	
	/**
	 * Add compatible component types here.
	 */
	protected GameSystem() {
		compatibleComponentTypes = new ArrayList<Class<? extends Component>>();
		
		compatibleEntities = new ArrayList<Entity>();
		
	}
	
	/**
	 * Loops through all the compatible {@code Entity} objects stored in {@code compatibleEntities} and forwards them to the
	 * aabstract {@link #tick(Entity)} method for individual processing.
	 * 
	 * @see #tick(Entity)
	 */
	public void tick() {
		for(Entity e: compatibleEntities) {
			tick(e);
		}
	}
	
	public abstract void tick(Entity e);
	
	public ArrayList<Class<? extends Component>> getCompatibleComponentTypes() {
		return compatibleComponentTypes;
	}
	
	public ArrayList<Entity> getCompatibleEntities() {
		return compatibleEntities;
	}
	
	protected void setGameEngine(GameEngine gameEngine) {
		this.gameEngine = gameEngine;
	}
	
}
