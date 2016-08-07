package com.rawad.gamehelpers.client.renderengine;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.log.Logger;

public final class RenderingTimer {
	
	/** Frames to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 30;
	
	private final Timer timer;
	
	private final IRenderable renderable;
	
	private final int targetFps;
	
	private long totalTime;
	
	private long currentTime;
	private long prevTime;
	
	private int frames;
	private int averageFps;
	
	private boolean running;
	
	public RenderingTimer(IRenderable renderable, int targetFps) {
		super();
		
		timer = new Timer("Rendering Thread");
		
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
		
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				
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
				
			}
			
		}, 0, TimeUnit.SECONDS.toMillis(1) / targetFps);
	}
	
	public void stop() {
		
		timer.cancel();
		
		running = false;
		
	}
	
	public int getAverageFps() {
		return averageFps;
	}
	
	public boolean isRunning() {
		return running;
	}
	
}
