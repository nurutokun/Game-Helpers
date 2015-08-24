package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.util.strings.DrawableString;

public class TextLabel extends TextContainer {
	
	private static final int BACKGROUND_LOCATION;
	
	public TextLabel(String text, int x, int y, int width, int height) {
		super(text, x, y, width, height);
		
		textForeground = Color.WHITE;
		
		texture = BACKGROUND_LOCATION;
		
	}
	
	public TextLabel(String text, int x, int y) {
		this(text, x, y, ResourceManager.getTexture(BACKGROUND_LOCATION).getWidth(),
				ResourceManager.getTexture(BACKGROUND_LOCATION).getHeight());
		
	}
	
	static {
		
		BACKGROUND_LOCATION = loadTexture(ResourceManager.getString("TextLabel.base"), ResourceManager.getString("Gui.background"));
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		g.drawImage(ResourceManager.getTexture(texture).getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
		
		super.render(g);
		
	}
	
	/**
	 * Appends a new line of text to this object's text, with the {@code DrawableString.NL} character appened to the end.
	 * 
	 * @param text
	 */
	public void addNewLine(String line) {
		this.text.setContent(getText() + line, width, wrapText);
	}
	
}
