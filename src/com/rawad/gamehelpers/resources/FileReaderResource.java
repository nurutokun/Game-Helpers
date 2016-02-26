package com.rawad.gamehelpers.resources;

import java.io.BufferedReader;

public class FileReaderResource extends Resource {
	
	protected BufferedReader reader;
	
	public FileReaderResource(String path, BufferedReader reader) {
		super(path);
		
		this.reader = reader;
		
	}
	
}
