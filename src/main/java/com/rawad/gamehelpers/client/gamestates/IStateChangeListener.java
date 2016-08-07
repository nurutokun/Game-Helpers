package com.rawad.gamehelpers.client.gamestates;

@FunctionalInterface
public interface IStateChangeListener {
	
	public void onStateChange(State oldState, State newState);// TODO: Use in StateManager, make Client implement.
	
}
