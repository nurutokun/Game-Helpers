package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;

public abstract class Loader {
	
	private static final String RES_FOLDER = ResourceManager.getString("GameHelpers.res");
	
	private static final String TEXT_FILE_EXTENSION = ResourceManager.getString("GameHelpers.text");
	private static final String TEXTURE_FILE_EXTENSION = ResourceManager.getString("GameHelpers.image");
	private static final String FONT_FILE_EXTENSION = ResourceManager.getString("GameHelpers.font");
	private static final String FXML_FILE_EXTENSION = ResourceManager.getString("GameHelpers.fxml");
	private static final String CSS_FILE_EXTENSION = ResourceManager.getString("GameHelpers.css");
	private static final String ENTITY_BLUEPRINT_FILE_EXTENSION = ResourceManager.getString("GameHeleprs.xml");
	
	/** Holds the name for the base texture folder. */
	private static final String TEXTURE_FOLDER = ResourceManager.getString("GameHelpers.textures");
	private static final String FONT_FOLDER = ResourceManager.getString("Font.base");
	
	/** Subclasses don't really need access to this... Either of the two methods below should work fine. */
	private final String basePath;
	
	/**
	 * Takes parts representing the path to the directory this {@code Loader} should load from.
	 * 
	 * @param basePathParts
	 */
	protected Loader(String... basePathParts) {
		
		this.basePath = ResourceManager.getProperPath(basePathParts);
		
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
	
	public void saveFile(String content, String folderName, String fileName) {
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
	
	/**
	 * Assumes the fxml file is in the same package as the the {@code clazz} given. <b>Note:</b> can be in completely 
	 * different src file.
	 * 
	 * @param clazz
	 * @param fileName
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz, String fileName) {
		return clazz.getResource(fileName + FXML_FILE_EXTENSION);
	}
	
	/**
	 * Assumes the name of the fxml file is that of the given {@code clazz}.
	 * 
	 * @param clazz
	 * @return
	 */
	public static URL getFxmlLocation(Class<? extends Object> clazz) {
		return getFxmlLocation(clazz, clazz.getSimpleName());
	}
	
	public static String getStyleSheetLocation(Class<? extends Object> clazz, String styleSheetName) {
		return clazz.getResource(styleSheetName + CSS_FILE_EXTENSION).toExternalForm();
	}
	
	public static File getEntityBlueprintFileLocation(Class<?extends Object> clazz, String fileName) {
		return new File(clazz.getResource(fileName + ENTITY_BLUEPRINT_FILE_EXTENSION).toExternalForm());
	}
	
}
