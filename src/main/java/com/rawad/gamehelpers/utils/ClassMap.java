package com.rawad.gamehelpers.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Could make this class Thread-safe if it needs to be.
 * 
 * @author Rawad
 *
 * @param <T>
 */
public class ClassMap<T> {
	
	private final Map<Class<? extends T>, T> map;
	
	public ClassMap() {
		super();
		
		map = new LinkedHashMap<Class<? extends T>, T>();
		
	}
	
	@SuppressWarnings("unchecked")
	public T put(T value) {
		return map.put((Class<? extends T>) value.getClass(), value);
	}
	
	public <K extends T> K get(Class<K> key) {
		return Util.cast(map.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It gets it from that "<K extends T>"; whatever K extends.
	}
	
	public Set<Class<? extends T>> keySet() {
		return map.keySet();
	}
	
	public Collection<T> values() {
		return map.values();
	}
	
	public void clear() {
		map.clear();
	}
	
}
