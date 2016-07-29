package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ClassMap;

public class StateManager {
	
	private ClassMap<State> states;
	
	private State currentState;
	
	private StateChangeRequest stateChangeRequest;
	
	private Game game;
	
	public StateManager(Game game) {
		super();
		
		states = new ClassMap<State>();
		
		stateChangeRequest = null;
		
		this.game = game;
		
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
	
	public void addState(State state) {
		
		states.put(state);
		
		state.init(this);
		
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
			
			currentState.onDeactivate();
			
			State newState = states.get(stateId);
			
			setCurrentState(newState);
			
			game.getGameEngine().setGameSystems(currentState.getGameSystems());
			game.setWorld(currentState.getWorld());
			game.getWorld().clearEntities();
			
			currentState.onActivate();
			
		} catch(NullPointerException ex) {
			
			Logger.log(Logger.WARNING, "Was the State set initially before requesting to change to a different State?");
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; "
					+ "probably wasn't created.");
			
			ex.printStackTrace();
			
		}
		
	}
	
	/**
	 * Should be called before requesting a {@code State} change to set the intiial {@code State} value.
	 * 
	 * @param state
	 */
	public void setCurrentState(State state) {
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
	
	public ClassMap<State> getStates() {
		return states;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
}
