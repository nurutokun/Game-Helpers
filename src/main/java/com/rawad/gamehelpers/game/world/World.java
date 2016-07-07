package com.rawad.gamehelpers.game.world;

import java.util.Collection;

import com.rawad.gamehelpers.game.entity.Entity;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class World {
	
	private ObservableList<Entity> entities;
	private ObservableList<Entity> readOnlyEntities;
	
	private double width;
	private double height;
	
	public World() {
		
		entities = FXCollections.observableArrayList();
		readOnlyEntities = FXCollections.unmodifiableObservableList(entities);
		
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
	
	public ObservableList<Entity> getEntities() {
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
