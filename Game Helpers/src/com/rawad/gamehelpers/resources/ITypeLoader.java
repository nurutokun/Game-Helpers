package com.rawad.gamehelpers.resources;

//gamedev.stackexchange.com/questions/34051/how-should-i-structure-an-extensible-asset-loading-system
public interface ITypeLoader {
	
	public Resource load(String resourceFileName);
	
}
