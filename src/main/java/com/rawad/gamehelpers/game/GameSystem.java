package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

public abstract class GameSystem {
	
	protected final ArrayList<Class<? extends Component>> compatibleComponentTypes = 
			new ArrayList<Class<? extends Component>>();
	
	protected ArrayList<Entity> compatibleEntities = new ArrayList<Entity>();
	
	// Consider an initCompatibleEntities() method (instead of doing it in constructor).
	
	/**
	 * Loops through all the compatible {@code Entity} objects stored in {@code compatibleEntities} and forwards them to 
	 * the abstract {@link #tick(Entity)} method for individual processing.
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
	
}
