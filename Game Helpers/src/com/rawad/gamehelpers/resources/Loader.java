package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;

import com.rawad.gamehelpers.utils.Util;

public abstract class Loader {
	
	private static final String RES_FOLDER = ResourceManager.getString("GameHelpers.res");
	
	private static final String TEXT_FILE_EXTENSION = ResourceManager.getString("GameHelpers.txt");
	private static final String TEXTURE_FILE_EXTENSION = ResourceManager.getString("GameHelpers.png");
	
	/** Holds the name for the base texture folder. */
	private static final String TEXTURE_FOLDER = ResourceManager.getString("GameHelpers.textures");
	
	/** Subclasses don't really need access to this... Either of the two methods below should work fine. */
	private final String basePath;
	
	protected Loader(String basePath) {
		
		this.basePath = basePath;
		
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
		return ResourceManager.loadTexture(getProperPath(basePath, RES_FOLDER, TEXTURE_FOLDER, subTextureFolder, 
				textureFile) + TEXTURE_FILE_EXTENSION);
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code folderName} and {@code fileName} or any extension at all.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public BufferedReader loadFile(String folderName, String fileName) {
		return ResourceManager.readFile(getProperPath(basePath, RES_FOLDER, folderName, fileName) + 
				TEXT_FILE_EXTENSION);
	}
	
	protected void saveFile(String content, String folderName, String fileName) {
		ResourceManager.saveFile(getProperPath(basePath, RES_FOLDER, folderName, fileName) + TEXT_FILE_EXTENSION, 
				content);
	}
	
	/**
	 * Final {@code String} of {@code pathParts} should be the actual file.
	 * 
	 * @param pathParts
	 * @return
	 */
	protected final String getProperPath(String... pathParts) {
		
		String path = Util.getStringFromLines(pathParts, "/", false);
		
		return path;
		
	}
	
}
