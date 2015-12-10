package com.rawad.gamehelpers.resources;

import java.util.HashMap;

import com.rawad.gamehelpers.utils.Util;

public final class ResourceSystem {
	
	private HashMap<Class<? extends ITypeLoader>, ITypeLoader> loaders;
	
	public ResourceSystem() {
		
		loaders = new HashMap<Class<? extends ITypeLoader>, ITypeLoader>();
		
	}
	
	public <K extends ITypeLoader> K getLoader(Class<K> key) {
		return Util.cast(loaders.get(key));
	}
	
}
