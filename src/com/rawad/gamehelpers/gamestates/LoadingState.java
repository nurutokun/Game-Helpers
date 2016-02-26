package com.rawad.gamehelpers.gamestates;

import com.rawad.gamehelpers.gui.overlay.LoadingOverlay;
import com.rawad.gamehelpers.resources.ResourceManager;

public class LoadingState extends State {
	
	private Thread loader;
	
	private LoadingOverlay loadingScreen;
	
	public LoadingState(Runnable lengthyTask) {
		super(LoadingState.class);
		
		loader = new Thread(lengthyTask, "Loading Thread");
		
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		
		loadingScreen = new LoadingOverlay();
		
		addOverlay(loadingScreen);
		
	}
	
	double progress = 0;
	
	@Override
	protected void update() {
		super.update();
		
		loadingScreen.setProgress(ResourceManager.getPercentLoaded());
		
	}
	
	@Override
	protected void onActivate() {
		super.onActivate();
		
		show(loadingScreen.getId());
		
		loader.start();
		
	}
	
}
