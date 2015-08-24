package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.util.strings.DrawableString;

public abstract class TextContainer extends GuiComponent {
	
	protected static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	
	protected DrawableString text;
	
	protected Color textBackground;
	protected Color textForeground;
	protected Color cursorColor;
	
	protected boolean centerText;
	protected boolean hideOutOfBoundsText;
	protected boolean wrapText;
	
	public TextContainer(String id, String text, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		
		this.text = new DrawableString(text);
		
		textBackground = TRANSPARENT;
		textForeground = Color.BLACK;
		cursorColor = Color.BLACK;
		
		centerText = true;
		hideOutOfBoundsText = false;
		wrapText = false;
		
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
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.render(g);
		
		text.render(g, textForeground, textBackground, cursorColor, hitbox, centerText, hideOutOfBoundsText);
		
	}
	
	public String getText() {
		return text.getContent();
	}
	
	public void setText(String text) {
		this.text.setContent(text);
	}
	
	public DrawableString getTextObject() {
		return text;
	}
	
	public void setHideText(boolean hideOutOfBoundsText) {
		this.hideOutOfBoundsText = hideOutOfBoundsText;
	}
	
	public void setCenterText(boolean centerText) {
		this.centerText = centerText;
	}
	
	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
	}
	
}
