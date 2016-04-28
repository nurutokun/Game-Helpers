package com.rawad.gamehelpers.server;

import com.rawad.gamehelpers.game.Proxy;

public abstract class AServer extends Proxy {
	
	@Override
	public void tick() {
		controller.tick();
	}
	
}
