package com.rawad.gamehelpers.gui.overlay;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * Much like fragments for Android applications; re-usable interfaces.
 * 
 * @author Rawad
 */
public class Overlay extends JPanel {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 2754972821440440957L;

	private static final int ALPHA = 50;
	
	private final String id;
	
	private boolean active;
	
	// TODO: Allow for custom background rendering; way better than having a color with an alpha that doesn't work...
	public Overlay(String id, Color backgroundColor) {
		
		this.id = id;
		
		setBackground(backgroundColor);
		
	}
	
	public String getId() {
		return id;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setBackground(Color bg) {
		super.setBackground(new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), ALPHA));
	}
	
}
