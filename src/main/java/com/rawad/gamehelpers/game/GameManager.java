package com.rawad.gamehelpers.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.log.Logger;

public class GameManager {
	
	private static final int MAX_FPS = 120;// Works fine with 300.
	
	private static Game currentGame;
	
	private static Timer gameTimer = new Timer("Game Thread");
	
	private static long sleepTime;
	private static long timePassed;
	
	static {
		setUpdateRate(MAX_FPS);
	}
	
	public static void launchGame(Game game) {
		
		if(!game.isRunning()) {// Don't launch a running game.
			
			currentGame = game;
			
			gameTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					game.init();
					
					for(Proxy proxy: game.getProxies().values()) {
						proxy.preInit(game);
					}
					
					for(Proxy proxy: game.getProxies().values()) {
						proxy.init();
					}
					
				}
				
			}, 0);
			
			gameTimer.scheduleAtFixedRate(new TimerTask() {
				
				private long currentTime = System.currentTimeMillis();
				private long prevTime = currentTime;// To keep the initial value limited to zero.
				
				@Override
				public void run() {
					
					currentTime = System.currentTimeMillis();
					
					long deltaTime = currentTime - prevTime;
					
					timePassed = (deltaTime <= 0? 1:deltaTime);
					
					prevTime = currentTime;
					
					try {
						game.update(GameManager.getTimePassed());
					} catch(Exception ex) {
						Logger.log(Logger.DEBUG, "Error in game thread.");
						ex.printStackTrace();
					}
					
					if(!game.isRunning()) gameTimer.cancel();
					
				}
				
			}, 0, sleepTime);
			
		} else {
			throw new IllegalStateException("Can't launch " + game + ", it is already running!");
		}
		
	}
	
	public static final Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * Number of times this {@code GameManager} tries to update the {@code currentGame} every second (in Hz).
	 * 
	 * @param updateRate
	 */
	public static void setUpdateRate(long updateRate) {
		
		sleepTime = TimeUnit.SECONDS.toMillis(1) / updateRate;
		
	}
	
	/**
	 * 
	 * @return The amount of time that has passed, in milliseconds, since the last update loop.
	 * @see GameThread#run()
	 */
	public static long getTimePassed() {
		return timePassed;
	}
	
}
