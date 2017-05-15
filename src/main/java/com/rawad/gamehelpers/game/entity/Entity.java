package com.rawad.gamehelpers.game.entity;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;

import com.rawad.gamehelpers.utils.ClassMap;

public final class Entity {
	
	private ClassMap<Component> components;
	
	private Entity() {
		super();
		
		components = new ClassMap<Component>();
		
	}
	
	/**
	 * 
	 * @param comp
	 * @return {@code this} to allow for easily chaining multiple calls to this method.
	 */
	public Entity addComponent(Component comp) {
		components.put(comp);
		return this;
	}
	
	public <T extends Component> T getComponent(Class<T> compClass) {
		return components.get(compClass);
	}
	
	public ClassMap<Component> getComponents() {
		return components;
	}
	
	public static Entity createEntity(Entity e, Object blueprintId) {
		
		Entity newEntity = Entity.createEntity(blueprintId);// Creates new Entity with empty components.
		
		Entity.copyComponentData(newEntity, e);
		
		return newEntity;
		
	}
	
	/**
	 * Creates an empty {@code Entity} object.
	 * 
	 * @return New {@code Entity} instance.
	 */
	public static Entity createEntity() {
		return new Entity();
	}
	
	/**
	 * Creates a new {@code Entity} with all the components defined by the {@code blueprintId} with no additional data.
	 * 
	 * @param blueprintId
	 * @return
	 */
	public static Entity createEntity(Object blueprintId) {
		
		Entity e = Entity.createEntity();
		
		if(blueprintId == null) return e;
		
		Entity entityBase = BlueprintManager.getBlueprint(blueprintId).getEntityBase();
		
		for(Component baseComp: entityBase.getComponents().values()) {
			
			try {
				
				Component newComp = baseComp.getClass().getConstructor().newInstance();
				e.addComponent(newComp);
				
				baseComp.copyData(newComp);
				
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException ex) {
				ex.printStackTrace();
				continue;
			}
			
		}
		
		return e;
	}
	
	public static void copyComponentData(Entity entityToCopyTo, Entity entityToCopyFrom) {
		
		for(Class<? extends Component> compClass: entityToCopyTo.getComponents().keySet()) {
			// Only loop through components we need which is are that in the target entity.
			
			Component compToCopyTo = entityToCopyTo.getComponent(compClass);
			
			Component compToCopyFrom = entityToCopyFrom.getComponent(compClass);
			
			if(compToCopyFrom != null) compToCopyFrom.copyData(compToCopyTo);
			
		}
		
	}
	
	public static boolean compare(Entity e1, Entity e2) {
		
		if(e1.getComponents().keySet().size() != e2.getComponents().keySet().size()) {
			return false;// Small optimization (hopefully).
		}
		
		return contains(e1, e2.getComponents().keySet());
		
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
		
		if(e.getComponents().keySet().size() != comps.length) {
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
	 * @return {@code true} if {@code e} contains all the {@code Component} types contained in {@code comps};
	 * 			{@code false} otherwise.
	 */
	public static boolean contains(Entity e, Collection<Class<? extends Component>> comps) {
		return e.getComponents().keySet().containsAll(comps);
	}
	
	@Override
	public String toString() {
		
		final String original = super.toString();
		final String regex = ".";
		
		return original.substring(original.lastIndexOf(regex) + regex.length());
	}
	
}
