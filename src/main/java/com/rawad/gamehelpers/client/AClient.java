package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.game.Proxy;

import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class AClient implements Proxy {
	
	protected IClientController controller;
	
	protected Stage stage;
	
	protected Thread renderingThread;
	
	public void setController(IClientController controller) {
		this.controller = controller;
		
		if(renderingThread == null) {
			renderingThread = getRenderingThread();
			renderingThread.start();
			
		}
		
	}
	
	@Override
	public IController getController() {
		return controller;
	}
	
	public Stage getStage() {
		return stage;
	}
	
	private final Thread getRenderingThread() {
		return new Thread(() -> {
			
			synchronized(renderingThread) {
				
				while(controller != null) {
					
					try {
						
						Platform.runLater(() -> {
							
							try {
								controller.render();
							} catch(NullPointerException ex) {
								// Have to catch this exception b/c multiple runLater calls can be made and the controller
								// is set to null before they can be executed (when stopping).
								return;
							}
							
						});
						
						renderingThread.wait();
						
					} catch(InterruptedException ex) {
						ex.printStackTrace();
					}
					
				}
				
			}
			
		}, "Rendering Thread");
	}
	
	public abstract void initResources();
	
}
