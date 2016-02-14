package com.rawad.gamehelpers.game;

public interface IController {
	
	public void tick();
	
	/**
	 * Should be called internally by this controller.
	 */
	void handleMouseInput();
	
	/**
	 * Should be called internally by this controller.
	 */
	void handleKeyboardInput();
	
}
