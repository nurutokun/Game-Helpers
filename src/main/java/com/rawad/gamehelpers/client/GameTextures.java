package com.rawad.gamehelpers.client;

import java.util.HashMap;

/**
 * A registry that binds objects to texture locations.
 * 
 * @author Rawad
 *
 */
public final class GameTextures {
	
	private static final HashMap<Object, Integer> textures = new HashMap<Object, Integer>();
	
	private GameTextures() {}
	
	public static void putTexture(Object id, Integer textureLocation) {
		textures.put(id, textureLocation);
	}
	
	/**
	 * @param id {@code Object} the texture is bound to.
	 * @return
	 * 
	 */
	public static Integer findTexture(Object id) {
		return textures.get(id);
	}	
}
