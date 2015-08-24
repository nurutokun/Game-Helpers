package com.rawad.gamehelpers.gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Button extends TextContainer {
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	protected boolean clicked;
	protected boolean highlighted;
	protected boolean pressed;
	protected boolean enabled;
	
	public Button(String id, String text, int x, int y, int width, int height) {
		super(id, text, x, y, width, height);
		
		enabled = true;
		
	}
	
	public Button(String textAndId, int x, int y, int width, int height) {
		this(textAndId, textAndId, x, y, width, height);
	}
	
	public Button(String text, int x, int y) {
		this(text, x, y, ResourceManager.getTexture(BACKGROUND_LOCATION).getWidth(),
				ResourceManager.getTexture(BACKGROUND_LOCATION).getHeight());
	}
	
	static {
		
		String buttonBase = ResourceManager.getString("Button.base");
		
		BACKGROUND_LOCATION = loadTexture(buttonBase, ResourceManager.getString("Gui.background"));
		FOREGROUND_LOCATION = loadTexture(buttonBase, ResourceManager.getString("Gui.foreground"));
		ONCLICK_LOCATION = loadTexture(buttonBase, ResourceManager.getString("Gui.onclick"));
		DISABLED_LOCATION = loadTexture(buttonBase, ResourceManager.getString("Gui.disabled"));
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		if(enabled) {
			super.update(me, ke);
			
			if(!intersects(me.getX(), me.getY()) && !me.isButtonDown()) {
				mouseReleased(me);
			}
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		this.render(g, BACKGROUND_LOCATION, FOREGROUND_LOCATION, ONCLICK_LOCATION, DISABLED_LOCATION);
	}
	
	protected void render(Graphics2D g, int background, int foreground, int onclick, int disabled) {
		
		drawBase(g, background, foreground, onclick, disabled);
		
		super.render(g);// Draw's button's text
		
	}
	
	protected void drawBase(Graphics2D g, int background_loc, int foreground_loc, int onclick_loc, int disabled_loc) {
		
		if(enabled) {
			
			BufferedImage background = ResourceManager.getTexture(background_loc);
			
			g.drawImage(background.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
			
			if(highlighted) {
				
				BufferedImage foreground = ResourceManager.getTexture(foreground_loc);
				
				g.drawImage(foreground.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
			}
			
			if(pressed) {
				
				BufferedImage onclick = ResourceManager.getTexture(onclick_loc);
				
				g.drawImage(onclick.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
			}
			
		} else {
			
			BufferedImage disabled = ResourceManager.getTexture(disabled_loc);
			
			g.drawImage(disabled.getScaledInstance(width, height, BufferedImage.SCALE_FAST), x, y, null);
			
		}
		
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		pressed = false;
		clicked = true;
		
	}
	
	@Override
	protected void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
		pressed = true;
		
	}
	
	@Override
	protected void mouseReleased(MouseEvent e) {
		pressed = false;
	}
	
	@Override
	protected void mouseEntered() {
		highlighted = true;
		
		if(mouseDragged && pressed) {
			mouseDragged = false;
		}
		
	}
	
	@Override
	protected void mouseExited() {
		highlighted = false;
	}
	
	public boolean isClicked() {
		return clicked;
	}
	
	public void setClicked(boolean clicked) {
		this.clicked = clicked;	
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
}
