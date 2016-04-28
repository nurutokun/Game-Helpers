package com.rawad.gamehelpers.resources;

import javafx.concurrent.Task;

public interface ILoading {
	
	public Runnable getLoadingRunnable();
	
	public void addTask(Task<Integer> taskToLoad);
	
}
