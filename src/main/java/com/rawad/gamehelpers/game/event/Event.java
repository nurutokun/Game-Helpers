package com.rawad.gamehelpers.game.event;

import com.rawad.gamehelpers.game.entity.Entity;

/**
 * 
 * @author Rawad
 *
 */
public final class Event {
	
	private final Object eventType;
	private final Entity entity;
	
	public Event(Object eventType, Entity entity) {
		super();
		
		this.eventType = eventType;
		this.entity = entity;
		
	}
	
	/**
	 * @return the eventType
	 */
	public Object getEventType() {
		return eventType;
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
}
