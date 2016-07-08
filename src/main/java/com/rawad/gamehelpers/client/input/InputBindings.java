package com.rawad.gamehelpers.client.input;

import com.rawad.gamehelpers.utils.MultiMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputBindings {
	
	private MultiMap<Object, MouseButton> mouseBindings;
	private MultiMap<Object, KeyCode> keyBindings;// Don't use KeyCombination because it would complicate getting key.
	
	/** Gets returned instead of {@code null}. */
	private Object defaultBinding;
	
	public InputBindings() {
		super();
		
		mouseBindings = new MultiMap<Object, MouseButton>();
		keyBindings = new MultiMap<Object, KeyCode>();
		
	}
	
	public void put(Object action, MouseButton button) {
		mouseBindings.put(action, button);
		if(defaultBinding == null) defaultBinding = action;
	}
	
	public Object get(MouseButton value) {
		return mouseBindings.getKey(value) == null? defaultBinding:mouseBindings.getKey(value);
	}
	
	public MultiMap<Object, MouseButton> getMouseBindings() {
		return mouseBindings;
	}
	
	public void put(Object action, KeyCode code) {
		keyBindings.put(action, code);
		if(defaultBinding == null) defaultBinding = code;
	}
	
	public Object get(KeyCode value) {
		return keyBindings.getKey(value) == null? defaultBinding:keyBindings.getKey(value);
	}
	
	public MultiMap<Object, KeyCode> getKeybindings() {
		return keyBindings;
	}
	
	public void setDefaultBinding(Object defaultBinding) {
		this.defaultBinding = defaultBinding;
	}
	
}
