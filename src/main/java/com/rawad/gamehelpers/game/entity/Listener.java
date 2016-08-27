package com.rawad.gamehelpers.game.entity;

@FunctionalInterface
public interface Listener<T extends Component> {
	
	public void onEvent(Entity e, T component);
	
}
