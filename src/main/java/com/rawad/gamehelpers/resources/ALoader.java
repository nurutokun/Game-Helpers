package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public abstract class ALoader {
	
	private static final Executor EXECUTOR_LOADING_TASKS = Executors.newSingleThreadExecutor(task -> {
		Thread t = new Thread(task, "Loading Thread");
		t.setDaemon(true);
		return t;
	});
	
	private static final String FOLDER_RES = "res";
	/** Holds the name for the base texture folder. */
	private static final String FOLDER_TEXTURE = "textures";
	private static final String FOLDER_FONT = "fonts";
	/** Used solely for saving {@code Entity} blueprint .xml files.*/
	private static final String FOLDER_ENTITY_BLUEPRINT = "entity";
	
	private static final String EXTENSION_TEXT_FILE = ".txt";
	private static final String EXTENSION_TEXTURE_FILE = ".png";
	private static final String EXTENSION_FONT_FILE = ".ttf";
	private static final String EXTENSION_ENTITY_BLUEPRINT_FILE = ".xml";
	
	/** Subclasses don't really need access to this... Either of the two methods below should work fine. */
	private final String basePath;
	
	/**
	 * Takes parts representing the path to the directory this {@code Loader} should load from.
	 * 
	 * @param basePathParts
	 */
	protected ALoader(String... basePathParts) {
		
		this.basePath = ResourceManager.getProperPath(basePathParts);
		
	}
	
	public static final synchronized void addTask(FutureTask<Void> taskToLoad) {
		EXECUTOR_LOADING_TASKS.execute(taskToLoad);
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
		return ResourceManager.registerTexture(ResourceManager.getProperPath(basePath, FOLDER_RES, FOLDER_TEXTURE, 
				subTextureFolder, textureFile) + EXTENSION_TEXTURE_FILE);
	}
	
	public void registerUnknownTexture(String unknownTextureFile) {
		TextureResource.UNKNOWN.setFile(new File(ResourceManager.getProperPath(FOLDER_RES, FOLDER_TEXTURE, 
				unknownTextureFile) + EXTENSION_TEXTURE_FILE));// Doesn't work with basePath; probably because File.
	}
	
	/**
	 * DO NOT add backslahes to the variables {@code folderName} and {@code fileName} or any extension at all.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return {@code BufferedReader} that can be used to read a single line of the file at a time.
	 */
	public BufferedReader readFile(String folderName, String fileName) {
		return ResourceManager.readFile(ResourceManager.getProperPath(basePath, FOLDER_RES, folderName, fileName) 
				+ EXTENSION_TEXT_FILE);
	}
	
	public void saveFile(String content, String folderName, String fileName) {
		ResourceManager.saveFile(ResourceManager.getProperPath(basePath, FOLDER_RES, folderName, fileName) 
				+ EXTENSION_TEXT_FILE, content);
	}
	
	public File loadFontFile(String fileName) {
		return ResourceManager.loadFile(ResourceManager.getProperPath(basePath, FOLDER_RES, FOLDER_FONT, fileName) 
				+ EXTENSION_FONT_FILE);
	}
	
	public BufferedReader readFontFile(String fileName) {
		return ResourceManager.readFile(ResourceManager.getProperPath(basePath, FOLDER_RES, FOLDER_FONT, fileName) 
				+ EXTENSION_FONT_FILE);
	}
	
	public String getEntityBlueprintSaveFileLocation(String fileName) {
		return ResourceManager.getProperPath(ResourceManager.basePath, basePath, FOLDER_RES, FOLDER_ENTITY_BLUEPRINT, 
				fileName + EXTENSION_ENTITY_BLUEPRINT_FILE);
	}
	
	public static InputStream getEntityBlueprintAsStream(Class<?extends Object> clazz, String fileName) {
		return clazz.getResourceAsStream(fileName + EXTENSION_ENTITY_BLUEPRINT_FILE);
	}
	
}
