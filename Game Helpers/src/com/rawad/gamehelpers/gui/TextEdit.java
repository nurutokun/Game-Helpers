package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.rawad.gamehelpers.display.Cursors;
import com.rawad.gamehelpers.input.MouseEvent;
import com.rawad.gamehelpers.input.MouseInput;

public class TextEdit extends TextContainer {
	
	private Font font;
	
	public TextEdit(String id, String text, int x, int y, int width, int height) {
		super(id, text, x, y, width, height);
		
		font = new Font("Arial", Font.PLAIN, 10);
		
	}
	
	public TextEdit(String id, int x, int y, int width, int height) {
		this(id, id, x, y, width, height);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		super.update(e);
		
		if(isHovered()) {
			MouseInput.setCursor(Cursors.TEXT);
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		Font prevFont = g.getFont();
		
		g.setFont(font);
		
		g.setColor(Color.WHITE);
		g.fill(hitbox);
		
		super.render(g);
		
		g.setFont(prevFont);
		
	}
	
}
