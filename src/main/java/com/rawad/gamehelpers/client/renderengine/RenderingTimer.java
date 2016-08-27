package com.rawad.gamehelpers.client.renderengine;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.log.Logger;

public final class RenderingTimer {
	
	/** Frames to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 30;
	
	private final ScheduledExecutorService executor;
	
	private final Renderable renderable;
	
	private final int targetFps;
	
	private long totalTime;
	
	private long currentTime;
	private long prevTime;
	
	private int frames;
	private int averageFps;
	
	private boolean running;
	
	public RenderingTimer(Renderable renderable, int targetFps) {
		super();
		
		executor = Executors.newSingleThreadScheduledExecutor(task -> {
			return new Thread(task, "Rendering Timer");
		});
		
		this.renderable = renderable;
		
		this.targetFps = targetFps;
		
		running = false;
		
	}
	
	public void start() {
		
		if(isRunning()) Logger.log(Logger.WARNING, "Trying to start Rendering Timer while it is already running.");
		
		running = true;
		
		totalTime = 0;
		
		currentTime = System.nanoTime();
		prevTime = currentTime;
		
		executor.scheduleAtFixedRate(() -> {
			
			currentTime = System.nanoTime();
			
			long deltaTime = currentTime - prevTime;
			
			totalTime += (deltaTime <= 0? 1:deltaTime);// timePassed in ().
			
			prevTime = currentTime;
			
			if(frames >= FPS_SAMPLE_RATE && totalTime > 0) {
				averageFps = (int) (frames * TimeUnit.SECONDS.toNanos(1) / totalTime);
				
				frames = 0;
				totalTime = 0;
				
			}
			
			renderable.render();
			frames++;
			
		}, 0, TimeUnit.SECONDS.toMillis(1) / targetFps, TimeUnit.MILLISECONDS);
		
	}
	
	public void stop() {
		
		executor.shutdown();
		
		running = false;
		
	}
	
	/**
	 * Submits the {@code task} to this {@code RenderingTimer}'s {@code executor} to be run on the {@code Rendering 
	 * Thread}.
	 * 
	 * @param task
	 * @return
	 */
	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}
	
	public int getAverageFps() {
		return averageFps;
	}
	
	public boolean isRunning() {
		return running;
	}
	
}
