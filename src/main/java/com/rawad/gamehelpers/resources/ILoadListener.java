package com.rawad.gamehelpers.resources;

@FunctionalInterface
public interface ILoadListener <T extends Resource> {
	
	public void onLoad(T resource);
	
}
