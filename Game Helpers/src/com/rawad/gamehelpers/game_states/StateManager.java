package com.rawad.gamehelpers.game_states;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.log.Logger;

public class StateManager {
	
	private HashMap<String, State> states;
	
	private State currentState;
	
	public StateManager() {
		
		states = new HashMap<String, State>();
		
	}
	
	public void update() {
		
		try {
			currentState.update();
			
			KeyboardInput.clearTypedKeysBuffer();
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "Current state is null for updating");
		}
		
	}
	
	public void render(Graphics2D g) {
		
		try {
			currentState.render(g);
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "Current state is null for rendering");
		}
		
	}
	
	public void addState(State state) {
		
		states.put(state.getStateId(), state);
		
		currentState = state;
		
		state.setStateManager(this);
		
	}
	
	public void setState(Object stateIdHolder) {
		setState(stateIdHolder.toString());
	}
	
	public void setState(String stateId) {
		
		try {
			
			currentState = states.get(stateId);
			currentState.onActivate();
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; probably wasn't created.");
			
		}
		
	}
	
}
