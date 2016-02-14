package com.rawad.gamehelpers.gamestates;

import java.util.HashMap;
import java.util.Set;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;

public class StateManager {
	
	private HashMap<String, State> states;
	
	private State currentState;
	
	private Object requestedStateIdHolder;
	
	private Game game;
	
	private Proxy client;
	
	public StateManager(Game game) {
		
		states = new HashMap<String, State>();
		
		requestedStateIdHolder = null;
		
		this.game = game;
		
		this.client = game.getProxy();
		
	}
	
	/**
	 * Handles changing state, should be called in postTick().
	 */
	public void update() {
		
		if(requestedStateIdHolder != null) {
			setState(requestedStateIdHolder.toString());
			
			requestedStateIdHolder = null;
			
		}
		
		try {
			currentState.update();
		} catch(Exception ex) {
			Logger.log(Logger.WARNING, "Caught exception while trying to update state: " 
					+ currentState.getStateId());
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
			
			DisplayManager.getContainer().add(state.container, state.getStateId());
			
		}
		
	}
	
	public void stop() {
		
		currentState.onDeactivate();
		
		currentState = null;
		requestedStateIdHolder = null;
		
	}
	
	public void addState(State state) {
		
		states.put(state.getStateId(), state);
		
//		currentState = state;// Note that these states aren't added to the game's container just yet.
		// So, they shouldn't be set as current state either.
		
		state.setStateManager(this);
		
	}
	
	/**
	 * Changes to the provided <code>State</code>'s id upon the next <code>update()</code> as to allow it to be
	 * consistently called from the <code>Game Thread</code>.
	 * 
	 * @param stateIdHolder
	 */
	public void requestStateChange(Object stateIdHolder) {
		requestedStateIdHolder = stateIdHolder;
	}
	
	private void setState(String stateId) {
		
		try {
			
			State newState = states.get(stateId);
			
			if(currentState != null) {
				currentState.onDeactivate();
			}
			
			game.getProxy().setController(null);
			
			newState.onActivate();// Just so that this is called before any updating/rendering.
			currentState = newState;
			
			DisplayManager.show(stateId);
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; "
					+ "probably wasn't created.");
			
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
	
	public Proxy getClient() {
		return client;
	}
	
}
