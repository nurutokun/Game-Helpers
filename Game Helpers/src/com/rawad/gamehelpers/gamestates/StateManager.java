package com.rawad.gamehelpers.gamestates;

import java.awt.Graphics2D;
import java.util.HashMap;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.gui.GuiRender;

public class StateManager {
	
	private GuiRender guiRender;
	
	private HashMap<String, State> states;
	
	private State currentState;
	
	private Game game;
	
	public StateManager(Game game) {
		
		states = new HashMap<String, State>();
		
		this.game = game;
		
	}
	
	public void update() {
		
		try {
			currentState.update();
			
			guiRender.addGuiComponents(currentState.getGuiManager().getComponents());
			guiRender.addOverlays(currentState.getOverlayManager().getOverlays());
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "Current state is null for updating");
			ex.printStackTrace();
		}
		
	}
	
	public void render(Graphics2D g) {
		
		try {
			
			currentState.render(g);
			
			currentState.getGuiManager().render(g);
			currentState.getOverlayManager().render(g);
			
		} catch(NullPointerException ex) {
			Logger.log(Logger.DEBUG, "Current state is null for rendering");
		}
		
	}
	
	/**
	 * Should be called by each game, independantly, after (at least) the GuiRender has been registered.
	 * 
	 */
	public void init() {
		
		guiRender = (GuiRender) game.getMasterRender().getRender(GuiRender.class);
		
	}
	
	public void addState(State state) {
		
		states.put(state.getStateId(), state);
		
		currentState = state;
		
		state.setStateManager(this);
		
	}
	
	public void setState(Object stateIdHolder) {
		setState(stateIdHolder.toString());
	}
	
	public void setState(String stateId) {
		
		try {
			
			State newState = states.get(stateId);
			
			currentState.onDeactivate();
			
			newState.onActivate();// Just so that this is called before any updating/rendering.
			currentState = newState;
			
		} catch(Exception ex) {
			
			Logger.log(Logger.WARNING, "The state \"" + stateId + "\" couldn't be set as the active state; probably wasn't "
					+ "created.");
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
	
}
