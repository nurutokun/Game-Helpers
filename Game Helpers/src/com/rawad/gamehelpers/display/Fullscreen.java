package com.rawad.gamehelpers.display;

import java.awt.Color;
import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.log.Logger;
import com.sun.glass.events.KeyEvent;

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
	
	private Window window;
	
	private JFrame frame;
	
	public Fullscreen() {
		super();
	}
	
	@Override
	public void create() {
		
		currentDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		
		try {
			
			if(!currentDevice.isFullScreenSupported()) {
				throw new Exception("Full screen isn't supported.");
			}
			
			DisplayMode compatibleMode = getCompatibleMode(DisplayManager.getFullScreenWidth(), DisplayManager.getFullScreenHeight());
			
			setFullScreen(compatibleMode);
			
			DisplayManager.setDisplayWidth(compatibleMode.getWidth());
			DisplayManager.setDisplayHeight(compatibleMode.getHeight());
			
		} catch(Exception ex) {
			
			Logger.log(Logger.SEVERE, ex.getLocalizedMessage() + "; abruptly exited full screen mode.");
			
			DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
			
		}
		
	}
	
	@Override
	public void destroy() {
		
		exitFullScreen();
		
	}
	
	@Override
	public void repaint() {
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F11)) {
			DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
			
			return;
		}
		
		BufferStrategy bufStrat = frame.getBufferStrategy();
		
		Graphics2D g = (Graphics2D) bufStrat.getDrawGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(DisplayManager.DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());
		
		g.setColor(Color.BLACK);
		
		if(!bufStrat.contentsLost()) {	
			
			double scaleX = (double) DisplayManager.getDisplayWidth()/(double) DisplayManager.getScreenWidth();
			double scaleY = (double) DisplayManager.getDisplayHeight()/(double) DisplayManager.getScreenHeight();
			
			g.scale(scaleX, scaleY);
			
			GameManager.getGame().render(g);
			
			g.setClip(0, 0, DisplayManager.getDisplayWidth(), DisplayManager.getDisplayHeight());
			
			bufStrat.show();
			
		}
		
		g.dispose();
		
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
		
		if(	m1.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
			&& m2.getBitDepth() != DisplayMode.BIT_DEPTH_MULTI
			&& m1.getBitDepth() != m2.getBitDepth()) {
			
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
		
		frame = new JFrame();
		
		frame.setTitle(GameManager.getGame().toString());
		
		frame.setUndecorated(true);
		frame.setIgnoreRepaint(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addKeyListener(l);
		frame.addMouseListener(l);
		frame.addMouseMotionListener(l);
		frame.addMouseWheelListener(l);
		frame.addComponentListener(l);
		frame.addWindowListener(l);
		
		currentDevice.setFullScreenWindow(frame);
		
		if (dm != null && currentDevice.isDisplayChangeSupported()) {
			
			currentDevice.setDisplayMode(dm);
			
		}
		
		frame.createBufferStrategy(2);
	}
	
	private void exitFullScreen() {
		
		currentDevice.setFullScreenWindow(null);
		
		if(window != null) {
			window.dispose();
		}
		
		frame.dispose();
		
		window = null;
		currentDevice = null;
		frame = null;
		
	}
	
	@Override
	public Component getCurrentWindow() {
		return frame;
	}
	
	public static DisplayMode[] getCompatibleDisplayModes() {
		return currentDevice.getDisplayModes();
	}
	
}
