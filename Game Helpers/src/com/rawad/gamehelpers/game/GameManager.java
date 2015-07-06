package com.rawad.gamehelpers.game;

public class GameManager {
	
	private static Game currentGame;
	
	public GameManager() {
		
	}
	
	public static void initGame(Game game) {
		GameManager.currentGame = game;
		
		GameManager.currentGame.init();
		
	}
	
	public static Game getGame() {
		return GameManager.currentGame;
	}
	
}
