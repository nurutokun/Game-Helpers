package com.rawad.gamehelpers.display;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.utils.Util;

public class DisplayManager {
	
	// vvv All of these should be changeable to what the user desires
	private static int DISPLAY_WIDTH = Game.SCREEN_WIDTH;// not final; changes with the resizing of the window
	private static int DISPLAY_HEIGHT = Game.SCREEN_HEIGHT;
	
	private static int FULLSCREEN_WIDTH = Game.SCREEN_WIDTH;
	private static int FULLSCREEN_HEIGHT = Game.SCREEN_HEIGHT;
	// ^^^
	
	private static SuperContainer superContainer;
	private static RXCardLayout cl;
	
	private static DisplayMode currentDisplayMode;
	
	private static Mode requestedMode;
	
	private static String displayTitle;
	
	private DisplayManager() {
		
	}
	
	/**
	 * <strike>Renders everything onto the current buffer.</strike> 
	 * (Not really... Not anymore, at least. Thanks to swing.)
	 * Now  this handles switching between display modes and handling close requests.
	 * 
	 */
	public static void update() {
		
		if(requestedMode != null) {
			
			Util.invokeLater(new Runnable() {
				
				final Mode mode = requestedMode;// This is so we can set it to null afterwards.
				
				@Override
				public void run() {
					
					showDisplayMode(mode);
					
				}
				
			});
			
			requestedMode = null;
			
		}
		
		superContainer.repaint();
		
	}
	
	public static void requestDisplayModeChange(Mode mode) {
		requestedMode = mode;
	}
	
	/**
	 * Sets up base for GUI.
	 */
	public static void init(String displayTitle) {
		
		cl = new RXCardLayout();
		
		superContainer = new SuperContainer(cl);
		
		setDisplayTitle(displayTitle);
		
	}
	
	/**
	 * Shows the required component using this {@code Game} object's {@code superContainer}.
	 * 
	 * @param name
	 * 				- Name of "card" to be shown.
	 */
	public static void show(final String name) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				cl.show(superContainer, name);
				
			}
			
		});
		
	}
	
	/**
	 * Should only be called at the beginning of an game's lifetime (<b>should change that</b>) on the EDT; should
	 * call <code>requestDisplayModeChange(Mode)</code> otherwise.
	 * 
	 * @param mode
	 */
	public static void showDisplayMode(Mode mode) {
		
		if(currentDisplayMode != null) {// For refreshing fullscreen.
			currentDisplayMode.destroy();
		}
		
		currentDisplayMode = mode.getDisplayMode();
		
		currentDisplayMode.create(displayTitle);
		
		currentDisplayMode.show();
		
	}
	
	public static void destroyWindow() {
		
		currentDisplayMode.destroy();
		
		currentDisplayMode = null;
		
	}
	
	public static void setIcon(BufferedImage icon) {
		
		for(Mode mode: Mode.values()) {
			mode.getDisplayMode().setIcon(icon);
		}
		
	}
	
	public static String[] getCompatibleDisplayModeResolutions() {
		
		java.awt.DisplayMode[] modes = Fullscreen.getCompatibleDisplayModes();
		
		ArrayList<String> prevRes = new ArrayList<String>();
		
		boolean matchFound = false;;
		
		for(int i = 0; i < modes.length; i++) {
			
			String currentRes = modes[i].getWidth() + "x" + modes[i].getHeight();
			
			for(int j = 0; j < prevRes.size(); j++) {
				
				if(prevRes.get(j).equals(currentRes)) {
					matchFound = true;
					break;
				}
				
			}
			
			if(matchFound) {
				
				matchFound = false;
				
				continue;
			} else {
				prevRes.add(currentRes);
				
			}
			
		}
		
		String[] resolutions = new String[prevRes.size()];
		
		for(int i = 0; i < resolutions.length; i++) {
			resolutions[i] = prevRes.get(i);
		}
		
		return resolutions;
		
	}
	
	public static void changeFullScreenResolution(String resolution) {
		
		int width = Integer.parseInt(resolution.split("x")[0]);
		int height = Integer.parseInt(resolution.split("x")[1]);
		
		FULLSCREEN_WIDTH = width;
		FULLSCREEN_HEIGHT = height;
		
		if(getDisplayMode() == Mode.FULLSCREEN) {
			
			showDisplayMode(getDisplayMode());
			
		}
		
	}
	
	public static String getFullScreenResolution() {
		return getFullScreenWidth() + "x" + getFullScreenHeight();
	}
	
	public static int getFullScreenWidth() {
		return FULLSCREEN_WIDTH;
	}
	
	public static int getFullScreenHeight() {
		return FULLSCREEN_HEIGHT;
	}
	
	/**
	 * Display width which is scaled up/down to from the original screen width.
	 * 
	 * @return
	 */
	public static int getDisplayWidth() {
		return DISPLAY_WIDTH;
	}
	
	public static void setDisplayWidth(int width) {
		DISPLAY_WIDTH = width;
	}
	
	/**
	 * Display height which is scaled up/down to from the original screen height.
	 * 
	 * @return
	 */
	public static int getDisplayHeight() {
		return DISPLAY_HEIGHT;
	}
	
	public static void setDisplayHeight(int height) {
		DISPLAY_HEIGHT = height;
	}
	
	public static void setDisplayTitle(String title) {
		displayTitle = title;
	}
	
	public static SuperContainer getContainer() {
		return superContainer;
	}
	
	public static Mode getDisplayMode() {
		return Mode.getMode(currentDisplayMode);
	}
	
	public static enum Mode {
		
		WINDOWED(new Windowed()),
		FULLSCREEN(new Fullscreen());
		
		private final DisplayMode displayMode;
		
		private Mode(DisplayMode displayMode) {
			this.displayMode = displayMode;
		}
		
		public DisplayMode getDisplayMode() {
			return displayMode;
		}
		
		public static Mode getMode(DisplayMode displayMode) {
			
			Mode[] modes = Mode.values();
			
			for(Mode mode: modes) {
				
				if(mode.getDisplayMode().equals(displayMode)) {
					return mode;
				}
				
			}
			
			return WINDOWED;// Default
			
		}
		
	}
	
}
