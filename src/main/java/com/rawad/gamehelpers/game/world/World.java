package com.rawad.gamehelpers.game.world;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.utils.ArrayObservableList;

import javafx.beans.property.ReadOnlyListWrapper;

public class World {
	
	private ArrayObservableList<Entity> entities;
	
	private double width;
	private double height;
	
	public World() {
		entities = new ArrayObservableList<Entity>();
		
		width = 2048;
		height = 2048;
		
	}
	
	public boolean addEntity(Entity e) {
		return entities.add(e);
	}
	
	public boolean removeEntity(Entity e) {
		return entities.remove(e);
	}
	
	public ReadOnlyListWrapper<Entity> getObservableEntities() {
		return new ReadOnlyListWrapper<Entity>(entities);
	}
	
	public ArrayList<Entity> getEntitiesAsList() {
		return entities.getArrayList();
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
