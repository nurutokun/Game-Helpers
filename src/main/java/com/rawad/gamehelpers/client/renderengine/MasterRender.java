package com.rawad.gamehelpers.client.renderengine;

import com.rawad.gamehelpers.utils.ClassMap;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * Responsible for giving each {@code LayeredRender} its apropriate {@code Entity} objects before 
 * {@link #render(GraphicsContext, double, double)} gets called.
 * 
 * @author Rawad
 *
 */
public class MasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);// Has to be 0.0-1.0
	
	private ClassMap<LayerRender> renders;
	
	public MasterRender() {
		
		renders = new ClassMap<LayerRender>(true);
		
	}
	
	public void render(GraphicsContext g) {
		
		Affine affine = g.getTransform();
		
		for(LayerRender render: renders.getOrderedMap()) {
			render.render(g);
			
			g.setTransform(affine);
		}
		
	}
	
	public ClassMap<LayerRender> getRenders() {
		return renders;
	}
	
}
