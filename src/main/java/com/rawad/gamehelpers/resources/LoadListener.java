package com.rawad.gamehelpers.resources;

public interface LoadListener <T extends Resource> {
	
	public void onLoad(T resource);
	
}
