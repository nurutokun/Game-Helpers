package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ClassMap;

public class StateManager {
	
	private ClassMap<State> states;
	
	private State currentState;
	
	private StateChangeRequest stateChangeRequest;
	
	private Game game;
	
	private StateChangeListener stateChangeListener;
	
	public StateManager(Game game, StateChangeListener stateChangeListener) {
		super();
		
		states = new ClassMap<State>();
		
		stateChangeRequest = null;
		
		this.game = game;
		
		this.stateChangeListener = stateChangeListener;
		
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
	
	/**
	 * Renders {@link StateManager#currentState} while synchronizing on 
	 * {@link com.rawad.gamehelpers.game.World#getEntities()} of {@link StateManager#game}.
	 */
	public void render() {
		
		synchronized(currentState.getGameEngine().getEntities()) {
			currentState.getMasterRender().render();
		}
		
	}
	
	public void terminate() {
		
		if(currentState != null) currentState.onDeactivate();
		
		currentState = null;
		stateChangeRequest = null;
		
	}
	
	public void addState(State state) {
		
		states.put(state);
		
		state.setStateManager(this);
		state.setGame(game);
		
		state.init();
		
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
	
	/**
	 * This will deactivate the current {@code State}, if it is not null, and will set the new {@code State} to be the one 
	 * specified by {@link #stateChangeRequest}) and activates it.
	 * 
	 * @param stateChangeRequest
	 */
	public void setState(StateChangeRequest stateChangeRequest) {
		
		Class<? extends State> stateId = stateChangeRequest.getRequestedState();
		
		try {
			
			State newState = states.get(stateId);
			
			if(currentState == null) {
				currentState = newState;
			} else {
				currentState.onDeactivate();
			}
			
			// Definitely submit the onStateChange event here.
			stateChangeListener.onStateChange(currentState, newState);
			
			setCurrentState(newState);
			
			game.setGameEngine(currentState.getGameEngine());
			
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
	 * 
	 * @param state
	 */
	private void setCurrentState(State state) {
		this.currentState = state;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public ClassMap<State> getStates() {
		return states;
	}
	
}
