package com.rawad.gamehelpers.client.states;

import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameEngine;
import com.rawad.gamehelpers.game.event.EventManager;

public abstract class State {
	
	protected StateManager sm;
	
	protected Game game;
	
	protected final GameEngine gameEngine;
	
	protected final EventManager eventManager;// This is purely for convenience.
	
	protected MasterRender masterRender;
	
	public State() {
		super();
		
		gameEngine = new GameEngine();
		
		this.eventManager = gameEngine.getEventManager();
		
		masterRender = new MasterRender(this);
		
	}
	
	/**
	 * Called when this {@code State} is added to a {@code StateManager}. Calls the {@link State#init()} method
	 * overriding this method unnecessary.
	 * 
	 * @param sm The {@code StateManager} this {@code State} is added to.
	 * @param game The {@code Game} this {@code State} is associated with.
	 */
	protected final void onAdd(StateManager sm, Game game) {
		this.sm = sm;
		this.game = game;
	}
	
	/**
	 * Called to initialize this {@code State}, usually right after it is added to a {@code StateManager}.
	 */
	public abstract void init();
	
	public abstract void terminate();
	
	protected abstract void onActivate();
	
	protected abstract void onDeactivate();
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
	}
	
	/**
	 * @return the gameEngine
	 */
	public GameEngine getGameEngine() {
		return gameEngine;
	}
	
}
