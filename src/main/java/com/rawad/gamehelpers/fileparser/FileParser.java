package com.rawad.gamehelpers.fileparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public abstract class FileParser {
	
	/** Just a unified object for holding all the data from the parsed file(s), used by subclasses. */
	protected HashMap<String, String> data;
	
	public FileParser() {
		super();
		
		data = new HashMap<String, String>();
		
	}
	
	public void parseFile(BufferedReader reader) throws IOException {
		
		start();
		
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
			
			if(line.isEmpty()) continue;// For using comments, consider how to keep them when file is saved.
			
			try {
				
				parseLine(line);
				
			} catch (Exception ex) {
				Logger.log(Logger.WARNING, "Coulnd't read line from file.");
//				ex.printStackTrace();
			}
			
		}
		
		Util.silentClose(reader);
		
		stop();
		
	}
	
	/**
	 * Called before parsing a file.
	 */
	protected void start() {}
	
	/**
	 * Called after a file is done being parsed.
	 */
	protected void stop() {}
	
	protected abstract void parseLine(String line);
	
	public abstract String getContent();
	
}
