package com.rawad.gamehelpers.client;

import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class AClient extends Proxy {
	
	/** How many {@code frames} to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 60;
	
	protected Stage stage;
	
	private Thread renderingThread;
	
	private Runnable renderingRunnable;
	
	private int frames;
	private int targetFps;
	private int averageFps;
	
	public AClient() {
		frames = 0;
		targetFps = 120;
	}
	
	public void setTargetFps(int targetFps) {
		this.targetFps = targetFps;
	}
	
	public int getAverageFps() {
		return averageFps;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	private final Runnable getRenderingRunnable() {
		
		if(renderingRunnable == null) {
			renderingRunnable = () -> {
				
				long totalTime = 0;
				
				long currentTime = System.currentTimeMillis();
				long prevTime = currentTime;
				
				while(game.isRunning()) {
					
					currentTime = System.currentTimeMillis();
					
					long deltaTime = currentTime - prevTime;
					
					totalTime += (deltaTime <= 0? 1:deltaTime);// timePassed in ().
					
					prevTime = currentTime;
					
					if(frames >= FPS_SAMPLE_RATE) {
						averageFps = (int) (frames * TimeUnit.SECONDS.toMillis(1) / totalTime);
						
						frames = 0;
						totalTime = 0;
						
					}
					
					try {
						
						Platform.runLater(() -> {
							render();
							frames++;
						});
//						Platform.runLater(() -> controller.renderThreadSafe());
						
						if(targetFps > 0) {// "Unlocked Framerate"; also prevents divide by zero exception.
							Thread.sleep(TimeUnit.SECONDS.toMillis(1)/targetFps);
						}
						
					} catch(InterruptedException ex) {
						ex.printStackTrace();
					} catch(NullPointerException ex) {
						// Have to catch this exception b/c multiple runLater calls can be made and the 
						// controller is set to null before they can be executed (when stopping).
						Logger.log(Logger.WARNING, "Got null controller when rendering.");
						break;
					}
					
				}
				
			};
		}
		
		return renderingRunnable;
		
	}
	
	@Override
	public void tick() {
		
		if(controller != null) {
			
			controller.tick();
			
		}
		
	}
	
	/**
	 * JavaFX thread safe (for now).
	 */
	protected abstract void render();
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		renderingThread = new Thread(getRenderingRunnable(), "Rendering Thread");
		renderingThread.setDaemon(true);
		renderingThread.start();
		
	}
	
	@Override
	public void stop() {
		
		setController(null);
		
	}
	
	public String getSettingsFileName() {
		return "settings";
	}
	
}
