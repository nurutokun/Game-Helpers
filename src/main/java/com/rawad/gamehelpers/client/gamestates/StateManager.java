package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.client.AClient;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class StateManager {
	
	private ClassMap<State> states;
	
	private State currentState;
	
	private Class<? extends State> requestedStateIdHolder;
	
	private Game game;
	
	private AClient client;
	
	private Scene scene;
	
	public StateManager(Game game, AClient client) {
		
		states = new ClassMap<State>();
		
		requestedStateIdHolder = null;
		
		this.game = game;
		
		this.client = client;
		
		scene = new Scene(new Pane(), Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT);// Creates empty scene.
		
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
			}
			
			setState(newState);
			
			game.getGameEngine().setGameSystems(currentState.gameSystems);
			game.setWorld(currentState.getWorld());
			
			currentState.onActivate();
			
			Platform.runLater(() -> {
				client.getStage().getScene().setRoot(currentState.getRoot());
				currentState.getRoot().requestFocus();
			});
			
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
	
	public Scene getScene() {
		return scene;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
}
