package com.rawad.gamehelpers.resources;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

import javafx.scene.image.Image;

public class ResourceManager {
	
	/** Second path used when/if games are released; should be checked on other OS's. */
	private static final String[] REPOSITORIES;
	
	private static boolean devEnv;
	
	private static final String BUNDLE_NAME = "com.rawad.gamehelpers.resources.strings_resources"; //$NON-NLS-1$
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static String basePath;
	// Always use "/" for file paths, they are all replaced to the system-dependant file-seperator in each method.
	
	private static final String UNKNOWN_TEXTURE_PATH;
	
	private static HashMap<Integer, TextureResource> textures = new HashMap<Integer, TextureResource>();
	
	private static double percentLoaded;
	
	private ResourceManager() {}
	
	static {
		
		String userDir = Util.getDefaultDirectory("");// This gives the current user directory (relative to where this
		// program was started from) because that's what it defaults to when nothing else is found.
		
		String allProjectsDir = userDir.substring(0, userDir.lastIndexOf(File.separatorChar) + 1);
		// +1 at the end includes the last path seperator.
		
		String userSpecificStorageFolder = Util.getDefaultDirectory(System.getProperty("os.name"));
		
		String[] repos = {
				allProjectsDir,
				userSpecificStorageFolder + File.separatorChar + "My Game Launcher"	+ File.separatorChar
		};
		
		REPOSITORIES = repos;
		
		UNKNOWN_TEXTURE_PATH = getProperPath(getString("GameHelpers.name"),
				getString("GameHelpers.res"), getString("GameHelpers.textures"), "unknown")
				+ getString("GameHelpers.image");
		
	}
	
	public static void init(HashMap<String, String> commands) {
		
		try {
			
			boolean devEnv = Boolean.valueOf(commands.get("devEnv"));
			
			setBasePath(devEnv);
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Error; can be ignored. Caused by improper command line argument(s).");
		}
		
	}
	
	/**
	 * 
	 * Should make it so that it works with indices.
	 * 
	 * @param developingEnvironment
	 * 			- Whether or not the <code>Game</code> is being run from an IDE, for example, or not.
	 */
	public static void setBasePath(boolean developingEnvironment) {
		
		if(developingEnvironment) {
			basePath = REPOSITORIES[0];
			
		} else {
			basePath = REPOSITORIES[1];
			
		}
		
		basePath = basePath.replace('\\', '/');
		
		devEnv = developingEnvironment;
		
	}
	
	public static boolean isDevEnv() {
		return devEnv;
	}
	
	public static String getString(String key) {
		
		return getString(RESOURCE_BUNDLE, key);
		
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
	
	public static String getString(String bundleName, String key) {
		
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName);
		
		return getString(bundle, key);
		
	}
	
	public static String getString(ResourceBundle bundle, String key) {
		
		try {
			return bundle.getString(key);
			
		} catch (MissingResourceException e) {
			return '!' + key + '!';
			
		}
		
	}
	
	private static BufferedImage generateUnkownTexture() {
		
		BufferedImage temp = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		
		for(int x = 0; x < temp.getWidth(); x++) {
			for(int y = 0; y < temp.getHeight(); y++) {
				
				int violet_rgb = new Color(250, 0, 255).getRGB();
				
				if(x >= temp.getWidth()/2) {// right
					if(y < temp.getHeight()/2) {
						temp.setRGB(x, y, 0xFF000000);// top right corner
					} else {
						temp.setRGB(x, y, violet_rgb);// bottom right corner
					}
				} else {
					if(y < temp.getHeight()/2) {
						temp.setRGB(x, y, violet_rgb);// top left corner
					} else {
						temp.setRGB(x, y, 0xFF000000);// bottom right corner
					}
				}
				
			}
		}
		
		String fileFormat = UNKNOWN_TEXTURE_PATH.substring(UNKNOWN_TEXTURE_PATH.length() - 3);// "png"
		
		try {
			ImageIO.write(temp, fileFormat, new File(getFinalPath(basePath, UNKNOWN_TEXTURE_PATH)));// excludes ".png"
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; the \"unknown texture\" was created but "
					+ "not saved.");
			
		}
		
		return temp;
		
	}
	
	public static double getPercentLoaded() {
		return percentLoaded;
	}
	
	public static void registerUnkownTexture() {
		
		TextureResource unknownTexture = getTextureObject(TextureResource.UNKNOWN);
		
		if(unknownTexture == null) {
			
			registerTexture(UNKNOWN_TEXTURE_PATH, TextureResource.UNKNOWN);
			
			unknownTexture = getTextureObject(TextureResource.UNKNOWN);
			
			if(!unknownTexture.exists()) {
				generateUnkownTexture();
			}
			
		}
		
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
					
					location = TextureResource.UNKNOWN;
					
					ex.printStackTrace();
					
				}
				
			} else {// Texture isn't loaded and can't be loaded.
				location = TextureResource.UNKNOWN;
			}
			
			
		}
		
		texture.setLocation(location);
		
		texture.onLoad();
		
		return location;
		
	}
	
	public static int registerTexture(String imagePath) {
		
		int location = getLowestResourceLocation(textures, imagePath);
		
		return registerTexture(imagePath, location);
		
	}
	
	private static int registerTexture(String imagePath, int location) {
		
		imagePath = getFinalPath(basePath, imagePath);
		
		textures.put(location, new TextureResource(imagePath, location));// TODO: Should make more efficient w/ the whole
		// textures being checked if they exist earlier in this method's lifetime (could have a null check).
		
		return location;
		
	}
	
	private static <T extends Resource> int getLowestResourceLocation(HashMap<Integer, T> resources, String resourcePath) {
		
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
				throw new NullPointerException();
			}
			
		} catch(NullPointerException ex) {
			
			return getTextureObject(TextureResource.UNKNOWN).getTexture();
			
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
			
			try {
				reader = new BufferedReader(new FileReader(""));
			} catch (FileNotFoundException e) {
				
				Logger.log(Logger.SEVERE, "Buffered Reader for the file at \"" + file.getAbsolutePath() + "\" "
						+ "couldn't be opened.");
				e.printStackTrace();
				
			}
			
		}
		
		return reader;
		
	}
	
	public static void saveFile(String filePath, String content) {
		
		for(String path: REPOSITORIES) {
			
			path = getFinalPath(path, filePath);
			
			// Don't append, start all over every time.
			try (	PrintWriter writer = new PrintWriter(new FileWriter(path, false), true)
					) {
				
				writer.write(content);
				
			} catch(IOException ex) {
				
				Logger.log(Logger.SEVERE, "Couldn't write to file at \"" + path + "\"");
				ex.printStackTrace();
				
			}
			
		}
		
	}
	
	private static String getFinalPath(String basePath, String relativePath) {
		return (basePath + relativePath).replace('/', File.separatorChar);
	}
	
}
