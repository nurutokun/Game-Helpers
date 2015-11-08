package com.rawad.gamehelpers.gui.overlay;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class OverlayManager {
	
	private ArrayList<Overlay> overlays;
	
	private Button currentClickedButton;
	
	public OverlayManager() {
		
		overlays = new ArrayList<Overlay>();
		
	}
	
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		for(Overlay over: overlays) {
			over.update(me, ke);
		}
		
		for(Overlay over: overlays) {
			currentClickedButton = over.getClickedButton();
			
			if(currentClickedButton != null) {
				break;
			}
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(Overlay over: overlays) {
			
			if(over.shouldRender()) {
				over.render(g);
				over.getGuiManager().render(g);
			}
			
		}
		
	}
	
	public void addOverlay(Overlay over) {
		overlays.add(over);
	}
	
	public ArrayList<Overlay> getOverlays() {
		return overlays;
	}
	
	public Button getClickedButton() {
		return currentClickedButton;
	}
	
}
