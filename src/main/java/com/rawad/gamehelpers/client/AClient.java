package com.rawad.gamehelpers.client;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.TimeUnit;

import com.rawad.gamehelpers.client.gamestates.StateManager;
import com.rawad.gamehelpers.client.input.InputBindings;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public abstract class AClient extends Proxy {
	
	/** {@code frames} to wait before calculating {@code averageFps}. */
	private static final int FPS_SAMPLE_RATE = 30;
	
	protected Stage stage;
	
	protected Scene scene;
	
	protected StateManager sm;
	
	protected InputBindings inputBindings;
	
	protected boolean readyToRender;
	
	private Timeline renderingLoop;
	
	private int frames;
	private int targetFps;
	private int averageFps;
	
	public AClient() {
		frames = 0;
		targetFps = 60;
		
		scene = new Scene(new Pane(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);// Creates empty scene.
		
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
	
	private final KeyFrame getRenderingKeyFrame() {
		return new KeyFrame(Duration.ONE, new EventHandler<ActionEvent>() {
			
			private long totalTime = 0;
			
			private long currentTime = System.nanoTime();
			private long prevTime = currentTime;
			
			@Override
			public void handle(ActionEvent event) {
				
				if(!game.isRunning()) {
					renderingLoop.stop();
					return;
				}
				
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
						sm.getCurrentState().render();
						frames++;
					}
				}
				
			}
			
		});
	}
	
	/**
	 * Sets {@link #stage} to the given {@code stage} parameter. This method should be called before 
	 * {@link #init(Game)}.
	 * 
	 * @param stage
	 */
	public void initGui(Stage stage) {
		this.stage = stage;
		
		stage.setScene(scene);
		
	}
	
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
		
	}
	
	@Override
	public void init(Game game) {
		super.init(game);
		
		inputBindings = new InputBindings();
		
		initInputBindings();
		
		sm = new StateManager(game, scene);
		
		renderingLoop = new Timeline(targetFps);
		renderingLoop.setCycleCount(Timeline.INDEFINITE);
		
		renderingLoop.getKeyFrames().add(getRenderingKeyFrame());
		
		renderingLoop.playFromStart();
		
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
