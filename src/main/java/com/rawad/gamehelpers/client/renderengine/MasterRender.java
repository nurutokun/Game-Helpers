package com.rawad.gamehelpers.client.renderengine;

import com.rawad.gamehelpers.client.gamestates.State;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.scene.paint.Color;

/**
 * Calls {@link LayeredRender#render()} for giving each {@code LayeredRender} from the {@link #render()} method.
 * 
 * @author Rawad
 *
 */
public class MasterRender {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.DARKGRAY;//new Color(202, 212, 227, 25);// Has to be 0.0-1.0
	
	private State state;
	
	private ClassMap<LayerRender> renders;
	
	public MasterRender(State state) {
		super();
		
		this.state = state;
		
		renders = new ClassMap<LayerRender>();
		
	}
	
	public void render() {
		
		for(LayerRender render: renders.values()) {
			
			render.setMasterRender(this);
			
			render.render();
			
		}
		
	}
	
	public State getState() {
		return state;
	}
	
	public ClassMap<LayerRender> getRenders() {
		return renders;
	}
	
}
