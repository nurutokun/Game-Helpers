package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.World;
import com.rawad.gamehelpers.utils.ClassMap;

public abstract class State {
	
	/** Each {@code State} can have its own set of {@code GameSystem} objects; order of adding them is maintained. */
	protected final ClassMap<GameSystem> gameSystems;
	
	protected StateManager sm;
	
	protected Game game;
	
	protected MasterRender masterRender;
	
	protected World world;
	
	public State() {
		super();
		
		gameSystems = new ClassMap<GameSystem>();
		
		masterRender = new MasterRender(this);
		
		world = new World();
		
	}
	
	/**
	 * Called when this {@code State} is added to a {@code StateManager}. Calls the {@link State#initialize()} method
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
	public abstract void initialize();
	
	public abstract void terminate();
	
	protected abstract void onActivate();
	
	protected abstract void onDeactivate();
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public ClassMap<GameSystem> getGameSystems() {
		return gameSystems;
	}
	
	public MasterRender getMasterRender() {
		return masterRender;
	}
	
	public World getWorld() {
		return world;
	}
	
}
