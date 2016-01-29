package com.rawad.gamehelpers.display;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.log.Logger;

public class Fullscreen extends com.rawad.gamehelpers.display.DisplayMode {
	
	@SuppressWarnings("unused") @Deprecated
	private DisplayMode[] displayModes = {
			
			// Makes it so that the dimensions can be easily changed by changing them from the DisplayManager class
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 32, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 24, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 16, DisplayManager.REFRESH_RATE),
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 32, DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 24, DisplayMode.REFRESH_RATE_UNKNOWN),
			new DisplayMode(DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight(), 16, DisplayMode.REFRESH_RATE_UNKNOWN),
			
	};
	
	private static GraphicsDevice currentDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	
	private JFrame frame;
	
	private DisplayMode compatibleMode;
	
	public Fullscreen() {
		super();
	}
	
	@Override
	public void create(Game game) {
		super.create(game);
		
		currentDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		try {
			
			if(!currentDevice.isFullScreenSupported()) {
				throw new Exception("Full screen isn't supported.");
			}
			
			frame = new JFrame(game.toString()) {
				
				/**
				 * Generated serial version UID.
				 */
				private static final long serialVersionUID = 4557208748620152253L;;
				
				@Override
				public boolean requestFocusInWindow() {
					return Fullscreen.this.game.getContainer().requestFocusInWindow();
				}
				
			};
			
			frame.setIconImage(game.getIcon());
			
			frame.setLayout(new BorderLayout());
			
			frame.add(game.getContainer(), BorderLayout.CENTER);
//			frame.add(new Button("Testing"), BorderLayout.CENTER);
			
			frame.setUndecorated(true);
			frame.setIgnoreRepaint(true);
			frame.setResizable(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			EventHandler l = EventHandler.instance();
			
			frame.addWindowListener(l);
			
		} catch(Exception ex) {
			
			Logger.log(Logger.SEVERE, ex.getMessage() + "; abruptly exited full screen mode.");
			
			DisplayManager.requestDisplayModeChange(DisplayManager.Mode.WINDOWED);
			
		}
		
	}

	@Override
	public void show() {
		
		compatibleMode = getCompatibleMode(DisplayManager.getFullScreenWidth(), 
				DisplayManager.getFullScreenHeight());// Was in create() method before; but game has to init first.
		
		setFullScreen(compatibleMode);
		
		DisplayManager.setDisplayWidth(compatibleMode.getWidth());
		DisplayManager.setDisplayHeight(compatibleMode.getHeight());
		
	}
	
	@Override
	public void destroy() {
		
		exitFullScreen();
		
	}
	
	private DisplayMode getCompatibleMode(int width, int height) {
		
		return new DisplayMode(width, height, 32, DisplayMode.REFRESH_RATE_UNKNOWN);
		
	}
	
	@SuppressWarnings("unused")
	private DisplayMode findCompatibleMode(DisplayMode[] displayModes) {
		
		DisplayMode[] goodModes = currentDevice.getDisplayModes();
		
		for(int i = 0; i < goodModes.length; i++) {
			for(int j = 0; j < displayModes.length; j++) {
				
				if(displayModesMatch(goodModes[i], displayModes[j])) {
					return goodModes[i];
				}
				
			}
		}
		
		return null;
		
	}
	
	private boolean displayModesMatch(DisplayMode m1, DisplayMode m2) {
		
		if(m1.getWidth() != m1.getWidth() || m1.getHeight() != m2.getHeight()) {
			
			return false;
		}
		
		if(	m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && 
			m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI && 
			m1.getBitDepth() != m2.getBitDepth()) {
			
			return false;
		}
		
		if(	m1.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
			m2.getRefreshRate() != DisplayMode.REFRESH_RATE_UNKNOWN &&
			m1.getRefreshRate() != m2.getRefreshRate()) {
			
			return false;
		}

		return true;
		
	}
	
	private void setFullScreen(DisplayMode dm) {
		
		currentDevice.setFullScreenWindow(frame);
		
		if (dm != null && currentDevice.isDisplayChangeSupported()) {
			
			currentDevice.setDisplayMode(dm);
			
		}
		
//		frame.createBufferStrategy(2);// IMPORTANT: Don't set it if you don't use it; we don't.
		
	}
	
	private void exitFullScreen() {
		
		frame.dispose();
		
		currentDevice.setFullScreenWindow(null);
		
		currentDevice = null;
		frame = null;
		
	}
	
	public static DisplayMode[] getCompatibleDisplayModes() {
		return currentDevice.getDisplayModes();
	}
	
}
