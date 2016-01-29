package com.rawad.gamehelpers.gui.overlay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
	
	private final String id;
	
	private BufferedImage background;
	
	private boolean active;
	
	public Overlay(String id) {
		
		this.id = id;
		
		background = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);// Just so it's never null.
		
		setOpaque(false);
		
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
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		
	}
	
	public void setBackground(BufferedImage background) {
		this.background = background;
	}
	
}
