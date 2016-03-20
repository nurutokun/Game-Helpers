package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

import com.rawad.gamehelpers.utils.Util;

public class ScrollBar extends JScrollBar {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 1606991837948594425L;
	
	private CustomUI ui;
	
	public ScrollBar(Color baseColor) {
		super();
		
		/*/
		UIDefaults scrollbarDefaults = UIManager.getDefaults();
		
		scrollbarDefaults.put("ScrollBar:\"ScrollBar.button\".foregroundPainter", this);
		scrollbarDefaults.put("ScrollBar:\"ScrollBar.button\".size", 0);
		scrollbarDefaults.put("ScrollBar.decrementButtonGap", 0);
		scrollbarDefaults.put("ScrollBar.incrementButtonGap", 0);
		scrollbarDefaults.put("ScrollBar:ScrollBarTrack[Disabled].backgroundPainter", null);
		scrollbarDefaults.put("ScrollBarTrack.background", new ColorUIResource(Util.TRANSPARENT));
		
		UIDefaults scrollbarDefaults = new UIDefaults();
		
		scrollbarDefaults.put("ScrollBar.backgroundPainter", this);
		UIManager.getDefaults().put("ScrollBar:\"ScrollBar.button\".size", 0);
		UIManager.getDefaults().put("ScrollBar.decrementButtonGap", 0);
		UIManager.getDefaults().put("ScrollBar.incrementButtonGap", 0);
		
		putClientProperty("Nimbus.Overrides", scrollbarDefaults);
		putClientProperty("Nimbus.Overrides.InheritDefaults", false);/**/
		/**/
		
		ui = new CustomUI();
		
		ui.baseColor = baseColor;
		
		setUI(ui);
		
		setBackground(Util.TRANSPARENT);
		
	}
	
	public void setBaseColor(Color baseColor) {
		ui.baseColor = baseColor;
	}
	
	private static class CustomUI extends BasicScrollBarUI {
		
		private JButton emptyButton;
		
		private Color baseColor;
		
		private CustomUI() {
			
			emptyButton = new JButton();
			
			emptyButton.setPreferredSize(new Dimension(0, 0));
			emptyButton.setEnabled(false);
			
		}
		
		@Override
		protected void paintTrack(Graphics graphics, JComponent c, Rectangle trackBounds) {
//			super.paintTrack(graphics, c, trackBounds);
			
			Graphics2D g = (Graphics2D) graphics;
			
			g.setColor(baseColor);
			g.fill(trackBounds);
			
		}
		
		@Override
		protected void paintThumb(Graphics graphics, JComponent c, Rectangle thumbBounds) {
//			super.paintThumb(graphics, c, thumbBounds);
			
			Graphics2D g = (Graphics2D) graphics;
			
			g.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 
					Math.min(baseColor.getAlpha() * 4 / 3, 255)));
			g.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 10, 10);
			
		}
		
		@Override
		public Dimension getPreferredSize(JComponent c) {
			
			Dimension size = super.getPreferredSize(c);
			
			return new Dimension(size.width / 3, size.height);
			
		}
		
		@Override
		protected JButton createIncreaseButton(int orientation) {
			return emptyButton;
		}
		
		@Override
		protected JButton createDecreaseButton(int orientation) {
			return emptyButton;
		}
		
	}
	
}
