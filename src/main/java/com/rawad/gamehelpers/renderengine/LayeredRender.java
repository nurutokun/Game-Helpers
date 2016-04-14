package com.rawad.gamehelpers.renderengine;

import javafx.scene.canvas.GraphicsContext;

/**
 * Has an abstract {@code render(Graphics2D)} method that is called statically by the {@code MasterRender}
 * 
 * @author Rawad
 *
 */
public abstract class LayeredRender extends Render {
	
	public LayeredRender() {
		
	}
	
	public abstract void render(GraphicsContext g);
	
}
