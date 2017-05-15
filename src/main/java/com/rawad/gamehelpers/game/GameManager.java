package com.rawad.gamehelpers.game;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.log.Logger;

public class GameManager {
	
	private static final int MAX_UPDATE_RATE = 120;// Works fine with 300.
	
	private static Game currentGame;
	
	private static Timer gameTimer = new Timer("Game Thread");
	
	private static long sleepTime;
	private static long timePassed;
	
	static {
		setUpdateRate(MAX_UPDATE_RATE);
	}
	
	public static void launchGame(Game game) {
		
		if(!game.isRunning()) {// Don't launch a running game.
			
			currentGame = game;
			
			gameTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					
					currentGame.init();
					
				}
				
			}, 0);
			
			// The game isn't actually running until after initialization.
			currentGame.setRunning(true);
			
			// Do not use fixed rate. Tries to catch up when falls behind (relative to initial start time).
			gameTimer.schedule(new TimerTask() {
				
				private long currentTime = System.currentTimeMillis();
				private long prevTime = currentTime;// To keep the initial value limited to zero.
				
				@Override
				public void run() {
					
					currentTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
					
					long deltaTime = currentTime - prevTime;
					
					timePassed = (deltaTime <= 0? 1:deltaTime);
					
					prevTime = currentTime;
					
					try {
						currentGame.update(GameManager.getTimePassed());
					} catch(Exception ex) {
						Logger.log(Logger.DEBUG, "Error in game thread.");
						ex.printStackTrace();
					}
					
					if(!currentGame.isRunning()) {
						
						currentGame.terminate();
						
						gameTimer.cancel();
						
					}
					
				}
				
			}, 0, sleepTime);
			
		} else {
			throw new IllegalStateException("Can't launch " + game + ", it is already running!");
		}
		
	}
	
	public static Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * Number of times this {@code GameManager} tries to update the {@code currentGame} every second (in Hz). This will have
	 * to reset the {@link java.util.Timer} rspondible for updating the game.
	 * 
	 * @param updateRate
	 */
	public static void setUpdateRate(long updateRate) {
		
		if(currentGame != null && currentGame.isRunning()) {
			throw new IllegalStateException("Can't set upadte rate while game is running.");
		}
		
		sleepTime = TimeUnit.SECONDS.toMillis(1) / updateRate;
		
	}
	
	/**
	 * @return the sleepTime
	 */
	public static long getUpdateRate() {
		return sleepTime;
	}
	
	/**
	 * 
	 * @return The amount of time that has passed, in milliseconds, since the last update loop.
	 * @see GameThread#run()
	 */
	public static long getTimePassed() {
		return timePassed;
	}
	
	/**
	 * Scheduless a {@code TimerTask} on the game thread.
	 * @param task
	 */
	public static void scheduleTask(TimerTask task) {
		gameTimer.schedule(task, 0);
	}
	
}
