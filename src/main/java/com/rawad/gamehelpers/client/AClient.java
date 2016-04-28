package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.game.Proxy;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

public abstract class AClient extends Proxy {
	
	protected Stage stage;
	
	private Thread renderingThread;
	
	private Task<Integer> renderingTask;
	
	@Override
	public <T extends IController> void setController(T controller) {
		super.setController(controller);
		
		if(renderingThread == null) {
			renderingThread = new Thread(getRenderingTask(), "Rendering Thread");
			renderingThread.setDaemon(true);
			renderingThread.start();
			
		}
		
	}
	
	public Stage getStage() {
		return stage;
	}
	
	private final Task<Integer> getRenderingTask() {
		
		if(renderingTask == null) {
			renderingTask = new Task<Integer>() {
				
				@Override
				protected Integer call() throws Exception {
					
					synchronized(renderingThread) {
						
						while(controller != null) {
							
							try {
								
								Platform.runLater(() -> {
									
									try {
										AClient.this.<IClientController>getController().render();
									} catch(NullPointerException ex) {
										// Have to catch this exception b/c multiple runLater calls can be made and the 
										// controller is set to null before they can be executed (when stopping).
										return;
									}
									
								});
								
								renderingThread.wait();
								
							} catch(InterruptedException ex) {
								ex.printStackTrace();
							}
							
						}
						
					}
					return 0;
					
				}
				
			};
		}
		
		return renderingTask;
		
	}
	
	public void render() {
		synchronized(renderingThread) {
			renderingThread.notify();
		}
	}
	
	@Override
	public void tick() {
		
		if(controller != null) {
			
			controller.tick();
			
			render();
			
		}
		
	}
	
	@Override
	public void stop() {
		
		render();// For resetting rendering thread (mainly for multi-game support.
		
	}
	
}
