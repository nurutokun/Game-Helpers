package com.rawad.gamehelpers.files;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.rawad.gamehelpers.utils.Util;

public abstract class FileType {
	
	protected static final String SPLIT = "=";
	
	protected HashMap<String, String> data;
	
	public FileType() {
		
		data = new HashMap<String, String>();
		
	}
	
	public void parseData(ArrayList<String> lines) {
		
		for(int i = 0; i < lines.size(); i++) {
			
			String line = lines.get(i);
			
			String[] tokens = line.split(SPLIT);
			
			try {
				String key = tokens[0];
				String value = tokens[1];
				
				data.put(key, value);
				
				handleData(key, value);
				
			} catch(ArrayIndexOutOfBoundsException ex) {
				
				handleCustomLine(line, i);
				
			}
			
		}
		
	}
	
	/**
	 * Should be overrided by any subclass when they want to format a line in a way different from the standard: 
	 * key SPLIT value
	 * 
	 * @param line
	 * @param lineIndex
	 */
	protected void handleCustomLine(String line, int lineIndex) {}
	
	protected void handleData(String key, String value) {}
	
	/** 
	 * For saving the file.
	 * 
	 * @return
	 */
	public String getContent() {
		
		String content = "";// Util.getStringFromLines(lines.toArray(new String[lines.size()]), Util.NL, false);
		
		Iterator<String> keySet = data.keySet().iterator();
		
		for(int i = 0; i < data.size(); i++) {
			
			String key = keySet.next();
			String value = data.get(key);
			
			content += key + SPLIT + value;
			
			if(!(i >= data.size() - 1)) {
				content += Util.NL;
			}
			
		}
		
		return content;
		
	}
	
}
