package com.rawad.gamehelpers.game.entity;

public class Blueprint {
	
	private final Entity entityBase;
	
	public Blueprint(Entity entityBase) {
		this.entityBase = entityBase;
	}
	
	public Entity getEntityBase() {
		return entityBase;
	}
	
}
