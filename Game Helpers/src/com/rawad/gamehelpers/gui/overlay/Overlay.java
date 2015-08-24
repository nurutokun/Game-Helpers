package com.rawad.gamehelpers.gui.overlay;

import java.awt.Color;
import java.awt.Graphics2D;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

/**
 * Much like fragments for Android applications; re-usable interfaces.
 * 
 * @author Rawad
 */
public class Overlay {
	
	private static final int ALPHA = 50;
	
	private GuiManager guiManager;
	
	protected Color backgroundColor;
	
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	
	public Overlay(Color backgroundColor, int x, int y, int width, int height) {
		
		guiManager = new GuiManager();
		
		this.backgroundColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), ALPHA);
		
		this.width = width;
		this.height = height;
		
	}
	
	public Overlay(int width, int height) {
		this(0, 0, width, height);
	}
	
	public Overlay(int x, int y, int width, int height) {
		this(new Color(0, 0, 0, 0), x, y, width, height);
	}
	
	public Overlay(Color backgroundColor, int x, int y) {
		this(backgroundColor, x, y, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
	}
	
	public Overlay(Color backgroundColor) {
		this(backgroundColor, 0, 0);
	}
	
	public void addComponent(GuiComponent comp, int index) {
		guiManager.addComponent(comp, index);
	}
	
	public void addComponent(GuiComponent comp) {
		guiManager.addComponent(comp);
	}
	
	public GuiManager getGuiManager() {
		return guiManager;
	}
	
	public void update(MouseEvent me, KeyboardEvent ke) {
		guiManager.update(me, ke);
	}
	
	public void render(Graphics2D g) {
		
		g.setColor(backgroundColor);
		g.fillRect(x, y, width, height);
		
		guiManager.render(g);
		
	}
	
	public Button getClickedButton() {
		return guiManager.getCurrentClickedButton();
	}
	
}
