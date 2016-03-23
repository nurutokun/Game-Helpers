package com.rawad.gamehelpers.gamestates;

import java.util.HashMap;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.Proxy;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public class StateManager {
	
	private HashMap<String, State> states;
	
	private State currentState;
	
	private Object requestedStateIdHolder;
	
	private Game game;
	
	private Proxy client;
	
	private boolean currentStateShowing;
	
	public StateManager(Game game) {
		
		states = new HashMap<String, State>();
		
		requestedStateIdHolder = null;
		
		this.game = game;
		
		this.client = game.getProxy();
		
		currentStateShowing = false;
		
	}
	
	/**
	 * Handles changing state, should be called in postTick().
	 */
	public void update() {// TODO: Fix this.
		
		if(requestedStateIdHolder != null) {
			
			State requestedState = states.get(requestedStateIdHolder.toString());
			
			if(requestedState != null && !requestedState.equals(currentState)) {
				setState(requestedState.getStateId());
			}
			
			requestedStateIdHolder = null;
			
		}
		
		try {
			
			if(currentState != null && currentStateShowing) {
				currentState.update();
			}
			
		} catch(Exception ex) {
//			Logger.log(Logger.WARNING, "Caught exception while trying to update state: " + currentState.getStateId());
			ex.printStackTrace();
		}
		
	}
	
	private void initializeState(State state) {
		
		state.initialize();
		
		DisplayManager.getContainer().add(state.container, state.getStateId());
		
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
			
			currentStateShowing = false;
			
			currentState = newState;
						
			Util.invokeAndWait(new Runnable() {
				
				@Override
				public void run() {
					
					if(!newState.isLoaded()) {
						initializeState(newState);
					}
					
					newState.onActivate();// Just so that this is called before any updating/rendering.
					
					DisplayManager.show(currentState.getStateId());
					
					currentStateShowing = true;
					
				}
				
			});
			
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
