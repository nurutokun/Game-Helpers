package com.rawad.gamehelpers.resources;

public class GameHelpersLoader extends Loader {
	
	private static final String BASE = ResourceManager.getString("GameHelpers.name");
	
	private static final String GUI_FOLDER = ResourceManager.getString("Gui.base");
	
	public GameHelpersLoader() {
		super(BASE);
		
	}
	
	public int loadGuiTexture(String subGuiFolder, String subComponentFolder, String guiTextureFile) {
		return loadGuiTexture(ResourceManager.getProperPath(subGuiFolder, subComponentFolder), guiTextureFile);
	}
	
	public int loadGuiTexture(String subGuiFolder, String guiTextureFile) {
		return loadTexture(ResourceManager.getProperPath(GUI_FOLDER, subGuiFolder), guiTextureFile);
	}
	
}
