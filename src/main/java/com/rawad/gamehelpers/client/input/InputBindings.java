package com.rawad.gamehelpers.client.input;

import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class InputBindings {
	
	private HashMap<MouseButton, Object> mouseBindings;
	private HashMap<KeyCode, Object> keyBindings;// Don't use KeyCombination because it would complicate getting key.
	
	/** Gets returned instead of {@code null}. */
	private Object defaultBinding;
	
	public InputBindings() {
		mouseBindings = new HashMap<MouseButton, Object>();
		keyBindings = new HashMap<KeyCode, Object>();
	}
	
	public void put(MouseButton button, Object value) {
		mouseBindings.put(button, value);
		
		if(defaultBinding == null) defaultBinding = value;
		
	}
	
	public Object get(MouseButton button) {
		return mouseBindings.get(button) == null? defaultBinding:mouseBindings.get(button);
	}
	
	public void put(KeyCode key, Object value) {
		keyBindings.put(key, value);
		
		if(defaultBinding == null) defaultBinding = value;
		
	}
	
	public Object get(KeyCode key) {
		return keyBindings.get(key) == null? defaultBinding:keyBindings.get(key);
	}
	
	public void setDefaultBinding(Object defaultBinding) {
		this.defaultBinding = defaultBinding;
	}
	
}
