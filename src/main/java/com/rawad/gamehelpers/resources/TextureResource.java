package com.rawad.gamehelpers.resources;

import javafx.scene.image.Image;

public class TextureResource extends Resource {
	
	public static final int UNKNOWN = -1;
	
	private Image texture;
	private int location;
	
	private Runnable action;
	
	public TextureResource(String path, int location) {
		super(path);
		
		this.location = location;
		
	}
	
	public void onLoad() {
		
		if(action != null) {
			action.run();
		}
		
	}
	
	public void setOnloadAction(Runnable action) {
		this.action = action;
	}
	
	/**
	 * @return the texture
	 */
	public Image getTexture() {
		return texture;
	}
	
	/**
	 * @param texture the texture to set
	 */
	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	/**
	 * @return the location
	 */
	public int getLocation() {
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(int location) {
		this.location = location;
	}
	
}
