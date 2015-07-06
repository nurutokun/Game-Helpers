package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.gamehelpers.util.DrawableString;

public abstract class TextContainer extends GuiComponent {
	
	protected static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	protected DrawableString text;
	
	protected Color textBackground;
	protected Color textForeground;
	
	public TextContainer(String id, String text, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		
		this.text = new DrawableString(text);
		
		textBackground = TRANSPARENT;
		textForeground = Color.BLACK;
		
	}
	
	/**
	 * Convenience constructor that uses id as text.
	 * 
	 * @param id
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public TextContainer(String id, int x, int y, int width, int height) {
		this(id, id, x, y, width, height);
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		text.render(g, textForeground, textBackground, hitbox);
		
	}
	
	public void append(String s) {
		text.append(s);
	}
	
	public void delete(int charactersToRemove) {
		text.delete(charactersToRemove);
	}
	
	public String getText() {
		return text.getContent();
	}
	
	public void setText(String text) {
		this.text.setContent(text);
	}
	
}
