package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import javafx.concurrent.Task;

/**
 * Used to define either a client or server.
 * 
 * @author Rawad
 *
 */
public abstract class Proxy {
	
	protected Thread loadingThread;
	
	protected Runnable loadingRunnable;
	
	protected ArrayList<Task<Integer>> tasks;
	
	protected Game game;
	
	protected boolean readyToUpdate;
	
	public void init(Game game) {
		this.game = game;
		
		tasks = new ArrayList<Task<Integer>>();
		
		loadingThread = new Thread(getLoadingRunnable(), "Loading Thread");
		loadingThread.setDaemon(true);
		loadingThread.start();
		
		readyToUpdate = false;
		
	}
	
	public abstract void tick();
	
	public abstract void stop();
	
	public Runnable getLoadingRunnable() {
		
		if(loadingRunnable == null) {
			
			loadingRunnable = () -> {
				
				while(game.isRunning()) {
					
					synchronized(tasks) {
						if(tasks.size() > 0) {
							Task<Integer> task = tasks.get(0);
							try {
								task.run();
								tasks.remove(0);
							} catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					}
					
				}
			};
			
		}
		
		return loadingRunnable;
		
	}
	
	public void addTask(Task<Integer> taskToLoad) {
		synchronized(tasks) {
			tasks.add(taskToLoad);
		}
	}
	
	public Game getGame() {
		return game;
	}
	
}
