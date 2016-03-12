package com.rawad.gamehelpers.display;

import java.awt.image.BufferedImage;

public abstract class DisplayMode {
	
	protected BufferedImage icon;
	
	public DisplayMode() {
		
	}
	
	public abstract void create(String displayTitle);
	
	public abstract void destroy();
	
	public abstract void show();
	
	/**
	 * Because of the way textures are rendered, the game icon can't be displayed right away.
	 * 
	 * @param icon
	 */
	public void setIcon(BufferedImage icon) {
		this.icon = icon;
	}
	
}
