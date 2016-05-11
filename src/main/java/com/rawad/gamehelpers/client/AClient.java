package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class AClient extends Proxy {
	
	protected Stage stage;
	
	private Thread renderingThread;
	
	private Runnable renderingRunnable;
	
	@Override
	public <T extends IController> void setController(T controller) {
		super.setController(controller);
		
		if(renderingThread == null) {
			renderingThread = new Thread(getRenderingRunnable(), "Rendering Thread");
			renderingThread.setDaemon(true);
			renderingThread.start();
			
		}
		
	}
	
	public Stage getStage() {
		return stage;
	}
	
	private final Runnable getRenderingRunnable() {
		
		if(renderingRunnable == null) {
			renderingRunnable = () -> {
					
				synchronized(renderingThread) {
					
					while(controller != null) {
						
						try {
							
							try {
								Platform.runLater(() -> render());
//								Platform.runLater(() -> controller.renderThreadSafe());
							} catch(NullPointerException ex) {
								// Have to catch this exception b/c multiple runLater calls can be made and the 
								// controller is set to null before they can be executed (when stopping).
								Logger.log(Logger.WARNING, "Got null controller when rendering.");
								break;
							}
							
							renderingThread.wait();
							
						} catch(InterruptedException ex) {
							ex.printStackTrace();
						}
						
					}
					
				}
				
			};
		}
		
		return renderingRunnable;
		
	}
	
	public void requestRender() {
		synchronized(renderingThread) {
			renderingThread.notify();
		}
	}
	
	@Override
	public void tick() {
		
		if(controller != null) {
			
			controller.tick();
			
			requestRender();
			
		}
		
	}
	
	/**
	 * JavaFX thread safe (for now).
	 */
	protected abstract void render();
	
	@Override
	public void stop() {
		
		setController(null);
		
		requestRender();// For resetting rendering thread.
		
	}
	
	public String getSettingsFileName() {
		return "settings";
	}
	
}
