package com.rawad.gamehelpers.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.resources.ResourceManager;

public class Background {
	
	private static final String DEFAULT_TEXTURE_PATH = "Game Helpers/res/textures/game_background.png";
	
	private static final int DEFAULT_TEXTURE;
	private static final int DEFAULT_FLIPPED_TEXTURE;
	
	private static Background instance;
	
	private int texture;
	private int flippedTexture;
	
	private int x;
	private int secondX;
	
	private int maxWidth;
	private int maxHeight;// Solely for rendering.
	
	private Background() {
		
		texture = DEFAULT_TEXTURE;
		
		flippedTexture = DEFAULT_FLIPPED_TEXTURE;
		
		maxWidth = ResourceManager.getTexture(texture).getWidth();
		maxHeight = ResourceManager.getTexture(texture).getHeight();
		
		x = 0;
		secondX = -maxWidth;
		
	}
	
	static {
		
		DEFAULT_TEXTURE = ResourceManager.loadTexture(DEFAULT_TEXTURE_PATH);
		
		BufferedImage temp = ResourceManager.getTexture(DEFAULT_TEXTURE);
		
		DEFAULT_FLIPPED_TEXTURE = ResourceManager.loadTexture(ResourceManager.getTextureObject(DEFAULT_TEXTURE)
				.getPath() + "(flipped)", getHorizontallyFlippedImage(temp)).getLocation();
		
	}
	
	public void update(long timePassed) {
		
		int delta = (int) timePassed/16;// The smaller the overall delta value the more accurate the display is.
		
		int offset = x - secondX;
		
		if(Math.abs(offset) - maxWidth > 0) {
			
			if(offset > 0) {
				secondX = x - maxWidth;
			} else if(offset < 0) {
				x = secondX - maxWidth;
			}
			
		}
		
		x += delta;
		secondX += delta;
		
		if(x >= maxWidth) {
			x = 0 - maxWidth;
		}
		
		if(secondX > maxWidth) {
			secondX = x - maxWidth;
		}
		
	}
	
	public void render(Graphics2D g) {
		
//		g.setColor(Color.RED);
		
		g.drawImage(ResourceManager.getTexture(texture), x, 0, null);
//		g.drawRect(x, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("original: " + (x+maxWidth), x, 10);
		
//		g.setColor(Color.BLACK);
		
		g.drawImage(ResourceManager.getTexture(flippedTexture), secondX, 0, null);
//		g.drawRect(secondX, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("flipped: " + (secondX + maxWidth), secondX, 10);
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getSecondX() {
		return secondX;
	}
	
	public int getTexture() {
		return texture;
	}
	
	public int getFlippedTexture() {
		return flippedTexture;
	}
	
	public int getMaxWidth() {
		return maxWidth;
	}
	
	public int getMaxHeight() {
		return maxHeight;
	}
	
	public static BufferedImage getHorizontallyFlippedImage(BufferedImage original) {
		
		BufferedImage flipped = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		
		for(int i = 0; i < flipped.getWidth(); i++) {
			for(int j = 0; j < flipped.getHeight(); j++) {
				
				flipped.setRGB(i, j, original.getRGB(original.getWidth() - i - 1, j));
				
			}
		}
		
		return flipped;
		
	}
	
	public static Background instance() {
		
		if(instance == null) {
			instance = new Background();
		}
		
		return instance;
		
	}
	
}
