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
	
	public TextRender() {
		
	}
	
	public void render(Graphics2D g, DrawableString text, FontData fontData) {
		
		// Check if texture at fontData.getTextureFileName() is loaded and load it if it isn't.
		
//		if(ResourceManager.getTextureObject()) {// Gotta add method to get texture by name.
//			
//		}
		
		for(Line line: text.getLines()) {
			
			for(Word word: line.getWords()) {
				
				for(CCharacter character: word.getCharacters()) {
					
					character.getId();
					
					BufferedImage texture = ResourceManager.getTexture(fontData.getTextureId());
					
				}
				
			}
			
		}
		
	}
	
}
