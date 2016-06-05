package com.rawad.gamehelpers.fileparser.xml;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.rawad.gamehelpers.game.entity.Component;

@XmlRootElement(name="Entity")
@XmlSeeAlso(value={Component.class})
public class Components {
	
	private Collection<Component> components = new ArrayList<Component>();
	
	/**
	 * @return the components
	 */
	@XmlElement(name="comp")// Don't name this the same as the actual Component class.
	public Collection<Component> getComponents() {
		return components;
	}
	
	/**
	 * @param components the components to set
	 */
	public void setComponents(Collection<Component> components) {
		this.components = components;
	}
	
}
