package com.rawad.gamehelpers.client.states;

@FunctionalInterface
public interface StateChangeListener {
	
	public void onStateChange(State oldState, State newState);// TODO: Use in StateManager, make Client implement.
	
}
