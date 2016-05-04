package com.rawad.gamehelpers.client.renderengine;

/**
 * Should make two levels of these: one for accepting graphics object directly from the master render and another that
 * can be composited into other renders. And this is just to help maintain a proper rendering order without having a
 * whole bunch of needless render(Graphics2D) methods being inherited and not used.
 * 
 * This will just be a basic render, that has custom render methods, for on-the-spot rendering of {@code objects}.
 * 
 * @author Rawad
 *
 */
public abstract class Render {
	
	protected Render() {
		
	}
	
}
