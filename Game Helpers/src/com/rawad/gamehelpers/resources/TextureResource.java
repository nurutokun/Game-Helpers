package com.rawad.gamehelpers.resources;

import java.awt.image.BufferedImage;

public class TextureResource implements Resource {
	
	private final BufferedImage texture;
	
	private final String filePath;
	
	private final int textureId;
	
	public TextureResource(BufferedImage texture, String filePath, int textureId) {
		this.texture = texture;
		
		this.filePath = filePath;
		
		this.textureId = textureId;
		
	}
	
	public BufferedImage getTexture() {
		return texture;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public int getTextureId() {
		return textureId;
	}
	
}
