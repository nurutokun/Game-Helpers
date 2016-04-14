package com.rawad.gamehelpers.gamestates;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.resources.TextureResource;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadingState extends State {
	
	private Task<Integer> loadingTask;
	
	@FXML private ProgressBar progressBar;
	@FXML private Label loadingProgressLabel;
	
	public LoadingState(final Class<? extends State> redirectState) {
		super();
		
		loadingTask = new Task<Integer>() {
			
			/**
			 * Note: Return value is not used.
			 * 
			 * @return Irrelevant
			 */
			@Override
			protected Integer call() throws Exception {
				
				String message = "Initializing client resources...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				sm.getClient().initResources();
				
				message = "Registering unknown texture...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				ResourceManager.registerUnkownTexture();
				
				message = "Loading textures...";
				
				updateMessage(message);
				Logger.log(Logger.DEBUG, message);
				
				int progress = 0;
				
				for(TextureResource texture: ResourceManager.getRegisteredTextures().values()) {
					
					message = "Loading \"" + texture.getPath() + "\"...";
					updateMessage(message);
					ResourceManager.loadTexture(texture);
					
					updateProgress(progress++, ResourceManager.getRegisteredTextures().size());
					
				}
				
				message = "Done!";
				Logger.log(Logger.DEBUG, message);
				
				sm.requestStateChange(redirectState);
				
				return 0;
				
			}
		};
		
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		progressBar.progressProperty().bind(loadingTask.progressProperty());
		loadingProgressLabel.textProperty().bind(loadingTask.messageProperty());
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		Thread thread = new Thread(loadingTask, "Loader Thread");
		thread.setDaemon(true);
		thread.start();
		
	}
	
}
