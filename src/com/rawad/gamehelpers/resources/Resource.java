package com.rawad.gamehelpers.resources;

import java.io.File;

public abstract class Resource {
	
	private File path;
	
	public Resource(String path) {
		this.path = new File(path);
	}
	
	public String getPath() {
		return path.getAbsolutePath();
	}
	
	public boolean exists() {
		return path.exists();
	}
	
}
