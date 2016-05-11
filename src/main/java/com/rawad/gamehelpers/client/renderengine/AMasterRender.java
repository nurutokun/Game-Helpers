package com.rawad.gamehelpers.client.renderengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.rawad.gamehelpers.utils.Util;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

// TODO: Create a WritableImage as a buffer, pass all the renders an object that can draw actual shapes and images onto the
// WritableRaster of the buffer; then separate the rendering in AClient's rendering thread and the IClientController.
/**
 * Responsible for giving each {@code LayeredRender} its apropriate {@code Entity} objects before 
 * {@link #render(GraphicsContext, double, double)} gets called.
 * 
 * @author Rawad
 *
 */
public abstract class AMasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);// Has to be 0.0-1.0
	
	private Map<Class<? extends LayeredRender>, LayeredRender> renders;
	private ArrayList<LayeredRender> iterableRenders;
	
	public AMasterRender() {
		
		renders = new HashMap<Class<? extends LayeredRender>, LayeredRender>();
		iterableRenders = new ArrayList<LayeredRender>();
		
	}
	
	public void render(GraphicsContext g, Camera camera) {
		
		Affine affine = g.getTransform();
		
		g.setFill(DEFAULT_BACKGROUND_COLOR);
		g.fillRect(0, 0, camera.getCameraBounds().getWidth(), camera.getCameraBounds().getHeight());
		
		g.scale(camera.getScaleX(), camera.getScaleY());
		
		g.rotate(camera.getRotation());
		
		g.translate(-camera.getX(), -camera.getY());
		
		for(LayeredRender render: iterableRenders) {
			render.render(g);
		}
		
		g.setTransform(affine);
		
	}
	
	/**
	 * Registers the given {@code render} with the given {@code key} value.
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
		registerRender(render.getClass(), render);
	}
	
	public <T extends LayeredRender> T getRender(Class<T> key) {
		return Util.cast(renders.get(key));
	}
	
}
