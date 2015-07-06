package com.rawad.gamehelpers.gui.overlay;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.input.MouseEvent;

public class PauseOverlay extends Overlay {
	
	private boolean paused;
	
	public PauseOverlay(Color backgroundColor, int x, int y, int width, int height) {
		super(backgroundColor, x, y, width, height);
		
		int centerX = this.width/2;
		int verticalSections = this.height/7;
		
		addComponent(new Button("Resume", centerX, verticalSections * 3));
		addComponent(new Button("Main Menu", centerX, verticalSections * 4));
		
//		alignComponents();
		
	}
	
	public PauseOverlay(Color backgroundColor, int x, int y) {
		this(backgroundColor, x, y, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
		
	}
	
	public PauseOverlay(Color backgroundColor) {
		this(backgroundColor, 0, 0);
		
	}
	
	public PauseOverlay(int x, int y, int width, int height) {
		this(Color.GRAY, x, y, width, height);
		
	}
	
	public PauseOverlay(int width, int height) {
		this(0, 0, width, height);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		
		if(isPaused()) {
			super.update(e);
		}
		
	}
	
	@Override
	public Button getClickedButton() {
		
		if(isPaused()) {
			return super.getClickedButton();
		} else {
			return null;
		}
		
	}
	
	public void alignComponents() {
		
		ArrayList<GuiComponent> comps = getGuiManager().getComponents();
		
		GuiComponent[] components = Arrays.copyOf(comps.toArray(), comps.size(), GuiComponent[].class);
		
		int x = this.width/2;
		
		final int sections = 7;
		int verticalSections = this.height/sections;// Could change.
		
		for(int i = 1; i <= components.length; i++) {
			
			GuiComponent comp = components[i-1];
			
			int index = 0;//(? i/-2:(i == 1? 1:i/2));
			
			if(i%2 == 0) {
				
				index = i/2;
				
			} else {
				
				index = i/-2;
				
			}
			
			int y = verticalSections * (sections/2 + index) + 32;
			
			comp.setX(x);
			comp.setY(y);
			
			comp.updateHitbox();
			
			x = this.width/2;
			
		}
		
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
}