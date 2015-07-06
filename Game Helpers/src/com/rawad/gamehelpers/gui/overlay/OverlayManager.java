package com.rawad.gamehelpers.gui.overlay;

import java.util.ArrayList;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.input.MouseEvent;

public class OverlayManager {
	
	private ArrayList<Overlay> overlays;
	
	private Button currentClickedButton;
	
	public OverlayManager() {
		
		overlays = new ArrayList<Overlay>();
		
	}
	
	public void update(MouseEvent e) {
		
		for(Overlay over: overlays) {
			over.update(e);
		}
		
		for(Overlay over: overlays) {
			currentClickedButton = over.getClickedButton();
			
			if(currentClickedButton != null) {
				break;
			}
			
		}
		
	}
	
	public void addOverlay(Overlay over) {
		overlays.add(over);
	}
	
	public Button getClickedButton() {
		return currentClickedButton;
	}
	
}
