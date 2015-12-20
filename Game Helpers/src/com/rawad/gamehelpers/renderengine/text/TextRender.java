package com.rawad.gamehelpers.renderengine.text;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.renderengine.Render;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.strings.CCharacter;
import com.rawad.gamehelpers.utils.strings.DrawableString;
import com.rawad.gamehelpers.utils.strings.FontData;
import com.rawad.gamehelpers.utils.strings.Line;
import com.rawad.gamehelpers.utils.strings.Word;

public class TextRender extends Render {
	
	private static final double LINE_HEIGHT = 0.5;
	
	public TextRender() {
		
	}
	
	public void render(Graphics2D g, DrawableString text, FontData fontData) {
		
		// Check if texture at fontData.getTextureFileName() is loaded and load it if it isn't.
		
//		if(ResourceManager.getTextureObject()) {// Gotta add method to get texture by name.
//			
//		}
		
		BufferedImage texture = ResourceManager.getTexture(fontData.getTextureId());
		
		for(Line line: text.getLines()) {
			//base x, base y. end of each word add space, each letter adds width
			
			int x = 0;// Get from text object
			int y = 0;
			
			for(Word word: line.getWords()) {
				
				for(CCharacter c: word.getCharacters()) {
					
					BufferedImage charTexture = texture.getSubimage((int) c.getX(), (int) c.getY(), 
							(int) c.getSizeX(), (int) c.getSizeY());
					
					// c.getSizeX() and c.getSizeY() should be used for hitbox.
					
					g.drawImage(charTexture, x, y, null);
					
					x += c.getXadvance();
					
				}
				
				// add width of "space" character to x
				x += fontData.getSpaceWidth();
				
			}
			
			// add line height to y
			y += LINE_HEIGHT;// TODO: * text.getFontSize();
			
		}
		
	}
	
}
