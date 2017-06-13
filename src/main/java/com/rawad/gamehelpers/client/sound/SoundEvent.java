package com.rawad.gamehelpers.client.sound;

import com.rawad.gamehelpers.game.event.Event;

/**
 * @author Rawad
 *
 */
public class SoundEvent extends Event {
	
	private final Object key;
	
	/**
	 * @param key
	 */
	public SoundEvent(Object key) {
		super(SoundEffectsManager.SOUND_EVENT);
		
		this.key = key;
		
	}
	
	/**
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}
	
}
