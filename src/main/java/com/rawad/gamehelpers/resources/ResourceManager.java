package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ResourceManager {
	
	private static boolean devEnv;
	
	public static String basePath;
	// Always use "/" for file paths, they are all replaced to the system-dependant file-seperator in each method.
	
	private static HashMap<Integer, TextureResource> textures = new HashMap<Integer, TextureResource>();
	
	private static double percentLoaded;
	
	private ResourceManager() {}
	
	static {
		
		String programDir = Util.getDefaultDirectory("");// This gives the current user directory (relative to where this
		// program was started from) because that's what it defaults to when nothing else is found.
		
		String allGamesDir = programDir.substring(0, programDir.lastIndexOf(File.separatorChar) + 1);
		// +1 at the end includes the last path seperator.
		
		basePath = allGamesDir;
		
	}
	
	public static void init(HashMap<String, String> commands) {
		
		boolean devEnv = true;
		
		try {
			
			devEnv = Boolean.valueOf(commands.get("devEnv"));
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Error; can be ignored. Caused by improper command line argument(s).");
		}
		
		setBasePath(devEnv);
		
		registerTexture(TextureResource.UNKNOWN);
		
	}
	
	/**
	 * 
	 * @param developmentEnvironment Whether or not the {@code Game} is being run from an IDE, for example, or not.
	 */
	public static void setBasePath(boolean developmentEnvironment) {
		
		basePath = basePath.replace('\\', '/');
		
		devEnv = developmentEnvironment;
		
	}
	
	public static boolean isDevEnv() {
		return devEnv;
	}
	
	/**
	 * Final {@code String} of {@code pathParts} can be the actual file; no extensions are added at the end.
	 * 
	 * @param pathParts
	 * @return
	 */
	public static String getProperPath(String... pathParts) {
		
		String path = Util.getStringFromLines(pathParts, "/", false);
		
		return path;
		
	}
	
	private static Image generateUnkownTexture() {
		
		final int width = 16;
		final int height = 16;
		
		WritableImage unknownTexture = new WritableImage(width, height);
		PixelWriter unkownTextureWriter = unknownTexture.getPixelWriter();
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				
				int violet_rgb = 0xFA00FFFF;
				
				if(x >= width/2) {// right
					if(y < height/2) {
						unkownTextureWriter.setArgb(x, y, 0xFF000000);// top right corner
					} else {
						unkownTextureWriter.setArgb(x, y, violet_rgb);// bottom right corner
					}
				} else {
					if(y < height/2) {
						unkownTextureWriter.setArgb(x, y, violet_rgb);// top left corner
					} else {
						unkownTextureWriter.setArgb(x, y, 0xFF000000);// bottom right corner
					}
				}
				
			}
		}
		
		return unknownTexture;
		
	}
	
	public static double getPercentLoaded() {
		return percentLoaded;
	}
	
	public static int loadTexture(TextureResource texture) {
		
		int location = texture.getLocation();
		
		if(texture.getTexture() == null) {
			
			if(texture.exists()) {// Texture isn't loaded but does exist so load it.
				
				try {
					
					texture.setTexture(new Image("file:" + texture.getPath()));
					
					Logger.log(Logger.DEBUG, "Loaded new texture at location: " + location
							+ " with path: \"" + texture.getPath() + "\"");
					
				} catch(Exception ex) {
					
					location = TextureResource.UNKNOWN.getLocation();
					
					ex.printStackTrace();
					
				}
				
			} else {// Texture isn't loaded and can't be loaded.
				
				if(location == TextureResource.UNKNOWN.getLocation()) {
					TextureResource.UNKNOWN.setTexture(generateUnkownTexture());
				} else {
					location = TextureResource.UNKNOWN.getLocation();
				}
				
			}
			
		}
		
		texture.setLocation(location);
		
		if(texture.getLoadListener() != null) texture.getLoadListener().onLoad(texture);
		
		return location;
		
	}
	
	public static int registerTexture(String imagePath) {
		
		int location = getLowestResourceLocation(textures, imagePath);
		
		return registerTexture(imagePath, location);
		
	}
	
	private static int registerTexture(String imagePath, int location) {
		
		imagePath = getFinalPath(basePath, imagePath);
		
		return registerTexture(new TextureResource(imagePath, location));// TODO: Should make more efficient w/ the whole
		// textures being checked if they exist earlier in this method's lifetime (could have a null check).
		
	}
	
	private static int registerTexture(TextureResource texture) {
		textures.put(texture.getLocation(), texture);
		return texture.getLocation();
	}
	
	private static <T extends Resource> int getLowestResourceLocation(HashMap<Integer, T> resources, 
			String resourcePath) {
		
		int minLoc = 0;
		int maxLoc = getMaximumResourceSize(resources.keySet());
		
		for(int i = 0; i <= maxLoc; i++) {
			
			minLoc = i;
			
			if(resources.get(i) == null) {
				break;
			}
			
			if(resources.get(i).getPath().equals(getFinalPath(basePath, resourcePath))) {// Resource already in cache
				break;
			}
			
			if(i == maxLoc) {
				minLoc = ++i;
				break;
			}
			
		}
		
		return minLoc;
		
	}
	
	private static int getMaximumResourceSize(Set<Integer> set) {
		
		int curMax = 0;
		
		Iterator<Integer> it = set.iterator();// Iterates in no particular order.
		
		while(it.hasNext()) {
			
			int curNum = it.next();
			
			if(curNum > curMax) {
				curMax = curNum;
			}
			
		}
		
		return curMax;
		
	}
	
	public static Image getTexture(int location) {
		
		TextureResource texture = getTextureObject(location);
		
		try {
			
			Image tex = texture.getTexture();
			
			if(tex != null) {
				return tex;
			} else {
				throw new NullPointerException("Texture at location " + location + " is null.");
			}
			
		} catch(NullPointerException ex) {
			
			return TextureResource.UNKNOWN.getTexture();
			
		}
		
	}
	
	public static TextureResource getTextureObject(int location) {
		return textures.get(location);
	}
	
	public static HashMap<Integer, TextureResource> getRegisteredTextures() {
		return textures;
	}
	
	public static void releaseResources() {
		
		for(Integer texture: textures.keySet()) {
			
			textures.put(texture, null);
			
		}
		
	}
	
	public static File loadFile(String filePath) {
		return new File(getFinalPath(basePath, filePath));
		
	}
	
	public static BufferedReader readFile(String filePath) {
		
		BufferedReader reader = null;
		
		File file = loadFile(filePath);
		
		try {
			
			reader = new BufferedReader(new FileReader(file));
			
		} catch(IOException ex) {
			
			Logger.log(Logger.WARNING, "Buffered Reader for file at \"" + file.getAbsolutePath() + "\" couldn't be "
					+ "opened; creating an empty Buffered Reader instead.");
			
			try {
				reader = new BufferedReader(new FileReader(""));
			} catch (FileNotFoundException ex2) {
				
				Logger.log(Logger.SEVERE, "Empty buffered Reader couldn't be opened because empty file wasn't found?");
				ex2.printStackTrace();
				
			}
			
		}
		
		return reader;
		
	}
	
	public static void saveFile(String filePath, String content) {
		
		String path = getFinalPath(basePath, filePath);
		
		// Don't append, start all over every time.
		try (	PrintWriter writer = new PrintWriter(new FileWriter(path, false), true)
				) {
			
			writer.write(content);
			
		} catch(IOException ex) {
			
			Logger.log(Logger.SEVERE, "Couldn't write to file at \"" + path + "\"");
			ex.printStackTrace();
			
		}
		
	}
	
	private static String getFinalPath(String basePath, String relativePath) {
		return (basePath + relativePath).replace('/', File.separatorChar);
	}
	
}
