package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import javax.swing.RepaintManager;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.CustomRepainter;
import com.rawad.gamehelpers.utils.Util;

public class GameManager {
	
	private static int FPS = 120;// Works fine with 300.
	
	private static GameManager instance;
	
	private ArrayList<Game> games;
	
	private Game currentGame;
	
	private Thread gameThread;
	
	private long prevTime;
	private long timePassed;
	private long averageFrameRate;
	
	private GameManager() {
		
		games = new ArrayList<Game>();
		
	}
	
	static {
		
		RepaintManager.setCurrentManager(new CustomRepainter());
		
	}
	
	/**
	 * Registers the given {@code gameToLaunch} object, then it calls the {@code init()} method inherited in 
	 * the {@code Game} class.
	 * 
	 * @param gameToLaunch
	 * @see #registerGame(Game)
	 */
	public void launchGame(Game gameToLaunch, Proxy clientOrServer) {
		
		registerGame(gameToLaunch);
		
		// Make sure that this/another game isn't already running. Could maybe make this game-dependant so multiple 
		// different games can be launched at once.
		if(!gameToLaunch.isRunning()) {
			
			gameThread = new Thread(new GameThread(currentGame, clientOrServer), "Game Thread");
			
			gameThread.start();
			
		}
		
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * Registers the given {@code game} object to the current list of games, if it isn't already, and sets the 
	 * {@code currentGame} object to this one.
	 * 
	 * @param game
	 */
	public void registerGame(Game game) {
		
		if(!games.contains(game)) {
			games.add(game);
		}
		
		currentGame = game;// Don't have to initialize game in order for this value to be set (for servers, mainly).
		// Also in launchGame method
		
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
		private final Proxy clientOrServer;
		
		public GameThread(Game game, Proxy clientOrServer) {
			this.game = game;
			this.clientOrServer = clientOrServer;
		}
		
		@Override
		public void run() {
			
			game.setProxy(clientOrServer);
			
			game.init();
			
			clientOrServer.init(game);
			
			initializeGUI(clientOrServer);
			
			int frames = 0;
			int totalTime = 0;
			
			long currentTime = System.currentTimeMillis();
			
			prevTime = currentTime;// To keep the initial value limited to zero, just in case.
			
			while(game.isRunning()) {
				
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
				
//				try {
					game.update(getDeltaTime());
//				} catch(Exception ex) {
//					game.stop();
//					
//					break;
//				}
				
				try {
					Thread.sleep(1000/FPS);
				} catch(InterruptedException ex) {
					Logger.log(Logger.SEVERE, "Thread was interrupted");
				}
				
			}
			
			ResourceManager.releaseResources();
			
		}
		
		private void initializeGUI(final Proxy clientOrServer) {
			
			Util.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					clientOrServer.initGUI();
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
	
}
