package com.rawad.gamehelpers.gamestates;

import java.util.HashMap;
import java.util.Set;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.log.Logger;

public class StateManager {
	
	private HashMap<String, State> states;
	
	private State currentState;
	
	private Game game;
	
	public StateManager(Game game) {
		
		states = new HashMap<String, State>();
		
		this.game = game;
		
	}
	
	public void update() {
		
		try {
			
			currentState.update();
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "Current state is null for updating");
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Everything GUI-initialization related is done here; this is called on the Swing EDT.
	 */
	public void initialize() {
		
		Set<String> stateIds = states.keySet();
		
		for(String stateId: stateIds) {
			
			State state = states.get(stateId);
			
			state.initialize();
			
			game.getContainer().add(state.container, state.getStateId());
			
		}
		
	}
	
	public void addState(State state) {
		
		states.put(state.getStateId(), state);
		
		currentState = state;// Note that these states aren't added to the game's container just yet.
		
		state.setStateManager(this);
		
	}
	
	public void setState(Object stateIdHolder) {
		setState(stateIdHolder.toString());
	}
	
	public void setState(String stateId) {
		
		try {
			
			State newState = states.get(stateId);
			
			currentState.onDeactivate();
			
			newState.onActivate();// Just so that this is called before any updating/rendering.
			currentState = newState;
			
			game.show(stateId);
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; probably wasn't "
					+ "created.");
			ex.printStackTrace();
			
		}
		
	}
	
	/**
	 * Mainly for convenience.
	 * 
	 * @return {@code Game} for which this {@code StateManager} is managing {@code State} objects for.
	 */
	public Game getGame() {
		return game;
	}
	
}
