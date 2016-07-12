package com.rawad.gamehelpers.utils;

import java.util.HashMap;
import java.util.Set;

import com.rawad.gamehelpers.log.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
	
	private final HashMap<K, ObservableList<V>> map;
	/** Binds every value to a single key. Allows for easily checking if a value is bound or not. */
	private final HashMap<V, K> singleMap;
	
	public MultiMap() {
		super();
		
		map = new HashMap<K, ObservableList<V>>();
		singleMap = new HashMap<V, K>();
		
	}
	
	public void put(K key, V value) {
		
		ObservableList<V> values = map.get(key);
		
		if(values == null) {
			values = FXCollections.<V>observableArrayList();
			map.put(key, values);
		}
		
		if(!values.contains(value)) {
			values.add(value);
			singleMap.put(value, key);
		} else {
			Logger.log(Logger.DEBUG, value + " already bound to key " + key + "; values: " + values);
		}
		
		
	}
	
	public boolean remove(K key, V value) {
		ObservableList<V> values = get(key);
		
		boolean removed = values.remove(value);
		
		if(removed) singleMap.remove(value);
		
		return removed;
	}
	
	public ObservableList<V> get(K key) {
		return map.get(key);// Can be null. Should be dealth with externally, though.
	}
	
	public K getKey(V value) {
		return singleMap.get(value);
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}
	
}
