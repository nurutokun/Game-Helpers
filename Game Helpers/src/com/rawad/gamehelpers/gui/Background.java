package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.gamehelpers.log.Logger;

public class Background {
	
	private static final BufferedImage DEFAULT_TEXTURE;
	
	private static final String DEFAULT_TEXTURE_PATH = "res/game_background.png";
	
	private BufferedImage texture;
	private BufferedImage flippedTexture;
	
	private int x;
	private int secondX;
	
	private int maxWidth;
	
	public Background() {
		
		this.texture = DEFAULT_TEXTURE;
		
		flippedTexture = getHorizontallyFlippedImage(texture);
		
		this.maxWidth = texture.getWidth();
		
		x = 0;
		secondX = -maxWidth;
		
	}
	
	static {
		
		BufferedImage temp = null;
		
		try {
			
			temp = ImageIO.read(new File(DEFAULT_TEXTURE_PATH));
			
		} catch(Exception ex) {
			
			Logger.log(Logger.DEBUG, ex.getLocalizedMessage() + "; couldn't laod background image.");
			
		} finally {
			DEFAULT_TEXTURE = temp;
		}
		
	}
	
	public void update(long timePassed) {
		
		int delta = (int) timePassed/16;// The smaller the overall delta value the more accurate the display is.
		
		x += delta;// Having these two down here works much better.
		secondX += delta;
		
		if(x >= maxWidth) {
			x = -maxWidth;
		}
		
		if(secondX >= maxWidth) {
			secondX = x - maxWidth;
		}
		
		int displacement = x - secondX;
		
		if(displacement > 0) {
			
			secondX = x - maxWidth;
			
		} else if(displacement < 0) {
			
			x = secondX - maxWidth;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(Color.RED);
		
		g.drawImage(texture, x, 0, null);
//		g.drawRect(x, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("original: " + (x+maxWidth), x, 10);
		
		g.setColor(Color.BLACK);
		
		g.drawImage(flippedTexture, secondX, 0, null);
//		g.drawRect(secondX, 0, texture.getWidth() - 1, texture.getHeight() - 1);
//		g.drawString("flipped: " + (secondX + maxWidth), secondX, 10);
		
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
	
}
