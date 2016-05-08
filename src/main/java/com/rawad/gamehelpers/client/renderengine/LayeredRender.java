package com.rawad.gamehelpers.client.renderengine;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

/**
 * Has an abstract {@code render(GraphicsContext)} method that is called statically by the {@code MasterRender}
 * 
 * @author Rawad
 *
 */
public abstract class LayeredRender extends Render {
	
	private ArrayList<Entity> entities;
	
	public abstract void render(GraphicsContext g, Entity e);
	
	public void setEntities(ArrayList<Entity> entities) {
		this.entities = entities;
	}
	
	protected ArrayList<Entity> getEntities() {
		if(entities == null) entities = new ArrayList<Entity>();
		return entities;
	}
	
}
