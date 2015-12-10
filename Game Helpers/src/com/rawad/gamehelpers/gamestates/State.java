package com.rawad.gamehelpers.gamestates;

import java.awt.Graphics2D;

import com.rawad.gamehelpers.files.FileParser;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.GuiComponent;
import com.rawad.gamehelpers.gui.GuiManager;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.gui.overlay.OverlayManager;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public abstract class State {
	
	private final String stateId;
	
	private GuiManager guiManager;
	private OverlayManager overlayManager;
	
	protected StateManager sm;
	
	private MouseEvent me;
	private KeyboardEvent ke;
	
	/** Mainly for convenience. Other classes will have to get this instance straight from the game class. */
	protected FileParser fileParser;
	
	public State(String stateId) {
		this.stateId = stateId;
		
		guiManager = new GuiManager();
		overlayManager = new OverlayManager();
		
	}
	
	/**
	 * For convenience.
	 * 
	 * @param stateIdHolder
	 */
	public State(Object stateIdHolder) {
		this(stateIdHolder.toString());
	}
	
	/**
	 * Should be called by subclass whenever anything GUI-related is being done
	 */
	public final void update() {
		
		me = new MouseEvent();
		ke = new KeyboardEvent();
		
		this.update(me, ke);
		
	}
	
	/**
	 * Optional in case different values want to be used. e.g. when mouse is clamped, maybe use coordinates of an on-screen object.
	 * 
	 * @param me
	 * @param ke
	 * 
	 */
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		guiManager.update(me, ke);
		
		Button butt = guiManager.getCurrentClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
		}
		
		DropDown drop = guiManager.getCurrentSelectedDropDown();
		
		if(drop != null) {
			handleDropDownMenuSelect(drop);
			
		}
		
	}
	
	public void updateOverlays(MouseEvent me, KeyboardEvent ke) {
		
		overlayManager.update(me, ke);
		
		Button butt = overlayManager.getClickedButton();
		
		if(butt != null) {
			handleButtonClick(butt);
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
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
	
	protected void onActivate() {
//		Logger.log(Logger.DEBUG, "onActive(): " + toString());
	}
	
	protected void onDeactivate() {}
	
	protected final void addGuiComponent(GuiComponent comp) {
		
		guiManager.addComponent(comp);
		
	}
	
	protected final void addOverlay(Overlay overlay) {
		
		overlayManager.addOverlay(overlay);
		
	}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
		
		fileParser = sm.getGame().getFileParser();
		
	}
	
	public final String getStateId() {
		return stateId;
	}
	
	public GuiManager getGuiManager() {
		return guiManager;
	}
	
	public OverlayManager getOverlayManager() {
		return overlayManager;
	}
	
}
