package com.rawad.gamehelpers.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Thin wrapper for the {@code HashMap} implementation allowing for multiple values to be bounds to a single {@code key}.
 * 
 * @author Rawad
 *
 * @param K
 * @param V
 *
 */
public class MultiMap<K, V> {
	
	private final HashMap<K, ArrayList<V>> map;
	/** Binds every value to a single key. Allows for easily checking is a value is bound or not. */
	private final HashMap<V, K> singleMap;
	
	public MultiMap() {
		super();
		
		map = new HashMap<K, ArrayList<V>>();
		singleMap = new HashMap<V, K>();
		
	}
	
	public void put(K key, V value) {
		
		ArrayList<V> values = map.get(key);
		
		if(values == null) {
			values = new ArrayList<V>();
			map.put(key, values);
		}
		
		values.add(value);
		
		singleMap.put(value, key);
		
	}
	
	public boolean remove(K key, V value) {
		ArrayList<V> values = get(key);
		return values.remove(value);
	}
	
	public ArrayList<V> get(K key) {
		return map.get(key);// Can be null. Should be dealth with externally, though.
	}
	
	public K getKey(V value) {
		return singleMap.get(value);
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}
	
}
