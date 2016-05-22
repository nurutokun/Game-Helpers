package com.rawad.gamehelpers.client.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rawad.gamehelpers.utils.Util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

// TODO: Create a WritableImage as a buffer, pass all the renders an object that can draw actual shapes and images onto the
// WritableRaster of the buffer.
/**
 * Responsible for giving each {@code LayeredRender} its apropriate {@code Entity} objects before 
 * {@link #render(GraphicsContext, double, double)} gets called.
 * 
 * @author Rawad
 *
 */
public class MasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);// Has to be 0.0-1.0
	
	private Map<Class<? extends LayerRender>, LayerRender> renders;
	private ArrayList<LayerRender> iterableRenders;
	
	public MasterRender() {
		
		renders = new HashMap<Class<? extends LayerRender>, LayerRender>();
		iterableRenders = new ArrayList<LayerRender>();
		
	}
	
	public void render(GraphicsContext g) {
		
		Affine affine = g.getTransform();
		
		for(LayerRender render: iterableRenders) {
			render.render(g);
			
			g.setTransform(affine);
		}
		
	}
	
	/**
	 * Registers the given {@code render} with the given {@code key} value.
	 * 
	 * @param key
	 * @param render
	 */
	public void registerRender(Class<? extends LayerRender> key, LayerRender render) {
		renders.put(key, render);
		iterableRenders.add(render);
	}
	
	/**
	 * Use default {@code class} variable of the given {@code render}.
	 * 
	 * @param render
	 */
	public void registerRender(LayerRender render) {
		registerRender(render.getClass(), render);
	}
	
	public <T extends LayerRender> T getRender(Class<T> key) {
		return Util.cast(renders.get(key));
	}
	
}
