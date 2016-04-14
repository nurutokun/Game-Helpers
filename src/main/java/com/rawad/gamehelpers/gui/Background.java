package com.rawad.gamehelpers.gui;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public class Background {
	
	private static final String DEFAULT_TEXTURE_FILE = "game_background";
	
	private static int DEFAULT_TEXTURE;
	private static int DEFAULT_FLIPPED_TEXTURE;
	
	private static Background instance;
	
	private int texture;
	private int flippedTexture;
	
	private double x;
	private double secondX;
	
	private int maxWidth;
	private int maxHeight;// Solely for rendering.
	
	private Background() {
		
		texture = DEFAULT_TEXTURE;
		
		flippedTexture = DEFAULT_FLIPPED_TEXTURE;
		
		maxWidth = (int) ResourceManager.getTexture(texture).getWidth();
		maxHeight = (int) ResourceManager.getTexture(texture).getHeight();
		
		x = 0;
		secondX = -maxWidth;
		
	}
	
	public static void registerTextures(GameHelpersLoader loader, final Game game) {
		
		DEFAULT_TEXTURE = loader.registerTexture("", DEFAULT_TEXTURE_FILE);
		
		DEFAULT_FLIPPED_TEXTURE = loader.registerTexture("", DEFAULT_TEXTURE_FILE + " (flipped)");
		
		final TextureResource texture = ResourceManager.getTextureObject(DEFAULT_TEXTURE);
		final TextureResource flippedTexture = ResourceManager.getTextureObject(DEFAULT_FLIPPED_TEXTURE);
		
		texture.setOnloadAction(new Runnable() {// In case they're not loaded in the order they're registered (which they are)
			
			@Override
			public void run() {
				flippedTexture.setTexture(getHorizontallyFlippedImage(ResourceManager.getTexture(DEFAULT_TEXTURE)));
				
				game.setBackground(Background.instance());
				
			}
			
		});
		
	}
	
	public void update(long timePassed) {
		
		double delta = (double) timePassed/16d;// The smaller the overall delta value the more accurate the display is.
		
		double offset = x - secondX;
		
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
	
//	public void render(GraphicsContext g) {
//		
//		g.setColor(Color.RED);
//		
//		g.drawImage(ResourceManager.getTexture(12), x, 0, null);
//		g.drawRect(x, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("original: " + (x+maxWidth), x, 10);
//		
//		g.setColor(Color.BLACK);
//		
//		g.drawImage(ResourceManager.getTexture(flippedTexture), secondX, 0, null);
//		g.drawRect(secondX, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("flipped: " + (secondX + maxWidth), secondX, 10);
//	}
		
	
	public double getX() {
		return x;
	}
	
	public double getSecondX() {
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
	
	public static Image getHorizontallyFlippedImage(Image original) {
		
		WritableImage flipped = new WritableImage((int) original.getWidth(), (int) original.getHeight());
		
		for(int i = 0; i < flipped.getWidth(); i++) {
			for(int j = 0; j < flipped.getHeight(); j++) {
				
				flipped.getPixelWriter().setArgb(i, j, original.getPixelReader().getArgb((int) original.getWidth() - i - 1, j));
				
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
