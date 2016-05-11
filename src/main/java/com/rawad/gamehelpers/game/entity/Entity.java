package com.rawad.gamehelpers.game.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

public final class Entity {
	
	private HashMap<Class<? extends Component>, Component> components;
	
	private Entity() {
		components = new HashMap<Class<? extends Component>, Component>();
	}
	
	public void addComponent(Component comp) {
		components.put(comp.getClass(), comp);
	}
	
	public <T extends Component> T getComponent(Class<T> compClass) {
		return Util.cast(components.get(compClass));
	}
	
	public static Entity createEntity(Object blueprintId) {
		Entity e = new Entity();
		
		ArrayList<Component> components = BlueprintManager.getBlueprint(blueprintId).getComponents();
		
		for(Component comp: components) {
			
			e.addComponent(comp.clone());
			
		}
		
		return e;
	}
	
}
