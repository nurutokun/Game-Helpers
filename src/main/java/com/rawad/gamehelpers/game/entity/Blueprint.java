package com.rawad.gamehelpers.game.entity;

public class Blueprint {
	
	private final Class<? extends Component>[] components;
	
	public Blueprint(Class<? extends Component>[] classes) {
		this.components = classes;
	}
	
	public Class<? extends Component>[] getComponents() {
		return components;
	}
	
}
