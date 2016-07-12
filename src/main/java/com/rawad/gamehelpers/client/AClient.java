package com.rawad.gamehelpers.client;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ALoader;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.concurrent.Task;

public abstract class AClient extends Proxy {
	
	/** {@code frames} to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 30;
	
	protected StateManager sm;
	
	protected InputBindings inputBindings;
	
	protected Task<Void> loadingTask;
	
	protected boolean readyToRender;
	
	private Timer renderingTimer;
	
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
	
	private final TimerTask getRenderingTask() {
		return new TimerTask() {
			
			private long totalTime = 0;
			
			private long currentTime = System.nanoTime();
			private long prevTime = currentTime;
			
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
				
				if(readyToRender) {
					synchronized(game.getWorld().getEntities()) {
						render();
						frames++;
					}
				}
				
				if(!game.isRunning()) renderingTimer.cancel();
				
			}
			
		};
	}
	
	@Override
	public void tick() {
		sm.update();
	}
	
	/**
	 * This method called from {@link #preInit(Game)}.
	 */
	protected abstract void initGui();
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		Thread.currentThread().setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable ex) {
				Logger.log(Logger.SEVERE, "Uncaught Exception.");
				ex.printStackTrace();
			}
		});
		
		inputBindings = new InputBindings();
		
		initGui();
		
		loadingTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				
				String message = "Initializing client resources...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				try {
					
					initResources();
					
					for(com.rawad.gamehelpers.client.gamestates.State state: sm.getStates().values()) {
						state.initGui();
					}
				
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				message = "Loading textures...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				int progress = 0;
				
				for(TextureResource texture: ResourceManager.getRegisteredTextures().values()) {
					
					message = "Loading \"" + texture.getPath() + "\"...";
					updateMessage(message);
					
					ResourceManager.loadTexture(texture);
					
					updateProgress(++progress, ResourceManager.getRegisteredTextures().size());
					
				}
				
				readyToRender = true;
				
				message = "Done!";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				return null;
				
			}
		};
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		sm = new StateManager(game, this);
		
		renderingTimer = new Timer("Rendering Thread");
		renderingTimer.scheduleAtFixedRate(getRenderingTask(), 0, TimeUnit.SECONDS.toMillis(1) / targetFps);
		
		readyToRender = false;
		
		ALoader.addTask(loadingTask);
		
	}
	
	/**
	 * Called from the Loading Thread to initialize anything that might take a while. Note that 
	 * {@link com.rawad.gamehelpers.client.gamestates.State#initGui} is called immediately after this.
	 * 
	 */
	protected abstract void initResources();
	
	protected abstract void render();
	
	/**
	 * {@link StateManager#currentState} is set to the new {@code State} before calling this method.
	 * 
	 */
	public abstract void onStateChange();
	
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
