package com.rawad.gamehelpers.client.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.rawad.gamehelpers.resources.AbstractLoader;

/**
 * @author Rawad
 *
 */
public class SoundLoader extends AbstractLoader {
	
	/**
	 * 
	 */
	public SoundLoader(String... base) {
		super();
	}
	
	public Clip loadSoundEffect(String soundName, String soundFormat) {
		
		try {
			
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(getFilePathFromParts(soundFormat, soundName)));
			
			Clip clip = AudioSystem.getClip();
			
			clip.open(inputStream);
			
			return clip;
			
		} catch(IOException | UnsupportedAudioFileException | LineUnavailableException ex) {
			ex.printStackTrace();
		}
		
		return null;
		
	}
	
}
