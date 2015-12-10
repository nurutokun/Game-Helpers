package com.rawad.gamehelpers.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import com.rawad.gamehelpers.log.Logger;

public class FileParser {
	
	public FileParser() {
		
	}
	
	/**
	 * Fills {@code infoHolder} with the provided data.
	 * 
	 * @param infoHolder
	 * @param reader
	 */
	public void parseFile(FileType infoHolder, BufferedReader reader) {
		
		ArrayList<String> lines = new ArrayList<String>();
		
		try {
			
			for(String line = reader.readLine(); line != null; line = reader.readLine()) {
				
				lines.add(line);
				
			}
			
		} catch (IOException ex) {
			Logger.log(Logger.WARNING, "Coulnd't read line from file.");
			ex.printStackTrace();
		}
		
		infoHolder.parseData(lines);
		
	}
	
//	protected abstract void parseLine(String line);
	
}
