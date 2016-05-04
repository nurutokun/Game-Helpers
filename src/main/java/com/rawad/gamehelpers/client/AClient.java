package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;
import com.rawad.gamehelpers.game.Proxy;

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
								IClientController controller = AClient.this.<IClientController>getController();
								Platform.runLater(() -> controller.render());
//								Platform.runLater(() -> controller.renderThreadSafe());
							} catch(NullPointerException ex) {
								// Have to catch this exception b/c multiple runLater calls can be made and the 
								// controller is set to null before they can be executed (when stopping).
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
	
	public String getSettingsFileName() {
		return "settings";
	}
	
}
