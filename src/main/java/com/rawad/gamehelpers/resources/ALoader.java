package com.rawad.gamehelpers.resources;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.rawad.gamehelpers.utils.Util;

public abstract class ALoader {
	
	/** Regex added after final path, right before the extension is added. */
	public static final String REGEX_EXTENSION = ".";
	
	private static final Executor EXECUTOR_LOADING_TASKS = Executors.newSingleThreadExecutor(task -> {
		Thread t = new Thread(task, "Loading Thread");
		t.setDaemon(true);
		return t;
	});
	
	private static final String FOLDER_TEXTURE = "textures";
	private static final String FOLDER_FONT = "fonts";
	
	private static final String EXTENSION_TEXT_FILE = "txt";
	private static final String EXTENSION_TEXTURE_FILE = "png";
	private static final String EXTENSION_FONT_FILE = "ttf";
	
	protected final String basePath;
	
	/**
	 * Takes parts representing the path to the directory this {@code Loader} should load from.
	 * 
	 * @param basePathParts
	 */
	protected ALoader(String... basePathParts) {
		
		this.basePath = ALoader.getPathFromParts(basePathParts);
		
	}
	
	public static final synchronized void addTask(FutureTask<Void> taskToLoad) {
		EXECUTOR_LOADING_TASKS.execute(taskToLoad);
	}
	
	/**
	 * 
	 * Concatenates the given {@code parts} into a single {@code String} that represents a full path. {@code parts} should
	 * end in actual name of the file and {@code extension} shouldn't contain a period as that is added by default.
	 * 
	 * @param extension
	 * @param parts
	 * @return
	 * 
	 * @see #REGEX_EXTENSION
	 * 
	 */
	public static final String getFilePathFromParts(String extension, String... parts) {
		return ALoader.getPathFromParts(parts) + REGEX_EXTENSION + extension;
	}
	
	/**
	 * 
	 * Ex.
	 * <p> Input: <code>parts = {"dirA", "dirB", "dirC", "file"}</code></p>
	 * <p> Output: <code>"dirA/dirB/dirC/file"</code></p>
	 * 
	 * The "/" seperator will depend on the operating system.
	 * 
	 * @param parts
	 * @return
	 */
	public static final String getPathFromParts(String... parts) {
		return Util.getStringFromLines(File.separator, false, parts);
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
//	public int registerTexture(String subTextureFolder, String textureFile) {
//		return ResourceManager.registerTexture(ResourceManager.getProperPath(basePath, FOLDER_RES, FOLDER_TEXTURE, 
//				subTextureFolder, textureFile) + EXTENSION_TEXTURE_FILE);
//	}
	
//	public void registerUnknownTexture(String unknownTextureFile) {
//		TextureResource.UNKNOWN.setFile(new File(ResourceManager.getProperPath(FOLDER_RES, FOLDER_TEXTURE, 
//				unknownTextureFile) + EXTENSION_TEXTURE_FILE));// Doesn't work with basePath; probably because File.
//	}
	
	/**
	 * DO NOT add backslahes to the variables {@code folderName} and {@code fileName} or any extension at all.
	 * 
	 * @param folderName
	 * @param fileName
	 * @return {@code BufferedReader} that can be used to read a single line of the file at a time.
	 */
//	public BufferedReader readFile(String folderName, String fileName, String extension) {
//		return ResourceManager.readFile(ResourceManager.getProperPath(basePath, FOLDER_RES, folderName, fileName) 
//				+ extension);
//	}
	
//	public void saveFile(String content, String folderName, String fileName, String extension) {
//		ResourceManager.saveFile(ResourceManager.getProperPath(basePath, FOLDER_RES, folderName, fileName) 
//				+ extension, content);
//	}
	
//	public File loadFontFile(String fileName) {
//		return ResourceManager.loadFile(ResourceManager.getProperPath(basePath, FOLDER_RES, FOLDER_FONT, fileName) 
//				+ EXTENSION_FONT_FILE);
//	}
	
//	public BufferedReader readFontFile(String fileName) {
//		return ResourceManager.readFile(ALoader.getPathFromParts(EXTENSION_FONT_FILE, basePath, FOLDER_RES, FOLDER_FONT, 
//				fileName));
//	}
	
}
