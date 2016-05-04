package com.rawad.gamehelpers.client.renderengine;

import javafx.scene.canvas.GraphicsContext;

/**
 * Has an abstract {@code render(GraphicsContext)} method that is called statically by the {@code MasterRender}
 * 
 * @author Rawad
 *
 */
public abstract class LayeredRender extends Render {
	
	public abstract void render(GraphicsContext g);
	
}
