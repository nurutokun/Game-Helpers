package com.rawad.gamehelpers.renderengine.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.TextContainer;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.renderengine.LayeredRender;
import com.rawad.gamehelpers.renderengine.gui.overlay.OverlayRender;
import com.rawad.gamehelpers.renderengine.text.TextRender;

public class GuiRender extends LayeredRender {
	
	private OverlayRender overlayRender;
	
	private TextRender textRender;
	
	private ArrayList<GuiComponent> components;
	private ArrayList<Overlay> overlays;
	
	public GuiRender() {
		
		overlayRender = new OverlayRender();
		
		textRender = TextRender.instance();
		
		components = new ArrayList<GuiComponent>();
		overlays = new ArrayList<Overlay>();
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		renderGuiComponents(g, components);
		
		renderOverlays(g, overlays);
		
		components.clear();
		overlays.clear();
		
	}
	
	public void renderGuiComponents(Graphics2D g, ArrayList<GuiComponent> components) {
		
		for(GuiComponent comp: components) {
			
			if(comp.shouldRender()) {
				comp.render(g);
				
				if(comp instanceof TextContainer) {
					
					TextContainer textComp = (TextContainer) comp;
					
					textRender.render(g, textComp.getTextObject(), textComp.getTextBoundBox());
					
				}
				
			}
			
		}
		
	}
	
	public void renderOverlays(Graphics2D g, ArrayList<Overlay> overlays) {
		
		for(Overlay overlay: overlays) {
			
			if(overlay.shouldRender()) {
				
				overlayRender.render(g, overlay);
				
				renderGuiComponents(g, overlay.getGuiManager().getComponents());
				
			}
			
		}
		
	}
	
	public void addGuiComponents(ArrayList<GuiComponent> components) {
		
		for(GuiComponent comp: components) {
			addGuiComponent(comp);
		}
		
	}
	
	public void addGuiComponent(GuiComponent comp) {
		components.add(comp);
	}
	
	public void addOverlays(ArrayList<Overlay> overlays) {
		
		for(Overlay overlay: overlays) {
			addOverlay(overlay);
		}
		
	}
	
	public void addOverlay(Overlay overlay) {
		overlays.add(overlay);
	}
	
}
