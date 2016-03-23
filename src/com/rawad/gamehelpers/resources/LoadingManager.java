package com.rawad.gamehelpers.resources;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class LoadingManager {
	
	private static Executor threadExecutor;
	
	static {
		
		threadExecutor = Executors.newCachedThreadPool();
		
	}
	
	public static void load(Runnable toBeLoaded) {
		threadExecutor.execute(toBeLoaded);
	}
	
}
