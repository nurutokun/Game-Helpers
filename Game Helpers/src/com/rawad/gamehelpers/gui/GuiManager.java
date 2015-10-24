package com.rawad.gamehelpers.gui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class GuiManager {
	
	private ArrayList<GuiComponent> components;
	private ArrayList<Button> buttons;
	private ArrayList<DropDown> dropDowns;
	
	private Button currentClickedButton;
	private DropDown currentSelectedDropDown;
	
	public GuiManager() {
		
		components = new ArrayList<GuiComponent>();
		buttons = new ArrayList<Button>();
		dropDowns = new ArrayList<DropDown>();
		
	}
	
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		for(int i = components.size() - 1; i >= 0; i--) {// Makes a lot more sense now: components added the latest are
			// rendered on top; need to be updated before ones added lower on the list.
			
			GuiComponent comp = components.get(i);
			
			if(comp.shouldUpdate()) {
				comp.update(me, ke);
				
			}
			
		}
		
		// Sadly, needs to be done this way.
		for(Button butt: buttons) {
			
			if(butt.isClicked()) {
				currentClickedButton = butt;
				
				butt.setClicked(false);
				
				break;
			}
			
			currentClickedButton = null;
			
		}
		
		for(DropDown drop: dropDowns) {
			
			if(drop.isClicked()) {
				
				drop.setClicked(false);
				
				if(drop.isMenuDown()) {
					// Tell the drop down menu to select an item.
					drop.calculateSelectedItem(me.getY());
					drop.setMenuDown(false);
					
					currentSelectedDropDown = drop;
					
					break;
					
				} else {
					drop.setMenuDown(true);
				}
				
			}
			
			currentSelectedDropDown = null;
			
		}
		
	}
	
	public void render(Graphics2D g) {
		
		for(GuiComponent comp: components) {
			
			if(comp.shouldRender()) {
				comp.render(g);
			}
			
		}
		
	}
	
	public void addComponent(GuiComponent comp, int index) {
		
		// Appends new components to beginning of list so that they are prioritized and are easily loop-able that way.
		if(comp instanceof DropDown) {
			
			DropDown drop = (DropDown) comp;
			
			dropDowns.add(0, drop);
			
		} else if(comp instanceof Button) {
			
			Button butt = (Button) comp;
			
			buttons.add(0, butt);
			
		}
		
		// Aren't added backwards for updating purposes
		components.add(index, comp);
		
		comp.onAdd(this);// TODO: Last in should be last rendered/first updated but isn't when added like this...
		
	}
	
	public void addComponent(GuiComponent comp) {
		
		int size = components.size();
		int index = size;// <= 0? 0:size - 1;// So, basically, this last part is pointless.
		
		this.addComponent(comp, index);// Appends it to end by default
		
	}
	
	public void addComponents(ArrayList<? extends GuiComponent> comps) {
		
		for(GuiComponent comp: comps) {
			addComponent(comp);
		}
		
	}
	
	public ArrayList<GuiComponent> getComponents() {
		return components;
	}
	
	public Button getCurrentClickedButton() {
		return currentClickedButton;
	}
	
	public DropDown getCurrentSelectedDropDown() {
		return currentSelectedDropDown;
	}
	
}
