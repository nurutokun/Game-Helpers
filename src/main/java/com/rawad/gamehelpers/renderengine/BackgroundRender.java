package com.rawad.gamehelpers.renderengine;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.gui.Background;
import com.rawad.gamehelpers.resources.ResourceManager;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Affine;

public class BackgroundRender extends Render {
	
	private static BackgroundRender instance;
	
	private BackgroundRender() {
		
	}
	
	public void render(GraphicsContext g, double width, double height) {
		
		Background background = GameManager.instance().getCurrentGame().getBackground();
		
		if(background == null) return;
		
		double x = background.getX();
		double secondX = background.getSecondX();
		
		Image texture = ResourceManager.getTexture(background.getTexture());
		Image flippedTexture = ResourceManager.getTexture(background.getFlippedTexture());
		
		Affine affine = g.getTransform();
		
		double scaleX = width / texture.getWidth();
		double scaleY = height / texture.getHeight();
		
		g.scale(scaleX, scaleY);
		                       
		g.drawImage(texture, x, 0);
		g.drawImage(flippedTexture, secondX, 0);
		
		g.setTransform(affine);
		
	}
	
	public static BackgroundRender instance() {
		
		if(instance == null) {
			instance = new BackgroundRender();
		}
		
		return instance;
		
	}
	
}
