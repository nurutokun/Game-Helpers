package com.rawad.gamehelpers.gui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.Painter;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class TextLabel extends JLabel implements Painter<TextLabel> {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -6486800223003919334L;
	
	private static final int BACKGROUND_LOCATION;
	
	private int backgroundTexture;
	
	private boolean drawBackground;
	
	public TextLabel(String text) {
//		super("<html><div style=\"text-align: center;\">" + text + "</html>");
//		super("<html><center>" + text + "</center></html>");
		super(text, SwingConstants.CENTER);
		
		setFocusable(false);
		
		setBackgroundTexture(BACKGROUND_LOCATION);
		
		UIDefaults labelDefaults = new UIDefaults();
		
		labelDefaults.put("Label.backgroundPainter", this);
		
		putClientProperty("Nimbus.Overrides", labelDefaults);
		putClientProperty("Nimbus.Overrides.InheritDefaults", false);
		
		drawBackground = true;
		
	}

	static {
		
		GameHelpersLoader loader = GameManager.instance().getCurrentGame().getLoader(GameHelpersLoader.class);
		
		BACKGROUND_LOCATION = loader.loadGuiTexture(ResourceManager.getString("TextLabel.base"), 
				ResourceManager.getString("Gui.background"));
		
	}
	
	public void setDrawBackground(boolean drawBackground) {
		this.drawBackground = drawBackground;
	}
	
	@Override
	public void paint(Graphics2D g, TextLabel object, int width, int height) {
		
		if(drawBackground) {
			BufferedImage image = ResourceManager.getTexture(backgroundTexture);
			
			g.drawImage(image, 0, 0, width, height, null);
		}
		
	}
	
	/**
	 * This is safe to call outside the EDT.
	 */
	@Override
	public void setText(final String text) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				TextLabel.super.setText(text);
			}
			
		});
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		
		BufferedImage image = ResourceManager.getTexture(BACKGROUND_LOCATION);
		
		return new Dimension(image.getWidth(), image.getHeight());
	}
	
	/**
	 * @return the backgroundTexture
	 */
	public int getBackgroundTexture() {
		return backgroundTexture;
	}
	
	/**
	 * @param backgroundTexture the backgroundTexture to set
	 */
	public void setBackgroundTexture(int backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}
	
}
