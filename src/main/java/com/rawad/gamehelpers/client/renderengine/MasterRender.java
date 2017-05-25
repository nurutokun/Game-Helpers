package com.rawad.gamehelpers.client.renderengine;

import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.utils.ClassMap;

/**
 * Calls {@link LayeredRender#render()} for giving each {@code LayeredRender} from the {@link #render()} method.
 * 
 * @author Rawad
 *
 */
public class MasterRender {
	
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
	
	/**
	 * @return the renders
	 */
	public ClassMap<LayerRender> getRenders() {
		return renders;
	}
	
	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}
	
}
