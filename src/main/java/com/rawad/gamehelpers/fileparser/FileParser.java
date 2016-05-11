package com.rawad.gamehelpers.fileparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

import com.rawad.gamehelpers.log.Logger;

public abstract class FileParser {
	
	/** Just a unified object for holding all the day from the parsed file(s). */
	protected HashMap<String, String> data;
	
	public FileParser() {
		
		data = new HashMap<String, String>();
		
	}
	
	public void parseFile(BufferedReader reader) throws IOException {
		
		start();
		
		for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				
			try {
				
				parseLine(line);
					
			} catch (Exception ex) {
				Logger.log(Logger.WARNING, "Coulnd't read line from file.");
//				ex.printStackTrace();
			}
			
		}
		
		stop();
		
	}
	
	/**
	 * Called every time a file is about to be parsed.
	 */
	protected void start() {}
	
	/**
	 * Called right after a file is done being parsed.
	 */
	protected void stop() {}
	
	protected abstract void parseLine(String line);
	
	public abstract String getContent();
	
}
