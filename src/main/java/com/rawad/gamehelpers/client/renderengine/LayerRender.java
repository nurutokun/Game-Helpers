package com.rawad.gamehelpers.client.renderengine;

/**
 * Has an abstract {@code render()} method that is called by the {@code MasterRender}. Represents a single, independent 
 * layer in the {@ MasterRender}.
 * 
 * @author Rawad
 *
 */
public abstract class LayerRender extends Render implements Renderable {
	
	protected MasterRender masterRender;
	
	protected void setMasterRender(MasterRender masterRender) {
		this.masterRender = masterRender;
	}
	
}
