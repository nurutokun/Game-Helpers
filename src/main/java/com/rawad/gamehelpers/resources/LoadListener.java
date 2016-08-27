package com.rawad.gamehelpers.resources;

@FunctionalInterface
public interface LoadListener <T extends Resource> {
	
	public void onLoad(T resource);
	
}
