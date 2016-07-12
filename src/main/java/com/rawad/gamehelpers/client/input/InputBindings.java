package com.rawad.gamehelpers.client.input;

import com.rawad.gamehelpers.utils.MultiMap;
import com.rawad.gamehelpers.utils.Util;

public class InputBindings {
	
	private final MultiMap<Object, AInput> bindings;
	
	/** Gets returned instead of {@code null}. */
	private Object defaultAction;
	
	public InputBindings() {
		super();
		
		bindings = new MultiMap<Object, AInput>();
		
	}
	
	public void put(Object action, AInput input) {
		bindings.put(action, input);
		if(defaultAction == null) defaultAction = action;
	}
	
	public Object get(AInput input) {
		return Util.getNullSafe(bindings.getKey(input), defaultAction);
	}
	
	public MultiMap<Object, AInput> getBindingsMap() {
		return bindings;
	}
	
	public void setDefaultAction(Object defaultAction) {
		this.defaultAction = defaultAction;
	}
	
}
