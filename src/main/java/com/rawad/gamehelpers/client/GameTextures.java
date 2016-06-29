package com.rawad.gamehelpers.client;

import java.util.HashMap;

import com.rawad.gamehelpers.resources.TextureResource;

/**
 * A registry that binds objects to texture locations.
 * 
 * @author Rawad
 *
 */
public final class GameTextures {
	
	public static final Object GAME_ICON = new Object();// Reserved for game icon texture.
	
	private static final HashMap<Object, Integer> textures = new HashMap<Object, Integer>();
	
	private GameTextures() {}
	
	static {
		putTexture(GAME_ICON, TextureResource.UNKNOWN);// Initial value.
	}
	
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
