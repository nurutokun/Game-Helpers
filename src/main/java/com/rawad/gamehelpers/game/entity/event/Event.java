package com.rawad.gamehelpers.game.entity.event;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

/**
 * Represents an in-game event that occurs between two {@code Entity} objects.
 * 
 * @author Rawad
 *
 */
public final class Event {
	
	private final Entity entity;
	private final Class<? extends Component> compClass;
	
	public Event(Entity entity, Class<? extends Component> compClass) {
		super();
		
		this.entity = entity;
		this.compClass = compClass;
		
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * @return the comp class
	 */
	public Class<? extends Component> getCompClass() {
		return compClass;
	}
	
}
