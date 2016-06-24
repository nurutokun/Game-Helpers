package com.rawad.gamehelpers.game;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.log.Logger;

public class GameManager {
	
	private static final int MAX_FPS = 120;// Works fine with 300.
	
	private static GameManager instance;
	
	private ArrayList<Game> games;
	
	private Game currentGame;
	
	private Timer timer;
	
	private long sleepTime;
	private long timePassed;
	
	private GameManager() {
		
		games = new ArrayList<Game>();
		timer = new Timer("Game Loop");
		
		setUpdateRate(MAX_FPS);
		
	}
	
	/**
	 * Registers the given {@code gameToLaunch} object, then it calls the {@code init()} method inherited in 
	 * the {@code Game} class.
	 * 
	 * @param gameToLaunch
	 * @see #registerGame(Game)
	 */
	public void launchGame(Game gameToLaunch) {
		
		registerGame(gameToLaunch);
		
		// Any game can only be run once but multiple games can be run concurrently.
		if(!gameToLaunch.isRunning()) {
			
			Thread gameThread = new Thread(new GameThread(currentGame), "Game Thread");
			
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
		
		public GameThread(Game game) {
			this.game = game;
		}
		
		@Override
		public void run() {
			
			game.init();
			
			for(Proxy proxy: game.getProxies().getOrderedMap()) {
				proxy.preInit(game);
			}
			
			for(Proxy proxy: game.getProxies().getOrderedMap()) {
				proxy.init(game);
			}
			
			timer.scheduleAtFixedRate(new TimerTask() {
				
				private long currentTime = System.currentTimeMillis();
				private long prevTime = currentTime;// To keep the initial value limited to zero.
				
				@Override
				public void run() {
					
					if(!game.isRunning()) {
						timer.cancel();
						return;
					}
					
					currentTime = System.currentTimeMillis();
					
					long deltaTime = currentTime - prevTime;
					
					timePassed = (deltaTime <= 0? 1:deltaTime);
					
					prevTime = currentTime;
					
					try {
						game.update(getTimePassed());
					} catch(Exception ex) {
						Logger.log(Logger.DEBUG, "Error in game thread.");
						ex.printStackTrace();
					}
					
				}
				
			}, 0, sleepTime);
			
		}
		
	}
	
	/**
	 * Number of times this {@code GameManager} tries to update the {@code currentGame} every second (in Hz).
	 * 
	 * @param updateRate
	 */
	public void setUpdateRate(long updateRate) {
		
		sleepTime = TimeUnit.SECONDS.toMillis(1) / updateRate;
		
	}
	
	/**
	 * 
	 * @return The amount of time that has passed, in milliseconds, since the last update loop.
	 * @see GameThread#run()
	 */
	public long getTimePassed() {
		return timePassed;
	}
	
}
