package com.rawad.gamehelpers.game.entity;

public interface IListener<T extends Component> {
	
	public void onEvent(Entity e, T component);
	
}
