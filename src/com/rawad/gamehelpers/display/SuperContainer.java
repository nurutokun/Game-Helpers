package com.rawad.gamehelpers.display;

import java.awt.AWTKeyStroke;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.util.HashSet;

import javax.swing.JPanel;

import com.rawad.gamehelpers.game.Game;

public class SuperContainer extends JPanel {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 6380315214760882628L;
	
	private Dimension size;
	
	public SuperContainer(CardLayout cl) {
		super();
		
//		RepaintManager.currentManager(this).markCompletelyClean(this);// Where did this come from?
		
		size = new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);
		
		// Allows sub containers to implement their own focus traversal keys.
		HashSet<AWTKeyStroke> emptyFocusTraversal = new HashSet<AWTKeyStroke>();
		
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, emptyFocusTraversal);
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, emptyFocusTraversal);
		
		setFocusTraversalKeys(KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, emptyFocusTraversal);
		setFocusTraversalKeys(KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS, emptyFocusTraversal);
		
		setFocusable(false);
		
		setLayout(cl);
		
	}
	
	@Override
	public void paint(Graphics graphics) {
		
		Graphics2D g = (Graphics2D) graphics;
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		super.paint(g);
		
	}
	
	@Override
	public Dimension getMinimumSize() {
		return size;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return size;
	}
	
}
