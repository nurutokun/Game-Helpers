package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public abstract class ALoader {
	
	/** Regex added after final path, right before the extension is added. */
	public static final String REGEX_EXTENSION = ".";
	
	private static final Executor EXECUTOR_LOADING_TASKS = Executors.newSingleThreadExecutor(task -> {
		Thread t = new Thread(task, "Loading Thread");
		t.setDaemon(true);
		return t;
	});
	
	private final String basePath;
	
	/**
	 * Takes parts representing the path to the directory this {@code Loader} should load from.
	 * 
	 * @param basePathParts
	 */
	protected ALoader(String... basePathParts) {
		super();
		
		this.basePath = ALoader.getPathFromParts(basePathParts);
		
	}
	
	protected BufferedReader readFile(String extension, String... pathParts) {
		
		String fullPath = getFilePathFromParts(extension, pathParts);
		
		return readFile(fullPath);
		
	}
	
	protected BufferedReader readFile(String fullPath) {
		
		BufferedReader reader = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(fullPath));
			
		} catch(IOException ex) {
			
			Logger.log(Logger.SEVERE, "File at: \"" + fullPath + "\", could not be opened.");
			ex.printStackTrace();
			
		}
		
		return reader;
		
	}
	
	protected void saveFile(String content, String extension, String... pathParts) {
		
		String fullPath = getFilePathFromParts(extension, pathParts);
		
		try (	PrintWriter writer = new PrintWriter(new FileWriter(fullPath, false), true);
				) {
			
			writer.write(content);
			
		} catch(IOException ex) {
			
			Logger.log(Logger.WARNING, "Could not save file to: \"" + fullPath + "\".");
			ex.printStackTrace();
			
		}
		
	}
	
	/**
	 * 
	 * Concatenates the given {@code parts} into a single {@code String} that represents a full path. {@code parts} should
	 * end in actual name of the file and {@code extension} shouldn't contain a period as that is added by default.
	 * 
	 * Ex.
	 * <p> Input: <code>extension="txt", parts = {"dirA", "dirB", "dirC", "file"}</code></p>
	 * <p> Output: <code>"basePath/dirA/dirB/dirC/file.txt"</code></p>
	 * 
	 * The "/" seperator will depend on the operating system and the "." is specified by {@link ALoader#REGEX_EXTENSION}.
	 * 
	 * @param extension
	 * @param parts
	 * @return
	 * 
	 * @see ALoader#REGEX_EXTENSION
	 * @see ALoader#basePath
	 * 
	 */
	public String getFilePathFromParts(String extension, String... parts) {
		return ALoader.getPathFromParts(basePath, ALoader.getPathFromParts(parts)) + REGEX_EXTENSION + extension;
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
