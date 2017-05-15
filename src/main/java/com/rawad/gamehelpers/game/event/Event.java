package com.rawad.gamehelpers.game.event;

/**
 * 
 * @author Rawad
 *
 */
public abstract class Event {
	
	private final Object eventType;
	
	public Event(Object eventType) {
		super();
		
		this.eventType = eventType;
		
	}
	
	/**
	 * @return the eventType
	 */
	public final Object getEventType() {
		return eventType;
	}
	
}
