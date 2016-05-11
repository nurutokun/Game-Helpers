package com.rawad.gamehelpers.game.entity;

import java.util.ArrayList;

public class Blueprint {
	
	private final ArrayList<Component> components;
	
	public Blueprint(Component... components) {
		this.components = new ArrayList<Component>();
		
		for(Component comp: components) {
			this.components.add(comp);
		}
		
	}
	
	public ArrayList<Component> getComponents() {
		return components;
	}
	
}
