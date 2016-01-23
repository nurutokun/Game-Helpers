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
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.rawad.gamehelpers.log.Logger;

public class ResourceManager {
	
	private static final String BUNDLE_NAME = "com.rawad.gamehelpers.resources.strings_resources"; //$NON-NLS-1$
	
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	
	public static final String appdataDir = System.getProperty("user.home").replace('\\', '/') + "/AppData/Roaming/My Game Launcher/";
	// Always use "/" for file paths, they are all replaced to the system-dependant file-seperator in each method.
	// TODO: Still gotta change that "/AppData/Roaming/"
	
	private static final String UNKNOWN_TEXTURE_PATH = appdataDir + "Game Helpers/res/textures/unknown.png";
	
	public static final int UNKNOWN = -1;
	
	private static final BufferedImage UNKNOWN_TEXTURE;
	
	private static HashMap<Integer, Texture> textures = new HashMap<Integer, Texture>();
	
	static {
		
		BufferedImage temp = null;
		
		try {
			temp = ImageIO.read(new File(UNKNOWN_TEXTURE_PATH));
		} catch(Exception ex) {
			Logger.log(Logger.DEBUG, "Unkown texture file couldn't be loaded from: \"" + UNKNOWN_TEXTURE_PATH + "\"");
			temp = generateUnkownTexture();
		}
		
		UNKNOWN_TEXTURE = temp;
		
	}
	
	public static String getString(String key) {
		
		return getString(RESOURCE_BUNDLE, key);
		
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
			ImageIO.write(temp, fileFormat, new File(UNKNOWN_TEXTURE_PATH));// excludes ".png"
			
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; the \"unknown texture\" was created but not saved.");
			
		}
		
		return temp;
		
	}
	
	public static int loadTexture(String imagePath) {
		
		if(imagePath.isEmpty()) {
			return UNKNOWN;
		}
		
		imagePath = (appdataDir + imagePath).replace('/', File.separatorChar);
		
		try {
			
			Texture texture = loadTexture(imagePath, ImageIO.read(new File(imagePath)));
			
			int loc = texture.getLocation();
			
			return loc;
			
		} catch(Exception ex) {
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; resource manager couldn't load image from \"" + imagePath + "\"");
			return UNKNOWN;
		}
		
	}
	
	public static Texture loadTexture(String imagePath, BufferedImage image) {
		
		int loc = getLowestResourceLocation(textures);
		
		Texture texture = getTextureByPath(imagePath);
		
		if(texture == null) {
			texture = new Texture(image, imagePath, loc);
			Logger.log(Logger.DEBUG, "Loaded new texture at location: " + loc + ", with path: " + imagePath);
			
		} else {
			Logger.log(Logger.DEBUG, "Found texture matching this one at location: " + texture.getLocation());
			
		}
		
		textures.put(loc, texture);
		
		return texture;
		
	}
	
	private static Texture getTextureByPath(String path) {
		
		Iterator<Entry<Integer, Texture>> it = textures.entrySet().iterator();
		
		while(it.hasNext()) {
			Entry<Integer, Texture> entry = it.next();
			
			Texture texture = entry.getValue();
			
			if(texture.getPath().equals(path)) {
				return texture;
			}
			
		}
		
		return null;
	}
	
	private static <T> int getLowestResourceLocation(HashMap<Integer, T> resources) {
		
		int minLoc = 0;
		int maxLoc = getMaximumResourceSize(resources.keySet());
		
		for(int i = 0; i <= maxLoc; i++) {
			
			minLoc = i;
			
			if(resources.get(i) == null) {
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
	
	/**
	 * Convencience method for getting {@code ImageIcon} object from texture location.
	 * 
	 * @param location
	 * @return
	 */
	public static ImageIcon getImageIcon(int location) {
		return new ImageIcon(getTexture(location));
	}
	
	public static BufferedImage getTexture(int location) {
		
		if(location == UNKNOWN) {
			return UNKNOWN_TEXTURE;
		}
		
		return textures.get(location).getTexture();
	}
	
	public static Texture getTextureObject(int location) {
		
		if(location == UNKNOWN) {
			return null;
		}
		
		return textures.get(location);
		
	}
	
	public static void unloadTexture(int location) {
		textures.put(location, null);
	}
	
	public static File loadFile(String filePath) {
		
		filePath = (appdataDir + filePath).replace('/', File.separatorChar);
		
		return new File(filePath);
		
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
				
				Logger.log(Logger.SEVERE, "Buffered Reader for the file at \"" + filePath + "\" couldn't be opened.");
				e.printStackTrace();
				
			}
			
		}
		
		return reader;
		
	}
	
	public static void saveFile(String filePath, String content) {
		
		filePath = (appdataDir + filePath).replace('/', File.separatorChar);
		
		try (	PrintWriter writer = new PrintWriter(new FileWriter(filePath, false), true)// Don't append, start all over every time.
				) {
			
			writer.write(content);
			
		} catch(IOException ex) {
			
			Logger.log(Logger.SEVERE, "Couldn't write to file at \"" + filePath + "\"");
			ex.printStackTrace();
			
		}
		
	}
	
	public static class Texture {
		
		private final BufferedImage texture;
		
		private final String path;
		
		private final int location;
		
		public Texture(BufferedImage texture, String path, int location) {
			this.texture = texture;
			
			this.path = path;
			
			this.location = location;
		}
		
		public BufferedImage getTexture() {
			return texture;
		}
		
		public String getPath() {
			return path;
		}
		
		public int getLocation() {
			return location;
		}
		
	}
	
}
