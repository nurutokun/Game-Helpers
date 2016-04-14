package com.rawad.gamehelpers.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rawad.gamehelpers.utils.Util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class MasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);// Has to be 0.0-1.0
	
	private Map<Class<? extends LayeredRender>, LayeredRender> renders;
	private ArrayList<LayeredRender> iterableRenders;
	
	public MasterRender() {
		
		renders = new HashMap<Class<? extends LayeredRender>, LayeredRender>();
		iterableRenders = new ArrayList<LayeredRender>();
		
	}
	
	public void render(GraphicsContext g, double scaleX, double scaleY) {
		
		Affine affine = g.getTransform();
		
		g.scale(scaleX, scaleY);
		
		for(LayeredRender render: iterableRenders) {
			
			render.render(g);
			
		}
		
		g.setTransform(affine);
		
	}
	
	/**
	 * Binds the given {@code render} to the given {@code key} value.
	 * 
	 * @param key
	 * @param render
	 */
	public void registerRender(Class<? extends LayeredRender> key, LayeredRender render) {
		renders.put(key, render);
		iterableRenders.add(render);
	}
	
	/**
	 * Use default {@code class} variable of the given {@code render}.
	 * 
	 * @param render
	 */
	public void registerRender(LayeredRender render) {
		this.registerRender(render.getClass(), render);
	}
	
	public <T extends LayeredRender> T getRender(Class<T> key) {
		return Util.cast(renders.get(key));
	}
	
}
