package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.gamehelpers.utils.strings.RenderedString;
import com.rawad.gamehelpers.utils.strings.RenderedString.HorizontalAlignment;
import com.rawad.gamehelpers.utils.strings.RenderedString.VerticalAlignment;

public abstract class TextContainer extends GuiComponent {
	
	protected RenderedString text;
	
	protected Color textBackground;
	protected Color textForeground;
	protected Color cursorColor;
	
	protected boolean centerText;
	protected boolean hideOutOfBoundsText;
	protected boolean wrapText;
	
	public TextContainer(String id, String text, int x, int y, int width, int height) {
		super(id, x, y, width, height);
		
		this.text = new RenderedString(text);
		this.text.setHorizontalAlignment(HorizontalAlignment.CENTER);
		this.text.setVerticalAlignment(VerticalAlignment.CENTER);
		
		textBackground = Util.TRANSPARENT;
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
		
		// TODO: Figure out what to do with inherited string rendering.
//		text.render(g, textForeground, textBackground, cursorColor, hitbox, centerText, hideOutOfBoundsText);
		
	}
	
	public String getText() {
		return text.getContent();
	}
	
	public void setText(String text) {
		this.text.setContent(text);
	}
	
	public RenderedString getTextObject() {
		return text;
	}
	
	public Rectangle getTextBoundBox() {
		return getHitbox();
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
