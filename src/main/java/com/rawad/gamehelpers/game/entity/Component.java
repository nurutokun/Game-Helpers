package com.rawad.gamehelpers.game.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * This class represent either a chunk of data or simply a marker for use the appropriate {@code GameSystem}.
 * 
 * @see com.rawad.gamehelpers.game.GameSystem
 * 
 * @author Rawad
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Component {
	
	/**
	 * Default constructor such that subclasses don't have to declare it but is required by {@code Entity}. Subclasses should
	 * avoid redeclaring this (mainly for neat-ness) and just use setters instead and declaring defaults in-line.
	 */
	public Component() {}// TODO: Consider making private.
	
	/**
	 * 
	 * 
	 * @param comp {@code Component} to copy data from {@code this} into.
	 * @return {@code comp} with copied data.
	 */
	public abstract Component copyData(Component comp);
	
}
