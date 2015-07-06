package com.rawad.gamehelpers.gamestates;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.gui.overlay.OverlayManager;
import com.rawad.gamehelpers.input.MouseEvent;
import com.rawad.gamehelpers.input.MouseInput;

public abstract class State {
	
	private final StateEnum stateType;
	
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	protected StateManager sm;
	
	private MouseEvent e;
	
	public State(StateEnum stateType) {
		this.stateType = stateType;
		
		guiManager = new GuiManager();
		overlayManager = new OverlayManager();
		
	}
	
	/**
	 * Should be called by subclass whenever anything GUI-related is being done
	 */
	public void update() {
		
		e = new MouseEvent(MouseInput.getX(), MouseInput.getY(), MouseInput.LEFT_MOUSE_BUTTON);
		
		this.update(e);
		
	}
	
	/**
	 * Optional in case different values want to be used. e.g. when mouse is clamped, maybe use coordinates of an on-screen object.
	 * 
	 * @param x
	 * @param y
	 * @param mouseButtonDown
	 */
	public void update(MouseEvent e) {
		
		guiManager.update(e);
		
		Button butt = guiManager.getCurrentClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
		}
		
		DropDown drop = guiManager.getCurrentSelectedDropDown();
		
		if(drop != null) {
			handleDropDownMenuSelect(drop);
			
		}
		
	}
	
	public void updateOverlays(MouseEvent e) {
		
		overlayManager.update(e);
		
		Button butt = overlayManager.getClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		guiManager.render(g);
		
	}
	
	/**
	 * Should be inherited to implement component clicking
	 * 
	 * @param comp
	 */
	protected void handleMouseClick(GuiComponent comp) {}
	
	/**
	 * Should be inherited to implement component hovering
	 * 
	 * @param comp
	 */
	protected void handleMouseHover(GuiComponent comp) {}
	
	protected void handleButtonClick(Button butt) {}
	
	protected void handleDropDownMenuSelect(DropDown drop) {}
	
	protected void onActivate() {}
	
	protected final void addGuiComponent(GuiComponent comp) {
		
		guiManager.addComponent(comp);
		
	}
	
	protected final void addOverlay(Overlay overlay) {
		
		overlayManager.addOverlay(overlay);
		
	}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public final StateEnum getStateType() {
		return stateType;
	}
	
}
