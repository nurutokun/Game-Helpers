package com.rawad.gamehelpers.game;

import java.util.ArrayList;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

import javafx.concurrent.Task;

/**
 * Used to define either a client or server.
 * 
 * @author Rawad
 *
 */
public abstract class Proxy {
	
	protected IController controller;
	
	protected Thread loadingThread;
	
	protected Runnable loadingRunnable;
	
	protected ArrayList<Task<Integer>> tasks;
	
	protected Game game;
	
	public void init(Game game) {
		this.game = game;
		
		tasks = new ArrayList<Task<Integer>>();
		
		loadingThread = new Thread(getLoadingRunnable(), "Loading Thread");
		loadingThread.setDaemon(true);
		loadingThread.start();
		
	}
	
	public abstract void tick();
	
	public abstract void stop();
	
	public <T extends IController> void setController(T controller) {
		this.controller = controller;
	}
	
	public final <T extends IController> T getController() {
		return Util.cast(controller);
	}
	
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
								Logger.log(Logger.DEBUG, "Task: " + task + ", task count: " + tasks.size() + ", tasks: " 
										+ tasks);
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
