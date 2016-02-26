package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;

public abstract class Loader {
	
	private static final String RES_FOLDER = ResourceManager.getString("GameHelpers.res");
	
	private static final String TEXT_FILE_EXTENSION = ResourceManager.getString("GameHelpers.text");
	private static final String TEXTURE_FILE_EXTENSION = ResourceManager.getString("GameHelpers.image");
	private static final String FONT_FILE_EXTENSION = ResourceManager.getString("GameHelpers.font");
	
	/** Holds the name for the base texture folder. */
	private static final String TEXTURE_FOLDER = ResourceManager.getString("GameHelpers.textures");
	private static final String FONT_FOLDER = ResourceManager.getString("Font.base");
	
	/** Subclasses don't really need access to this... Either of the two methods below should work fine. */
	private final String basePath;
	
	protected Loader(String basePath) {
		
		this.basePath = basePath;
		
	}
	
	/**
	 * 
	 * @param texture
	 * 		- Contains the image to be indexed by the <code>ResourceManager</code>.
	 * @param subTextureFolder
	 * @param textureFile
	 * @return
	 */
	public int loadTexture(TextureResource texture) {
		return ResourceManager.loadTexture(texture);
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code subTextureFolder} and {@code textureFile} or any extension 
	 * at all.
	 * 
	 * Could add support for multiple "/folder/" things with a String... subFolders variable of some sort, 
	 * automatically adding the backslahes in this method.
	 * 
	 * @param subTextureFolder
	 * 		- Folder within the texture folder which contains the required texture.
	 * @param textureFile
	 * @return
	 */
	public int registerTexture(String subTextureFolder, String textureFile) {
		return ResourceManager.registerTexture(ResourceManager.getProperPath(basePath, RES_FOLDER, TEXTURE_FOLDER, 
				subTextureFolder, textureFile) + TEXTURE_FILE_EXTENSION);
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code folderName} and {@code fileName} or any extension at all.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return {@code BufferedReader} that can be used to read a single line of the file at a time.
	 */
	public BufferedReader readFile(String folderName, String fileName) {
		return ResourceManager.readFile(ResourceManager.getProperPath(basePath, RES_FOLDER, folderName, fileName) 
				+ TEXT_FILE_EXTENSION);
	}
	
	protected void saveFile(String content, String folderName, String fileName) {
		ResourceManager.saveFile(ResourceManager.getProperPath(basePath, RES_FOLDER, folderName, fileName) 
				+ TEXT_FILE_EXTENSION, content);
	}
	
	public File loadFontFile(String fileName) {
		return ResourceManager.loadFile(ResourceManager.getProperPath(basePath, RES_FOLDER, FONT_FOLDER, fileName) 
				+ FONT_FILE_EXTENSION);
	}
	
	public BufferedReader readFontFile(String fileName) {
		return ResourceManager.readFile(ResourceManager.getProperPath(basePath, RES_FOLDER, FONT_FOLDER, fileName) 
				+ FONT_FILE_EXTENSION);
	}
	
}
