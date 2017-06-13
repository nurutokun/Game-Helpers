package com.rawad.gamehelpers.client.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.AbstractLoader;

/**
 * @author Rawad
 *
 */
public class SoundEffectsManager {
	
	public static final Object SOUND_EVENT = new Object();
	
	private static final EventManager SOUND_EVENT_MANAGER = new EventManager();
	
	private static final Thread SOUND_THREAD = new Thread(() -> {
		
		while(true) {
			SOUND_EVENT_MANAGER.processQueuedEvents();
		}
		
	}, "Sound Thread");
	
	private static final HashMap<Object, Clip> SOUND_EFFECTS = new HashMap<Object, Clip>();
	
	static {
		
		SOUND_EVENT_MANAGER.registerListener(SOUND_EVENT, (Event e) -> {
			
			SoundEvent soundEvent = (SoundEvent) e;
			
			Clip soundEffect = SOUND_EFFECTS.get(soundEvent.getKey());
			
			if(soundEffect == null) {
				
				Logger.log(Logger.WARNING, "Sound effect not loaded. Key: %s.", soundEvent.getKey());
				
				return;
				
			}
			
			if(soundEffect.isRunning()) soundEffect.stop();
			
			soundEffect.setFramePosition(0);
			soundEffect.start();
			
		});
		
		SOUND_THREAD.setDaemon(true);
		SOUND_THREAD.start();
		
	}
	
	public static void loadSoundEffect(File soundEffectFile, Object key) {
		
		AbstractLoader.addTask(() -> {

			try {
				
				AudioInputStream inputStream = AudioSystem.getAudioInputStream(soundEffectFile);
				
				Clip clip = AudioSystem.getClip();
				
				clip.open(inputStream);
				
				SOUND_EFFECTS.put(key, clip);
				
			} catch(IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
				Logger.log(Logger.WARNING, "Error loading sound effect file: %s.", soundEffectFile.getName());
				ex.printStackTrace();
			}
			
		});
		
	}
	
	public static void playSoundEffect(Object key) {
		SOUND_EVENT_MANAGER.queueEvent(new SoundEvent(key));
	}
	
}
