package com.rawad.gamehelpers.renderengine;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.resources.ResourceManager;

public class BackgroundRender extends Render {
	
	private static BackgroundRender instance;
	
	private BackgroundRender() {
		
	}
	
	public void render(Graphics2D g) {
		
		Background background = Background.instance();
		
		int x = background.getX();
		int secondX = background.getSecondX();
		
		double scaleX = (double) DisplayManager.getDisplayWidth() / (double) background.getMaxWidth();
		double scaleY = (double) DisplayManager.getDisplayHeight() / (double) background.getMaxHeight();
		
		g.scale(scaleX, scaleY);
		
		g.drawImage(ResourceManager.getTexture(background.getTexture()), x, 0, null);
		g.drawImage(ResourceManager.getTexture(background.getFlippedTexture()), secondX, 0, null);
		
		g.scale(1d/scaleX, 1d/scaleY);

	}
	
	public static BackgroundRender instance() {
		
		if(instance == null) {
			instance = new BackgroundRender();
		}
		
		return instance;
		
	}
	
}
