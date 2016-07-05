package com.rawad.gamehelpers.resources;

import java.io.File;

public abstract class Resource {
	
	private File file;
	
	public Resource(String path) {
		this.file = new File(path);
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	protected File getFile() {
		return file;
	}
	
	public boolean exists() {
		return file.exists();
	}
	
	/**
	 * Should make changing which path is used (relative or absolute) very easily in the future.
	 */
	public abstract String getPath();
	
}
