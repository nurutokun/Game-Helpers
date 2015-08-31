package com.rawad.gamehelpers.game_manager;

import java.util.ArrayList;

import com.rawad.gamehelpers.display.DisplayManager;

public class GameManager {
	
	private static GameManager instance;
	
	private ArrayList<Game> games;
	
	private Game currentGame;
	
	private GameManager() {
		
		games = new ArrayList<Game>();
		
	}
	
	/**
	 * Registers the given {@code gameToLaunch} object, then it calls the {@code init()} method inherited in the {@code Game} class and starts
	 * the {@code DisplayManager}.
	 * 
	 * @param gameToLaunch
	 * @see #registerGame(Game)
	 */
	public void launchGame(Game gameToLaunch) {
		
		registerGame(gameToLaunch);
		
		// Make sure that this/another game isn't already running. Could maybe make this game-dependant so multiple different games can be
		// launched at once.
		if(!DisplayManager.isRunning()) {
			currentGame.init();
			
			DisplayManager.setDisplayMode(DisplayManager.Mode.WINDOWED);
		}
		
	}
	
	public Game getCurrentGame() {
		return currentGame;
	}
	
	public void onClose() {
		
	}
	
	/**
	 * Registers the given {@code game} object to the current list of games, if it isn't already, and sets the {@code currentGame} object to 
	 * this one.
	 * 
	 * @param game
	 */
	public void registerGame(Game game) {
		
		if(!games.contains(game)) {
			games.add(game);
		}
		
		currentGame = game;// Don't have to initialize game in order for this value to be set (for servers, mainly). Also in launchGame method
		
	}
	
	public Game[] getGames() {
		return games.toArray(new Game[games.size()]);
	}
	
	public static GameManager instance() {
		
		if(instance == null) {
			instance = new GameManager();
		}
		
		return instance;
		
	}
	
}
