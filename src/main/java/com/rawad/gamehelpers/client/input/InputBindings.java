package com.rawad.gamehelpers.client.input;

import com.rawad.gamehelpers.utils.MultiMap;
import com.rawad.gamehelpers.utils.Util;

public class InputBindings {
	
	private final MultiMap<Object, Object> bindings;
	
	/** Gets returned instead of {@code null}. */
	private Object defaultBinding;
	
	public InputBindings() {
		super();
		
		bindings = new MultiMap<Object, Object>();
		
	}
	
	public void put(Object action, Object input) {
		bindings.put(action, input);
		if(defaultBinding == null) defaultBinding = action;
	}
	
	public Object get(Object input) {
		return Util.getNullSafe(bindings.getKey(input), defaultBinding);
	}
	
	public MultiMap<Object, Object> getBindingsMap() {
		return bindings;
	}
	
	public void setDefaultBinding(Object defaultBinding) {
		this.defaultBinding = defaultBinding;
	}
	
}
