package com.rawad.gamehelpers.game.entity;

@FunctionalInterface
public interface IListener<T extends Component> {
	
	public void onEvent(Entity e, T component);
	
}
