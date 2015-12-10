package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;

public abstract class Loader {
	
	private static final String TEXT_FILE_EXTENSION = ".txt";
	private static final String TEXTURE_FILE_EXTENSION = ".png";
	
	/** Holds the name for the base texture folder. */
	private static final String TEXTURE_FOLDER = "textures/";
	
	private final String basePath;
	
	protected Loader(String basePath) {
		
		this.basePath = basePath + "/";
		
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code subTextureFolder} and {@code textureFile} or any extension 
	 * at all.
	 * 
	 * Could add support for multiple "/folder/" things with a String... subFolders variable of some sort, 
	 * automatically adding the backslahes in this method.
	 * 
	 * @param subTextureFolder
	 * 							- Folder within the texture folder which contains the required texture.
	 * @param textureFile
	 * @return
	 */
	public int loadTexture(String subTextureFolder, String textureFile) {
		return ResourceManager.loadTexture(basePath + TEXTURE_FOLDER + subTextureFolder + textureFile + TEXTURE_FILE_EXTENSION);
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code folderName} and {@code fileName} or any extension at all.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public BufferedReader loadFile(String folderName, String fileName) {
		return ResourceManager.readFile(basePath + folderName + fileName + TEXT_FILE_EXTENSION);
	}
	
}
