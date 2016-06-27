package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.application.Platform;

public class StateManager {
	
	private ClassMap<State> states;
	
	private State currentState;
	
	private StateChangeRequest stateChangeRequest;
	
	private Game game;
	
	private AClient client;
	
	public StateManager(Game game, AClient client) {
		
		states = new ClassMap<State>();
		
		stateChangeRequest = null;
		
		this.game = game;
		
		this.client = client;
		
	}
	
	/**
	 * Handles changing state.
	 */
	public void update() {
		
		if(stateChangeRequest != null) {
			
			State requestedState = states.get(stateChangeRequest.getRequestedState());
			
			if(requestedState != null) {
				
				if(!requestedState.equals(currentState)) {
					setState(stateChangeRequest);
				}
				
				stateChangeRequest = null;// Allows it to wait until state has been initialized.
				
			}
			
		}
		
	}
	
	public void stop() {
		
		if(currentState != null) {
			currentState.onDeactivate();
		}
		
		currentState = null;
		stateChangeRequest = null;
		
	}
	
	protected void addState(State state) {
		states.put(state);
	}
	
	public void initGui() {
		
		for(State state: states.getMap().values()) {
			
			if(state.getRoot() == null) state.initGui();// Avoids re-initializing LoadingState.
			
		}
		
	}
	
	/**
	 * Changes to the provided {@code State}'s id upon the next {@link #update()} as to allow it to be
	 * consistently called from the {@code Game Thread}.
	 * 
	 * @param stateIdHolder
	 */
	public void requestStateChange(StateChangeRequest stateChangeRequest) {
		this.stateChangeRequest = stateChangeRequest;
	}
	
	public void setState(StateChangeRequest stateChangeRequest) {
		
		Class<? extends State> stateId = stateChangeRequest.getRequestedState();
		
		try {
			
			State newState = states.get(stateId);
			
			if(currentState != null) currentState.onDeactivate();
			
			setState(newState);
			
			client.onStateChange();
			
			Platform.runLater(() -> {
				 currentState.getRoot().requestFocus();
			});
			
			game.getGameEngine().setGameSystems(currentState.gameSystems);
			game.setWorld(currentState.getWorld());
			
			currentState.onActivate();
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; "
					+ "probably wasn't created.");
			
			ex.printStackTrace();
			
		}
		
	}
	
	public void setState(State state) {
		this.currentState = state;
	}
	
	/**
	 * Mainly for convenience.
	 * 
	 * @return {@code Game} for which this {@code StateManager} is managing {@code State} objects for.
	 */
	public Game getGame() {
		return game;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
}
