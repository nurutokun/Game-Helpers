package com.rawad.gamehelpers.gamemanager;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.RepaintManager;
import javax.swing.UIManager;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.fileparser.FileParser;
import com.rawad.gamehelpers.gamestates.StateManager;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.MasterRender;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.Util;

public abstract class Game {
	
	public static final int SCREEN_WIDTH = 640;// 640
	public static final int SCREEN_HEIGHT = 480;// 480
	
	private static final String DEFAULT_FONT = "Xenotron";
	
	protected Container superContainer;
	private CardLayout cl;
	
	protected StateManager sm;
	
	protected HashMap<Class<? extends FileParser>, FileParser> fileParsers;
	
	protected HashMap<String, Loader> loaders;
	
	protected MasterRender masterRender;
	
	protected GameHelpersLoader gameHelpersLoader;
	
	protected boolean debug;
	
	public Game() {
		
		fileParsers = new HashMap<Class<? extends FileParser>, FileParser>();
		
		loaders = new HashMap<String, Loader>();
		
	}
	
	// Should replace the two seperate init methods with just having this class be more neutral and having a client
	// instead of a game manager to hold this and other games and the viewport be exclusively for controlling the 
	// player
	/**
	 * For use by client only.
	 * 
	 */
	public void clientInit() {
		init();
		
		masterRender = new MasterRender();
		
		sm = new StateManager(Game.this);
		
		debug = false;
		
	}
	
	public void initGUI() {
		
		cl = new CardLayout();
		
		superContainer = new SuperContainer();
		
	}
	
	/**
	 * For use by server only.
	 */
	public void serverInit() {
		init();
	}
	
	protected void init() {
		
		gameHelpersLoader = new GameHelpersLoader();
		
		loaders.put(GameHelpersLoader.BASE, gameHelpersLoader);// Loads things from game helpers folder
		
		try {
			Font f = Font.createFont(Font.PLAIN, gameHelpersLoader.loadFontFile(DEFAULT_FONT))
					.deriveFont(12f);
			
			LookAndFeel fal = UIManager.getLookAndFeel();
			fal.getDefaults().put("defaultFont", f);
			
		} catch (FontFormatException | IOException ex) {
			ex.printStackTrace();
			
			Logger.log(Logger.DEBUG, "Couln't load font: \"" + DEFAULT_FONT + "\"");
			
		}
		
	}
	
	public abstract void update(long timePassed);
	
	/**
	 * Deprecated for now at least; replaced by {@code Client.render(Graphics2D)}
	 * 
	 * @param g
	 */
	@Deprecated
	public abstract void render(Graphics2D g);
	
	public abstract BufferedImage getIcon();
	
	public <T extends Loader> T getLoader(String key) {
		return Util.cast(loaders.get(key));
	}
	
	public <T extends FileParser> T getFileParser(Class<T> key) {
		return Util.cast(fileParsers.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It get's it from that "<T extends FileType>"; whatever T extends.
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	/**
	 * Can be optionally overriden.
	 * 
	 * @return
	 */
	public String getSettingsFileName() {
		return "settings";
	}
	
	// TODO: This is getting called quite a bit for some reason...
	public Container getContainer() {
		return superContainer;
	}
	
	/**
	 * Shows the required component using this {@code Game} object's {@code superContainer}.
	 * 
	 * @param name
	 * 				- Name of "card" to be shown.
	 */
	public void show(final String name) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				cl.show(superContainer, name);

//				superContainer.requestFocusInWindow();
				
			}
			
		});
		
	}
	
	private class SuperContainer extends JPanel {
		
		/**
		 * Generated serial version UID.
		 */
		private static final long serialVersionUID = 6380315214760882628L;
		
		
		public SuperContainer() {
			super();
			
			RepaintManager.currentManager(this).markCompletelyClean(this);
			
			EventHandler l = EventHandler.instance();
			
			addKeyListener(l);
			addMouseListener(l);
			addMouseMotionListener(l);
			addMouseWheelListener(l);
			addComponentListener(l);
			
			setIgnoreRepaint(true);
			
			getInputMap().put(KeyStroke.getKeyStroke("F11"), "doFullscreen");
			getActionMap().put("doFullscreen", new AbstractAction() {
				
				/**
				 * Generated serial version UID.
				 */
				private static final long serialVersionUID = 6907263934985705439L;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if(DisplayManager.getDisplayMode() == DisplayManager.Mode.FULLSCREEN) {
						DisplayManager.requestDisplayModeChange(DisplayManager.Mode.WINDOWED);
					} else {
						DisplayManager.requestDisplayModeChange(DisplayManager.Mode.FULLSCREEN);
					}
					
				}
				
			});
			
			setLayout(cl);
			
		}
		
		@Override
		public void paint(Graphics graphics) {
			
			Graphics2D g = (Graphics2D) graphics;
			
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			super.paint(g);
			
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
		}
		
	}
	
}
