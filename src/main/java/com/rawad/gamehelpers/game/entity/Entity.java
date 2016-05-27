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
	
	public static Entity createEntity(Entity e, Object blueprintId) {
		
		Entity newEntity = createEntity(blueprintId);// Creates new Entity with empty components.
		
		Entity.copyComponentData(newEntity, e);
		
		return newEntity;
	}
	
	/**
	 * Creates a new {@code Entity} with all the components defined by the {@code blueprintId} with no additional data.
	 * 
	 * @param blueprintId
	 * @return
	 */
	public static Entity createEntity(Object blueprintId) {
		Entity e = new Entity();
		
		if(blueprintId == null) return e;
		
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
	
	public static void copyComponentData(Entity entityToCopyTo, Entity entityToCopyFrom) {
		
		for(Class<? extends Component> compClass: entityToCopyTo.components.keySet()) {
			// Only loop through components we need which is are that in the target entity.
			
			Component compToCopyTo = entityToCopyTo.getComponent(compClass);
			
			Component compToCopyFrom = entityToCopyFrom.getComponent(compClass);
			
			if(compToCopyFrom != null) compToCopyFrom.copyData(compToCopyTo);
			
		}
		
	}
	
	public static boolean compare(Entity e1, Entity e2) {
		
		if(e1.components.keySet().size() != e2.components.keySet().size()) {
			return false;// Small optimization (hopefully).
		}
		
		return contains(e1, e2.components.keySet());
		
	}
	
	/**
	 * Returns {@code false} if the number of component in {@code e} is not equal to the length of {@code comps}. To get
	 * around this, use the other method {@link Entity#contains(Entity, Collection)}.
	 * 
	 * @param e
	 * @param comps
	 * @return
	 */
	public static boolean compare(Entity e, Class<? extends Component>[] comps) {
		
		if(e.components.keySet().size() != comps.length) {
			return false;// Small optimization (hopefully).
		}
		
		ArrayList<Class<? extends Component>> list = new ArrayList<Class<? extends Component>>();
		
		for(Class<? extends Component> comp: comps) {
			list.add(comp);
		}
		
		return contains(e, list);
	}
	
	/**
	 * 
	 * @param e
	 * @param comps
	 * @return {@code true} if {@code e} contains all the {@code Component} types contained in {@code comps}; {@code false} 
	 * 			otherwise.
	 */
	public static boolean contains(Entity e, Collection<Class<? extends Component>> comps) {
		
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
