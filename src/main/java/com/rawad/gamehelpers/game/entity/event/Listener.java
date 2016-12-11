package com.rawad.gamehelpers.game.entity.event;

@FunctionalInterface
public interface Listener {
	
	public void onEvent(Event ev);
	
}
