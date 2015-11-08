package com.rawad.gamehelpers.renderengine;

import java.awt.Graphics2D;

/**
 * Should make two levels of these: one for accepting graphics object directly from the master render and another that
 * can be composited into other renders. And this is just to help maintain a proper rendering order without having a
 * whole bunch of needless render(Graphics2D) methods being inherited and not used.
 * 
 * @author Rawad
 *
 */
public abstract class Render {
	
	protected Render() {
		
	}
	
	public abstract void render(Graphics2D g);
	
}
