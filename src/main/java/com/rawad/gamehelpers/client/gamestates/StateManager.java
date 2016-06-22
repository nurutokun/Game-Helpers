package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.Scene;

public class StateManager {
	
	private ClassMap<State> states;
	
	private State currentState;
	
	private Class<? extends State> requestedStateIdHolder;
	
	private Game game;
	
	private Scene scene;
	
	public StateManager(Game game, Scene scene) {
		
		states = new ClassMap<State>();
		
		requestedStateIdHolder = null;
		
		this.game = game;
		
		this.scene = scene;
		
	}
	
	/**
	 * Handles changing state, should be called in postTick().
	 */
	public void update() {
		
		if(requestedStateIdHolder != null) {
			
			State requestedState = states.get(requestedStateIdHolder);
			
			if(requestedState != null) {
				
				if(!requestedState.equals(currentState)) {
					setState(requestedState.getClass());
				}
				
				requestedStateIdHolder = null;// Allows it to wait until state has been initialized.
				
			}
			
		}
		
	}
	
	public void stop() {
		
		if(currentState != null) {
			currentState.onDeactivate();
		}
		
		currentState = null;
		requestedStateIdHolder = null;
		
	}
	
	protected void addState(State state) {
		states.put(state);
	}
	
	public void initGui() {
		
		for(State state: states.values()) {
			
			if(state.getRoot() == null) state.initGui();// Avoids re-initializing LoadingState.
			
		}
		
	}
	
	/**
	 * Changes to the provided <code>State</code>'s id upon the next <code>update()</code> as to allow it to be
	 * consistently called from the <code>Game Thread</code>.
	 * 
	 * @param stateIdHolder
	 */
	public void requestStateChange(Class<? extends State> stateIdHolder) {
		requestedStateIdHolder = stateIdHolder;
	}
	
	public void setState(Class<? extends State> stateId) {
		
		try {
			
			State newState = states.get(stateId);
			
			if(currentState != null) {
				currentState.onDeactivate();
				
				Transition transition = currentState.getTransition();
				transition.setOnFinished(e -> {
					scene.setRoot(currentState.getRoot());
					currentState.getRoot().requestFocus();
				});
				
				transition.playFromStart();
				
			} else {
				
				Platform.runLater(() -> {
					scene.setRoot(currentState.getRoot());
					currentState.getRoot().requestFocus();
				});
				
			}
			
			setState(newState);
			
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
