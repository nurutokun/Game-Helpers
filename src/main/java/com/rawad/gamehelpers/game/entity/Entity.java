package com.rawad.gamehelpers.game.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
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
		
		Class<? extends Component>[] components = BlueprintManager.getBlueprint(blueprintId).getComponents();
		
		for(Class<? extends Component> comp: components) {
			
			try {
				e.addComponent(comp.getConstructor().newInstance());// Might have to add something special for non-primitive
				// data (e.g. Rectangle)
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException ex) {
				ex.printStackTrace();
				continue;
			}
			
		}
		
		return e;
	}
	public static boolean compare(Entity e1, Entity e2) {
		
		if(e1.components.keySet().size() != e2.components.keySet().size()) {
			return false;// Small optimization (hopefully).
		}
		
		return compare(e1, e2.components.keySet());
		
	}
	
	public static boolean compare(Entity e, Class<? extends Component>[] comps) {
		
		if(e.components.keySet().size() != comps.length) {
			return false;// Small optimization (hopefully).
		}
		
		ArrayList<Class<? extends Component>> list = new ArrayList<Class<? extends Component>>();
		
		for(Class<? extends Component> comp: comps) {
			list.add(comp);
		}
		
		return compare(e, list);
	}
	
	public static boolean compare(Entity e, Collection<Class<? extends Component>> comps) {
		
		for(Class<? extends Component> compClazz: comps) {
			if(e.getComponent(compClazz) == null) return false;
		}
		
		return true;
		
	}
	
	@Override
	public String toString() {
		
		final String original = super.toString();
		final String regex = ".";
		
		return original.substring(original.lastIndexOf(regex) + regex.length());
	}
	
}
