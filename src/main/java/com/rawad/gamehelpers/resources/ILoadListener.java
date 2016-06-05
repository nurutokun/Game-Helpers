package com.rawad.gamehelpers.resources;

public interface ILoadListener <T extends Resource> {
	
	public void onLoad(T resource);
	
}
