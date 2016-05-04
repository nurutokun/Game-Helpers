package com.rawad.gamehelpers.game.entity;

import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

public class Entity {
	
	private HashMap<Class<? extends Component>, Component> components;
	
	public Entity() {
		components = new HashMap<Class<? extends Component>, Component>();
	}
	
	public void addComponent(Component comp) {
		components.put(comp.getClass(), comp);
	}
	
	public <T extends Component> T getComponent(Class<T> compClass) {
		return Util.cast(components.get(compClass));
	}
	
}
