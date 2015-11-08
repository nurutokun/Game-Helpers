package com.rawad.gamehelpers.gamemanager;

import java.util.ArrayList;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.MasterRender;

public class GameManager {
	
	public static final int SCREEN_WIDTH = 640;//640
	public static final int SCREEN_HEIGHT = 480;//480
	
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
		
		private final MasterRender masterRender;
		
		public GameThread(Game game) {
			this.game = game;
			
			masterRender = game.getMasterRender();
			
		}
		
		@Override
		public void run() {
			
			game.init();
			
			DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED, masterRender);// Might put this back
			// in the launchGame method.
			
			int frames = 0;
			int totalTime = 0;
			
			prevTime = System.currentTimeMillis();// To keep the initial value limited to zero, just in case.
			
			while(running) {
				
				long currentTime = System.currentTimeMillis();
				
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
				
				MouseInput.update(DisplayManager.getCurrentWindowComponent(), getDeltaTime());
				
				game.update(getDeltaTime());
				
				if(useOldRendering) {
					game.render(masterRender.getGraphics());
				} else {
					masterRender.render();
				}
				
				DisplayManager.update();
				
				masterRender.clearBuffer();
				
				if(DisplayManager.isCloseRequested()) {
					running = false;
					
					DisplayManager.destroyWindow();
					
				}
				
				try {
					Thread.sleep(1000/FPS);
				} catch(InterruptedException ex) {
					Logger.log(Logger.SEVERE, "Thread was interrupted");
				}
				
			}
			
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
