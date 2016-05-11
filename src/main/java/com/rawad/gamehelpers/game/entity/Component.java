package com.rawad.gamehelpers.game.entity;

/**
 * This class represent either a chunk of data or simply a marker for use the appropriate {@code GameSystem}.
 * 
 * @see com.rawad.gamehelpers.game.GameSystem
 * 
 * @author Rawad
 *
 */
public abstract class Component implements Cloneable {
	
	/**
	 * Default constructor such that subclasses don't have to declare it but is required by {@code Entity}. Subclasses should
	 * avoid redeclaring this (mainly for neat-ness) and just use setters instead and declaring defaults in-line.
	 */
	public Component() {}
	
	@Override
	public Component clone() {
		try {
			return (Component) super.clone();// Might have to add something special for non-primitive data (e.g. Rectangle)
		} catch (CloneNotSupportedException e) {
			return null;// Should not happen as long as Cloneable is implemented.
		}
	}
	
}
