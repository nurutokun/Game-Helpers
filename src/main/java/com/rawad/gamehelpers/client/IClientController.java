package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;

public interface IClientController extends IController {
	
	/**
	 * <strike>Not JavaFX thread safe. Called from separate, rendering thread.</strike>
	 * <br/>
	 * 
	 * Is thread safe until further notice.
	 * 
	 * @see IClientController#renderThreadSafe
	 */
	public void render();
	
	/**
	 * JavaFX thread safe.
	 */
//	public void renderThreadSafe();
	
}
