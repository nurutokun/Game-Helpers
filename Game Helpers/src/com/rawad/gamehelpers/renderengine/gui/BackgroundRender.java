package com.rawad.gamehelpers.renderengine.gui;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.renderengine.Render;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BackgroundRender extends Render {
	
	private Background background;
	
	public BackgroundRender() {
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(background == null) return;
		
		int x = background.getX();
		int secondX = background.getSecondX();
		
		g.drawImage(ResourceManager.getTexture(background.getTexture()), x, 0, null);
		g.drawImage(ResourceManager.getTexture(background.getFlippedTexture()), secondX, 0, null);
		
	}
	
	public void setBackground(Background background) {
		this.background = background;
	}
	
}
