package com.rawad.gamehelpers.renderengine.gui.overlay;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.renderengine.Render;

public class OverlayRender extends Render {
	
	public OverlayRender() {
		
	}
	
	public void render(Graphics2D g, Overlay overlay) {
		
		g.setColor(overlay.getBackground());
		g.fillRect(overlay.getX(), overlay.getY(), overlay.getWidth(), overlay.getHeight());
		
	}
	
}
