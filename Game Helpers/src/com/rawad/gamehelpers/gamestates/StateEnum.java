package com.rawad.gamehelpers.gamestates;

public enum StateEnum {
	
	GAME_STATE("Game State"),
	MENU_STATE("Menu State"),
	OPTION_STATE("Option State"),
	WORLDEDITOR_STATE("World Editor State"),
	MULTIPLAYERGAME_STATE("Multiplayer Game State");
	
	private final String id;
	
	private StateEnum(String id) {
		this.id = id;
	}
	
	public String getID() {
		return id;
	}
	
}
