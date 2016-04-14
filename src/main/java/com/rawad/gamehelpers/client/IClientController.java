package com.rawad.gamehelpers.client;

import com.rawad.gamehelpers.game.IController;

public interface IClientController extends IController {
	
	/**
	 * JavaFX Thread safe; so drawing on a <code>Canvas</code> actually works.
	 */
	public void render();
	
}
