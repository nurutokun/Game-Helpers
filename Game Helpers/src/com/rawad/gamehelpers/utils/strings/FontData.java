package com.rawad.gamehelpers.utils.strings;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;

public class FontData {
	
	private static final String FONT_FOLDER = "/fonts/";
	
	private static final String TOKEN_SEPERATOR = " ";
	private static final String DATA_SEPERATOR = "=";
	private static final String SUB_DATA_SEPERATOR = ",";
	
	private static final String INFO = "info";
	private static final String COMMON = "common";
	private static final String PAGE = "page";
	private static final String CHARS = "chars";
	private static final String CHAR = "char";
	
	private static final double LINE_HEIGHT = 10;
	
	private static final int PAD_TOP = 0;
	private static final int PAD_LEFT = 1;
	private static final int PAD_BOTTOM = 2;
	private static final int PAD_RIGHT = 3;
	
	private static final int DESIRED_PAD_HOR = 8;
	private static final int DESIRED_PAD_VER = 8;
	
	private static final int ASCII_SPACE = 32;
	
	private HashMap<String, String> currentLineData;
	
	/** Binds specific characters to appropriate ASCII code. */
	private HashMap<Integer, CCharacter> characters;
	
	private String textureFileName;
	
	private int textureId;
	
	private int[] padding;
	
	private int paddingWidth;
	private int paddingHeight;
	
	private int lineHeight;
	
	private int imageWidth;
	private int imageHeight;
	
	private double spaceWidth;
	
	private double aspectRatio;
	
	private double verticalPerPixelSize;
	private double horizontalPerPixelSize;
	
	public FontData() {
		
		characters = new HashMap<Integer, CCharacter>();
		currentLineData = new HashMap<String, String>();
		
		aspectRatio = (double) Game.SCREEN_WIDTH / (double) Game.SCREEN_HEIGHT;
		
	}
	
	public void readFile(String fontFile) {
		
		BufferedReader reader = ResourceManager.readFile(FONT_FOLDER + fontFile + ".txt");
		
		try {
			
			for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				
				currentLineData.clear();
				
				processLine(line);
				
			}
			
		} catch(IOException ex) {
			
			Logger.log(Logger.WARNING, "Something happened while trying to read font file.");
			ex.printStackTrace();
			
		}
		
	}
	
	private void processLine(String line) {
		
		String[] tokens = line.split(TOKEN_SEPERATOR);
		
		for(int i = 1; i < tokens.length; i++) {// Skips first one
			
			String[] data = tokens[i].split(DATA_SEPERATOR);
			
			String key = data[0];
			String value = data[1];
			
			currentLineData.put(key, value);
			
		}
		
		switch(tokens[0]) {
		
		case INFO:
			processInfo();
			break;
			
		case COMMON:
			processCommon();
			break;
			
		case PAGE:
			processPage();
			break;
			
		case CHARS:
			processChars();
			break;
			
		case CHAR:
			processChar();
			break;
			
			default:
				Logger.log(Logger.WARNING, "Got unknown line ");
				break;
		
		}
		
	}
	
	private void processInfo() {
		
		padding = getDataAsIntArray("padding");
		
		paddingWidth = padding[PAD_LEFT] + padding[PAD_RIGHT];
		paddingHeight = padding[PAD_TOP] + padding[PAD_BOTTOM];
		
	}
	
	private void processCommon() {
		
		lineHeight = getDataAsInt("lineHeight");
		
		verticalPerPixelSize = LINE_HEIGHT / (double) (lineHeight - paddingHeight);
		horizontalPerPixelSize = verticalPerPixelSize / aspectRatio;
		
		imageWidth = getDataAsInt("scaleW");
		imageHeight = getDataAsInt("scaleH");
		
	}
	
	private void processPage() {
		
		textureFileName = currentLineData.get("file");
		
	}
	
	private void processChars() {}
	
	private void processChar() {
		
		int id = getDataAsInt("id");
		
		if(id == ASCII_SPACE) {
			
			spaceWidth = (getDataAsInt("xadvance") - paddingWidth) * horizontalPerPixelSize;
			
			return;
		}
		
		double xTex = ((double) getDataAsInt("x") + (padding[PAD_LEFT] - 0)) / imageWidth;
		double yTex = ((double) getDataAsInt("y") + (padding[PAD_TOP] - 0)) / imageHeight;
		
		int width = getDataAsInt("width") - (paddingWidth - (2 * DESIRED_PAD_HOR));
		int height = getDataAsInt("height") - ((paddingHeight) - (2 * DESIRED_PAD_VER));
		
		double quadWidth = width * horizontalPerPixelSize;
		double quadHeight = height * verticalPerPixelSize;
		
		double xTexSize = (double) width / imageWidth;
		double yTexSize = (double) height / imageHeight;
		
		double xOff = (getDataAsInt("xoffset") + padding[PAD_LEFT] - DESIRED_PAD_HOR) * horizontalPerPixelSize;
		double yOff = (getDataAsInt("yoffset") + (padding[PAD_TOP] - DESIRED_PAD_VER)) * verticalPerPixelSize;
		
		double xAdvance = (getDataAsInt("xadvance") - paddingWidth) * horizontalPerPixelSize;
		
		characters.put(id, new CCharacter(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, 
				xAdvance));
		
	}
	
	private int getDataAsInt(String key) {
		return Util.parseInt(currentLineData.get(key));
	}
	
	private int[] getDataAsIntArray(String key) {
		
		String[] data = currentLineData.get(key).split(SUB_DATA_SEPERATOR);
		
		int[] numberData = new int[data.length];
		
		for(int i = 0; i < data.length; i++) {
			numberData[i] = Util.parseInt(data[i]);
		}
		
		return numberData;
		
	}
	
	public String getTextureFileName() {
		return textureFileName;
	}
	
	public double getSpaceWidth() {
		return spaceWidth;
	}
	
	public CCharacter getCharacter(int ascii) {
		return characters.get(ascii);
	}
	
	public int getTextureId() {
		return textureId;
	}
	
	public void setTextureId(int textureId) {
		this.textureId = textureId;
	}
	
}
