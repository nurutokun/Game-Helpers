package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public abstract class AbstractLoader {
	
	/* TODO: Maybe add a third overloaded getFileFromPathFromParts(String protocol, String extension, String... paths).
	 * The protocol, so far, is mainly for something like javafx which needs it to load images; <protocol>:<filepath>.
	 */
	
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
	protected AbstractLoader(String... basePathParts) {
		super();
		
		this.basePath = AbstractLoader.getPathFromParts(basePathParts);
		
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
	 * The "/" seperator will depend on the operating system and the "." is specified by {@link AbstractLoader#REGEX_EXTENSION}.
	 * 
	 * @param extension
	 * @param parts
	 * @return
	 * 
	 * @see AbstractLoader#REGEX_EXTENSION
	 * @see AbstractLoader#basePath
	 * 
	 */
	public String getFilePathFromParts(String extension, String... parts) {
		return AbstractLoader.getPathFromParts(basePath, AbstractLoader.getPathFromParts(parts)) + REGEX_EXTENSION + extension;
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
	
	public static final synchronized void addTask(Runnable task) {
		EXECUTOR_LOADING_TASKS.execute(task);
	}
	
}
