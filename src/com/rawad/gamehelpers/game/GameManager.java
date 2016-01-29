package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import javax.swing.RepaintManager;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.input.Mouse;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.CustomRepainter;
import com.rawad.gamehelpers.utils.Util;

public class GameManager {
	
	private static int FPS = 60;
	
	private static GameManager instance;
	
	private ArrayList<Game> games;
	
	private Game currentGame;
	
	private Thread gameThread;
	
	private long prevTime;
	private long timePassed;
	private long averageFrameRate;
	
	/** Represents if a game is running or not. */
	private boolean running;
	
	private boolean useOldRendering;
	
	private GameManager() {
		
		games = new ArrayList<Game>();
		
		running = false;
		
		useOldRendering = false;
		
	}
	
	static {
		
		RepaintManager.setCurrentManager(new CustomRepainter());
		
		boolean setLookAndFeel = false;
		
		try {
			
			
			for(LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()) {
				
				if("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					
					setLookAndFeel = true;
					
					break;
				}
				
			}
			
			if(!setLookAndFeel) {
				throw new Exception("Couldn't set Nimbus Look and Feel!");
			}
			
		} catch(Exception e) {
			
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | UnsupportedLookAndFeelException ex) {
				
				Logger.log(Logger.WARNING, "Couldn't load system look and feel; " + ex.getMessage() 
						+ ". Using default look and feel instead.");
				
			}
			
		}
	}
	
	/**
	 * Registers the given {@code gameToLaunch} object, then it calls the {@code init()} method inherited in 
	 * the {@code Game} class and starts the {@code DisplayManager}.
	 * 
	 * @param gameToLaunch
	 * @see #registerGame(Game)
	 */
	public void launchGame(Game gameToLaunch) {
		
		registerGame(gameToLaunch);
		
		// Make sure that this/another game isn't already running. Could maybe make this game-dependant so multiple different games can be
		// launched at once.
		if(!running) {
			
			running = true;
			
			gameThread = new Thread(new GameThread(currentGame), "Game Thread");
			
			gameThread.start();
			
		}
		
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}
	
	public void onClose() {
		
	}
	
	/**
	 * Registers the given {@code game} object to the current list of games, if it isn't already, and sets the {@code currentGame} object to 
	 * this one.
	 * 
	 * @param game
	 */
	public void registerGame(Game game) {
		
		if(!games.contains(game)) {
			games.add(game);
		}
		
		currentGame = game;// Don't have to initialize game in order for this value to be set (for servers, mainly). Also in launchGame method
		
	}
	
	public Game[] getGames() {
		return games.toArray(new Game[games.size()]);
	}
	
	public static GameManager instance() {
		
		if(instance == null) {
			instance = new GameManager();
		}
		
		return instance;
		
	}
	
	private class GameThread implements Runnable {
		
		private final Game game;
		
		public GameThread(Game game) {
			this.game = game;
			
		}
		
		@Override
		public void run() {

			game.clientInit();// If you're running the game from here, you've got to be a client.
			
			initializeGUI(game);
			
			int frames = 0;
			int totalTime = 0;
			
			long currentTime = System.currentTimeMillis();
			
			prevTime = currentTime;// To keep the initial value limited to zero, just in case.
			
			while(running) {
				
				currentTime = System.currentTimeMillis();
				
				long deltaTime = currentTime - prevTime;
				
				timePassed = (deltaTime <= 0? 1:deltaTime);
				
				totalTime += timePassed;
				
				frames++;
				
				if(frames >= FPS) {
					
					averageFrameRate = frames * 1000 / totalTime;
					
					frames = 0;
					totalTime = 0;
					
				}
				
				prevTime = currentTime;
				
				Mouse.update(DisplayManager.getCurrentContainer(), getDeltaTime());
				
				game.update(getDeltaTime());
				
				if(DisplayManager.isCloseRequested()) {
					running = false;
					
				}
				
				DisplayManager.update();
				
				try {
					Thread.sleep(1000/FPS);
				} catch(InterruptedException ex) {
					Logger.log(Logger.SEVERE, "Thread was interrupted");
				}
				
			}
			
		}
		
		private void initializeGUI(final Game game) {
			
			Util.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					game.initGUI();
					
					DisplayManager.showDisplayMode(DisplayManager.Mode.WINDOWED, game);
					
				}
				
			});
			
		}
		
	}
	
	public long getDeltaTime() {
		return timePassed;
	}
	
	public long getFPS() {
		return averageFrameRate;
	}
	
	public void changeUseOldRendering() {
		useOldRendering = !useOldRendering;
	}
	
	public boolean shouldUseOldRendering() {
		return useOldRendering;
	}
	
}
