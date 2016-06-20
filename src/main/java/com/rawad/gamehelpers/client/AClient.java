package com.rawad.gamehelpers.client;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class AClient extends Proxy {
	
	/** How many {@code frames} to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 30;
	
	protected Stage stage;
	
	protected StateManager sm;
	
	protected InputBindings inputBindings;
	
	protected boolean readyToRender;
	
	private Thread renderingThread;
	
	private Runnable renderingRunnable;
	
	private int frames;
	private int targetFps;
	private int averageFps;
	
	public AClient() {
		frames = 0;
		targetFps = 60;
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
					
					if(frames >= FPS_SAMPLE_RATE && totalTime > 0) {
						averageFps = (int) (frames * TimeUnit.SECONDS.toMillis(1) / totalTime);
						
						frames = 0;
						totalTime = 0;
						
					}
					
					try {
						
						Platform.runLater(() -> {
							
							if(readyToRender) {
								synchronized(game.getWorld().getEntities()) {
									sm.getCurrentState().render();
									frames++;
								}
							}
							
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
	
	/**
	 * Sets {@link #stage} to the given {@code stage} parameter. This method should be called before 
	 * {@link #init(Game)}.
	 * 
	 * @param stage
	 */
	public void initGui(Stage stage) {
		this.stage = stage;
		
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable ex) {
				Logger.log(Logger.SEVERE, "Uncaught Exception.");
				ex.printStackTrace();
			}
		});
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		inputBindings = new InputBindings();
		
		initInputBindings();
		
		sm = new StateManager(game, this);
		
		renderingThread = new Thread(getRenderingRunnable(), "Rendering Thread");
		renderingThread.setDaemon(true);
		renderingThread.start();
		
		readyToRender = false;
		
	}
	
	protected abstract void initInputBindings();
	
	public InputBindings getInputBindings() {
		return inputBindings;
	}
	
	public StateManager getStateManager() {
		return sm;
	}
	
	public String getSettingsFileName() {
		return "settings";
	}
	
}
