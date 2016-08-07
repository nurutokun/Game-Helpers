package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.world.World;
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
	
	public abstract void initGui();
	
	protected void onActivate() {}
	
	protected void onDeactivate() {}
	
	/**
	 * Called when this {@code State} is added to a {@code StateManager}.
	 * 
	 * @param sm The {@code StateManager} this {@code State} is added to.
	 * @param game
	 */
	public void init(StateManager sm, Game game) {
		this.sm = sm;
		this.game = game;
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
