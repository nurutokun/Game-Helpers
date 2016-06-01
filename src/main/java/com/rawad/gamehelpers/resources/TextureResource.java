package com.rawad.gamehelpers.resources;

import javafx.scene.image.Image;

public class TextureResource extends Resource {
	
	public static final int UNKNOWN = -1;
	
	private Image texture;
	private int location;
	
	private ILoadListener<TextureResource> loadListener;
	
	public TextureResource(String path, int location) {
		super(path);
		
		this.location = location;
		
	}
	
	public void setOnloadAction(ILoadListener<TextureResource> loadListsner) {
		this.loadListener = loadListsner;
	}
	
	public ILoadListener<TextureResource> getLoadListener() {
		return loadListener;
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
