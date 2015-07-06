package com.rawad.gamehelpers.util;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

public class DrawableString {
	
	public static final String LINE_SEPERATOR = System.lineSeparator();
	
	/**
	 * Used to put vertical padding between seperate lines of text.
	 * 
	 * @see #render
	 */
	private static final int VERTICAL_PADDING = 7;
	
	private String content;
	
	private HashMap<Integer, Color> backgroundColors;// Indices on either of these 2 arraylists should correspond to their proper character
	private HashMap<Integer, Color> foregroundColors;// in the content Object. Background = for highlight, Foreground = for text.
	
	public DrawableString(String content) {
		
		this.content = content;
		
		backgroundColors = new HashMap<Integer, Color>();
		foregroundColors = new HashMap<Integer, Color>();
		
	}
	
	public DrawableString() {
		this("");
	}
	
	public void render(Graphics2D g, Color textColor, Color backgroundColor, Rectangle boundingBox) {
		
		String[] lines = content.split(LINE_SEPERATOR);
		
		FontMetrics fm = g.getFontMetrics();
		
		int sections = lines.length <= 1? 2:lines.length;// For single-lines texts, it keeps it centered this way.
		
		int stringY = boundingBox.y + (boundingBox.height/sections);
		
		int indexOffset = 0;// To keep track of proper character index for multiple lines.
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i] + "\r";// Keeps it uniform, accross all platforms.
			
			int stringWidth = fm.stringWidth(line);
			int stringHeight = fm.getHeight();
			
			int stringX = boundingBox.x + (boundingBox.width/2) - (stringWidth/2);
			
			drawLine(g, textColor, backgroundColor, line, stringX, 
					stringY + (stringHeight/4 * (i+1)) + (i * VERTICAL_PADDING), indexOffset);
			
			indexOffset += line.length();
			
		}
		
	}
	
	/**
	 * 
	 * @param g
	 * @param textColor
	 * @param backgroundColor
	 * @param line
	 * @param startX
	 * @param y
	 * @param colorOffset Since each line is drawn seperately, this is added to the index of each character in this line to get its index
	 * from the {@code ArrayList} Objects to get the proper colors.
	 */
	public void drawLine(Graphics2D g, Color textColor, Color backgroundColor, String line, int startX, int y, int colorOffset) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int height = fm.getHeight();
		
		for(int i = 0; i < line.length(); i++) {
			
			String letter = String.valueOf(line.charAt(i));
			int letterWidth = getCharacterWidth(letter, fm);
			
			int colorIndex = colorOffset + i;
			
			Color color = backgroundColor;
			
			Color definedColor = backgroundColors.get(colorIndex);
			
			if(definedColor != null) {
				color = definedColor;
				definedColor = null;
//				Logger.log(Logger.DEBUG, "Defined Background Color found at: " + colorIndex + " with letter: \"" + letter + "\"");
			}
			
			g.setColor(color);
			g.fillRect(startX, y - height + 5, letterWidth, height - 5);
			
			color = textColor;
			
			definedColor = foregroundColors.get(colorIndex);
			
			if(definedColor != null) {
				color = definedColor;
				definedColor = null;
//				Logger.log(Logger.DEBUG, "Defined Text/Foreground Color found at: " + colorIndex + " with letter: \"" + letter + "\"");
			}
			
			g.setColor(color);
			g.drawString(letter, startX, y);
			
//			g.drawString("" + colorIndex, startX, y - height);
			
			startX += letterWidth;// Shifts x-value over for next letter.
			
		}
		
	}
	
	private int getCharacterWidth(String character, FontMetrics fm) {
		
		int width = 0;
		
		switch(character) {
		
		case "\n":// Sadly, can't use System.lineSeparator() here.
		case "\r":// Also, should always be this one. "\r"
		case "\r\n":
		case "\n\r":
			width = fm.getHeight() / 2;
			break;
			
		default:
			width = fm.stringWidth(character);
			break;
			
		}
		
		return width;
		
	}
	
	public void append(String s) {
		setContent(getContent() + s);
	}
	
	/**
	 * Removes the last {@code charactersToRemove} number of characters from the end of this {@code Object}'s content text.
	 * 
	 * @param charactersToRemove
	 */
	public void delete(int charactersToRemove) {
		
		int textLength = getContent().length();
		
		if(charactersToRemove >= textLength) {
			
			setContent("");
			
		} else {
			
			for(int i = 1; i <= charactersToRemove; i++) {
				
				String text = getContent();
				
				setContent(text.substring(0, text.length() - 1));
				
			}
			
		}
		
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
}
