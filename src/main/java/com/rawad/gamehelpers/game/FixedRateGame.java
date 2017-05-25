package com.rawad.gamehelpers.game;

import java.util.concurrent.TimeUnit;

/**
 * @author Rawad
 *
 */
public abstract class FixedRateGame extends Game {
	
	/** Time a single tick lasts in nanoseconds. */
	private long tickTime = TimeUnit.MILLISECONDS.toNanos(50);
	
	private long totalTime = 0;
	private long remainingTime = 0;
	
	/**
	 * @see com.rawad.gamehelpers.game.Game#update(long)
	 */
	@Override
	public void update(long timePassed) {
		
		totalTime = timePassed + remainingTime;
		
		while(totalTime >= tickTime) {
			
			totalTime -= tickTime;
			
			if(!paused && gameEngine != null) {
				gameEngine.tick();
			}
			
			for(Proxy proxy: proxies.values()) {
				// Consider moving this out of while. Get timePassed from GameManager and use to render, for example.
				if(proxy.shouldUpdate()) proxy.tick();
			}
			
		}
		
		remainingTime = totalTime;
		
	}
	
	
	/**
	 * Sets the amount of time, in nanoseconds, each tick should last .Checks to ensure {@code tickTime} is greater than or 
	 * equal to zero.
	 * 
	 * @param tickTime the tickTime to set
	 */
	public void setTickTime(long tickTime) {
		
		if(tickTime < 0) tickTime = 0;
		
		this.tickTime = tickTime;
		
	}
	
	/**
	 * @return the tickTime in nanoseconds.
	 */
	public long getTickTime() {
		return tickTime;
	}
	
}
