package com.rawad.gamehelpers.game.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @author Rawad
 *
 */
public class EventManager {
	
	private LinkedList<Event> eventQueue = new LinkedList<Event>();
	
	private final HashMap<Object, ArrayList<Listener>> listeners = new HashMap<Object, ArrayList<Listener>>();
	
	/**
	 * Queues an {@code Event} for processing later on.
	 * 
	 * @param e
	 */
	public synchronized void queueEvent(Event e) {
		eventQueue.push(e);
	}
	
	/**
	 * Submits an {@code Event} for immediate processing.
	 * 
	 * @param e
	 */
	public void submitEvent(Event e) {
		this.processEvent(e);
	}
	
	// No amount of synchronization wwill help if a listener tries to queue an event while it responds to a queued event.
	public synchronized void processQueuedEvents() {
		
		// Could also do this: Check if empty. for(pop, check if empty, pop)
		for(Event e: eventQueue) {
			this.processEvent(e);
		}
		
		eventQueue.clear();
		
	}
	
	private void processEvent(Event e) {
		
		if(listeners.containsKey(e.getEventType())) {
			for(Listener l: listeners.get(e.getEventType())) {
				l.onEvent(e);
			}
		}
		
	}
	
	public void registerListener(Object eventType, Listener l) {
		
		ArrayList<Listener> compListeners = listeners.get(eventType);
		
		if(compListeners == null) {
			compListeners = new ArrayList<Listener>();
			listeners.put(eventType, compListeners);
		}
		
		compListeners.add(l);
		
	}
	
}
