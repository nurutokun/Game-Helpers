package com.rawad.gamehelpers.game;

/**
 * Used to define either client or server.
 * 
 * @author Rawad
 *
 */
public abstract class  Proxy {
	
	private IController controller;
	
	public abstract void init(Game game);
	
	public abstract void initGUI();
	
	public abstract void tick();
	
	public abstract void stop();
	
	/**
	 * @return the controller
	 */
	public IController getController() {
		return controller;
	}
	
	/**
	 * @param controller the controller to set
	 */
	public void setController(IController controller) {
		this.controller = controller;
	}
	
}
