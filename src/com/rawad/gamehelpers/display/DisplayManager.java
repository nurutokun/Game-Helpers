package com.rawad.gamehelpers.display;

import java.awt.Container;
import java.util.ArrayList;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.utils.Util;

public class DisplayManager {
	
	// vvv All of these should be changeable to what the user desires
	public static int REFRESH_RATE = 60;
	
	private static int DISPLAY_WIDTH = Game.SCREEN_WIDTH;// not final; changes with the resizing of the window
	private static int DISPLAY_HEIGHT = Game.SCREEN_HEIGHT;
	
	private static int FULLSCREEN_WIDTH = Game.SCREEN_WIDTH;
	private static int FULLSCREEN_HEIGHT = Game.SCREEN_HEIGHT;
	// ^^^
	
	private static DisplayMode currentDisplayMode;
	
	private static Mode requestedMode;
	
	private static boolean closeRequested;
	
	private DisplayManager() {
		
	}
	
	/**
	 * Renders everything onto the current buffer. (Not really... Not anymore, at least. Thanks to swing.)
	 * 
	 */
	public static void update() {
		
		if(requestedMode != null) {
			
			Util.invokeLater(new Runnable() {
				
				final Mode mode = requestedMode;// This is so we can set it to null afterwards.
				
				@Override
				public void run() {
					
					showDisplayMode(mode, currentDisplayMode.game);// Take game from prev. object.
					
				}
				
			});
			
			requestedMode = null;
			
		}
		
		if(closeRequested) {
			destroyWindow();
		}
		
	}
	
	public static void requestDisplayModeChange(Mode mode) {
		requestedMode = mode;
	}
	
	/**
	 * Should only be called by the {@code GameManager}; use {@code requestDisplayModeChange(Mode} otherwise.
	 * This will immediately show the display mode. The reason for this being the whole thread thing.
	 * 
	 * @param mode
	 */
	public static void showDisplayMode(final Mode mode, final Game game) {
		
		if(currentDisplayMode != null) {// For refreshing fullscreen.
			currentDisplayMode.destroy();
		}
		
		mode.getDisplayMode().create(game);
		
		currentDisplayMode = mode.getDisplayMode();
		
		currentDisplayMode.show();
		
	}
	
	public static void requestClose() {
		closeRequested = true;
	}
	
	private static void destroyWindow() {
		
		closeRequested = false;
		
		currentDisplayMode.destroy();
		
		currentDisplayMode = null;
		 
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
			
			showDisplayMode(getDisplayMode(), currentDisplayMode.game);
			
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
	
	/**
	 * Fore {@code MouseInput} class.
	 * 
	 * @return
	 */
	public static Container getCurrentContainer() {
		return currentDisplayMode.game.getContainer();
	}
	
	public static boolean isCloseRequested() {
		return closeRequested;
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
