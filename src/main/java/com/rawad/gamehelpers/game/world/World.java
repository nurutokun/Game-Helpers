package com.rawad.gamehelpers.game.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.rawad.gamehelpers.game.entity.Entity;

public class World {
	
	private List<Entity> entities;
	private List<Entity> readOnlyEntities;
	
	private double width;
	private double height;
	
	public World() {
		
		entities = new ArrayList<Entity>();
		readOnlyEntities = Collections.unmodifiableList(entities);
		
		width = 4096;
		height = 4096;
		
	}
	
	public boolean addEntity(Entity e) {
		return entities.add(e);
	}
	
	public void clearEntities() {
		entities.clear();
	}
	
	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}
	
	public boolean removeAllEntities(Collection<Entity> entitiesToRemove) {
		return entities.removeAll(entitiesToRemove);
	}
	
	public List<Entity> getEntities() {
		return readOnlyEntities;
	}
	
	/**
	 * @return the width
	 */
	public double getWidth() {
		return width;
	}
	
	/**
	 * @return the height
	 */
	public double getHeight() {
		return height;
	}
	
}
