package com.rawad.gamehelpers.fileparser.event;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

/**
 * @author Rawad
 *
 */
public class FileParseEvent extends Event {
	
	private final Entity e;
	private final Component comp;
	
	/**
	 * @param e
	 * @param comp
	 */
	public FileParseEvent(Entity e, Component comp) {
		super(FileParseEventType.FILE_PARSE);
		
		this.e = e;
		this.comp = comp;
		
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return e;
	}
	
	/**
	 * @return the comp
	 */
	public Component getComponent() {
		return comp;
	}
	
}
