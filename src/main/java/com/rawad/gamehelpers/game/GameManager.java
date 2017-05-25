package com.rawad.gamehelpers.game;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.rawad.gamehelpers.log.Logger;

public class GameManager {
	
	private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<Runnable>();
	
	private static Thread gameThread;
	
	private static Game currentGame;
	
	private static long timePassed;
	
	public static void launchGame(Game game) {
		// Don't launch a running game.
		if(game.isRunning()) throw new IllegalStateException("Can't launch " + game + ", it is already running!");
		
		currentGame = game;
		
		// Leave this as a non-daemon thread.
		gameThread = new Thread(() -> {
			
			currentGame.init();
			
			// The game isn't actually running until after initialization.
			currentGame.setRunning(true);
			
			GameManager.gameLoop();
			
		}, "Game Thread");
		
		gameThread.start();
		
	}
	
	private static void gameLoop() {
		
		long currentTime = System.nanoTime();
		long prevTime = currentTime;
		
		while(true) {
			
			currentTime = System.nanoTime();
			
			long deltaTime = currentTime - prevTime;
			
			timePassed = (deltaTime <= 0? 1:deltaTime);
			
			prevTime = currentTime;
			
			try {
				currentGame.update(timePassed);
			} catch(Exception ex) {
				Logger.log(Logger.DEBUG, "Error in game thread.");
				ex.printStackTrace();
			}
			
			if(!currentGame.isRunning()) {
				
				currentGame.terminate();
				
				break;
				
			}
			
			for(Runnable task = tasks.poll(); task != null; task = tasks.poll()) {
				task.run();
			}
			
		}
		
	}
	
	public static Game getCurrentGame() {
		return currentGame;
	}
	
	/**
	 * 
	 * @return The amount of time that has passed, in milliseconds, since the last update loop.
	 * @see GameThread#run()
	 */
	public static long getTimePassed() {
		return timePassed;
	}
	
	public static void scheduleTask(Runnable task) {
		tasks.add(task);
	}
	
}
