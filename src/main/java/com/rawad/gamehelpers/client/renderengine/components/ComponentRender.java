package com.rawad.gamehelpers.client.renderengine.components;

import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

import javafx.scene.canvas.GraphicsContext;

public abstract class ComponentRender<T extends Component> extends Render {
	
	/**
	 * Calls the {@link #render(GraphicsContext, Entity, Component)} method, passing it the {@code Component} type returned
	 * by {@link #getComponentType()} only if that component is present in {@code e} (not {@code null}).
	 * 
	 * @param g
	 * @param e
	 */
	public final void render(GraphicsContext g, Entity e) {
		
		T comp = e.getComponent(getComponentType());
		
		if(comp != null) render(g, e, comp);
		
	}
	
	protected abstract void render(GraphicsContext g, Entity e, T comp);
	
	public abstract Class<T> getComponentType();
	
}
