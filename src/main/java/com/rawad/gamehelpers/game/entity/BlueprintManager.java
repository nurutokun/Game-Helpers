package com.rawad.gamehelpers.game.entity;

import java.util.HashMap;

public final class BlueprintManager {
	
	private static final HashMap<Object, Blueprint> blueprints = new HashMap<Object, Blueprint>();
	
	private BlueprintManager() {}
	
	public static void addBlueprint(Object blueprintId, Blueprint blueprint) {
		blueprints.put(blueprintId, blueprint);
	}
	
	public static Blueprint getBlueprint(Object blueprintId) {
		return blueprints.get(blueprintId);
	}
	
}
