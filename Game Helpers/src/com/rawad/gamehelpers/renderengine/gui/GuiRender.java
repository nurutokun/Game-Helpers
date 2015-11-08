package com.rawad.gamehelpers.renderengine.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.gui.overlay.OverlayManager;
import com.rawad.gamehelpers.renderengine.Render;
import com.rawad.gamehelpers.renderengine.gui.overlay.OverlayRender;

public class GuiRender extends Render {
	
	private OverlayRender overlayRender;
	
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	public GuiRender() {
		
		overlayRender = new OverlayRender();
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		if(guiManager != null) {
			renderComponents(g, guiManager.getComponents());
		}
		
		if(overlayManager != null) {	
			renderOverlayManager(g, overlayManager);
		}
		
	}
	
	private void renderComponents(Graphics2D g, ArrayList<GuiComponent> components) {
		
		for(GuiComponent comp: components) {
			
			if(comp.shouldRender()) {
				comp.render(g);
			}
			
		}
		
	}
	
	public void renderOverlayManager(Graphics2D g, OverlayManager overlayManager) {
		
		ArrayList<Overlay> overlays = overlayManager.getOverlays();
		
		for(Overlay overlay: overlays) {
			
			if(!overlay.shouldRender()) continue;
			
			overlayRender.render(g, overlay);
			
			renderComponents(g, overlay.getGuiManager().getComponents());
			
		}
		
	}
	
	public void setGuiManager(GuiManager guiManager) {
		this.guiManager = guiManager;
	}
	
	public void setOverlayManager(OverlayManager overlayManager) {
		this.overlayManager = overlayManager;
	}
	
}
