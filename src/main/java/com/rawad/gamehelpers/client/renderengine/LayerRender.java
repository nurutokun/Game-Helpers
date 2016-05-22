package com.rawad.gamehelpers.client.renderengine;

import javafx.scene.canvas.GraphicsContext;

/**
 * Has an abstract {@code render(GraphicsContext)} method that is called statically by the {@code MasterRender}. Represents a
 * single, independent layer in the scene.
 * 
 * @author Rawad
 *
 */
public abstract class LayerRender extends Render {
	
	public abstract void render(GraphicsContext g);
	
}
