package com.rawad.gamehelpers.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Could make this class Thread-safe if it needs to be.
 * 
 * @author Rawad
 *
 * @param <T>
 */
public class ClassMap<T> {
	
	private final Map<Class<? extends T>, T> map;
	private final Map<Class<? extends T>, T> mapUnmodifiable;
	
	private List<T> orderedMap;
	private List<T> orderedMapUnmodifiable;
	
	private boolean saveOrder;
	
	public ClassMap(boolean saveOrder) {
		
		map = new HashMap<Class<? extends T>, T>();
		mapUnmodifiable = Collections.unmodifiableMap(map);
		
		this.saveOrder = saveOrder;
		
		if(saveOrder) {
			orderedMap = new ArrayList<T>();
			orderedMapUnmodifiable = Collections.unmodifiableList(orderedMap);
		}
		
	}
	
	public ClassMap() {
		this(false);
	}
	
	/**
	 * If {@link saveOrder} is {@code true} then this will add the {@code value} at the specified {@code index} to the
	 * {@link orderedMap}.
	 * 
	 * @param value
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T put(T value, int index) {
		
		T prevValue = map.put((Class<? extends T>) value.getClass(), value);
		
		if(saveOrder) orderedMap.add(index, value);
		
		return prevValue;
		
	}
	
	public T put(T value) {
		return put(value, map.size());
	}
	
	public <K extends T> K get(Class<K> key) {
		return Util.cast(map.get(key));// Now, how it knows what to cast the object to, not quite sure...
		// It gets it from that "<K extends T>"; whatever K extends.
	}
	
	public void clear() {
		map.clear();
		if(saveOrder) orderedMap.clear();
	}
	
	public Map<Class<? extends T>, T> getMap() {
		return mapUnmodifiable;
	}
	
	public List<T> getOrderedMap() {
		if(!saveOrder) throw new IllegalStateException("Can't return ordered map because order isn't being saved.");
		return orderedMapUnmodifiable;
	}
	
}