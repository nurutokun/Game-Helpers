package com.rawad.gamehelpers.gamestates;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.utils.Util;

public abstract class State {
	
	protected final Listener listener = new Listener();
	
	private final String stateId;
	
	protected StateManager sm;
	
	protected CustomContainer container;
	
	private CardLayout cl;// Create custom add() and show() methods that will run the cl method on EDT.
	
	public State(String stateId) {
		super();
		
		this.stateId = stateId;
		
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
	 * Note: Called after client is updated, so logic for that can be done here.
	 * 
	 */
	protected void update() {}
	
	protected void handleButtonClick(Button butt) {}
	
	protected void handleDropDownMenuSelect(DropDown drop) {}
	
	/**
	 * Should be responsible for initializing anything GUI related; will be called from the Swing EDT.
	 */
	protected void initialize() {
		
		container = new CustomContainer();
		
		cl = new CardLayout();
		container.setLayout(cl);
		
		addListener(container);
		
	}
	
	/**
	 * This method is NOT EDT-safe.
	 * 
	 */
	protected void onActivate() {}
	
	protected void onDeactivate() {}
	
	protected final void addOverlay(Overlay overlay) {
		
		container.add(overlay, overlay.getId());
		
		addListener(overlay);
		
	}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	public final String getStateId() {
		return stateId;
	}
	
	protected final void show(final String name) {
		
		Util.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				cl.show(container, name);
			}
			
		});
		
	}
	
	/**
	 * Add {@code else if} statements as more {@code Listener} objects are required. Also adds listeners to childs
	 * of containers. Could also add key listeners.
	 * 
	 * @param comp
	 */
	protected void addListener(Component comp) {

		if(comp instanceof Button) {
			((Button) comp).addActionListener(listener);
		} else if(comp instanceof DropDown) {
			((DropDown) comp).addActionListener(listener);
		}
		
		if(comp instanceof Container) {
			
			Component[] components = ((Container) comp).getComponents();
			
			for(Component c: components) {
				
				addListener(c);
				
			}
		}
		
	}
	
	private class Listener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			Object source = e.getSource();
			
			if(source instanceof Button) {
				handleButtonClick((Button) source);
			} else if(source instanceof DropDown) {
				handleDropDownMenuSelect((DropDown) source);
			}
			
		}
		
	}
	
	protected class CustomContainer extends JPanel {
		
		/**
		 * Generated serial version UID.
		 */
		private static final long serialVersionUID = -7152604465120519057L;
		
		public CustomContainer() {
			super();
			
			setIgnoreRepaint(true);
			
		}
		
		@Override
		protected void addImpl(Component comp, Object constraints, int index) {
			super.addImpl(comp, constraints, index);
			
			addListener(comp);
			
		}
		
	}
	
}
