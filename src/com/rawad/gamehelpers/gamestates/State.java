package com.rawad.gamehelpers.gamestates;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.rawad.gamehelpers.display.RXCardLayout;
import com.rawad.gamehelpers.gui.Button;
import com.rawad.gamehelpers.gui.DropDown;
import com.rawad.gamehelpers.gui.overlay.Overlay;
import com.rawad.gamehelpers.input.EventHandler;
import com.rawad.gamehelpers.utils.Loadable;
import com.rawad.gamehelpers.utils.Util;

public abstract class State implements Loadable {
	
	protected final Listener listener = new Listener();
	
	private final String stateId;
	
	protected StateManager sm;
	
	protected CustomContainer container;
	
	private RXCardLayout cl;
	
	// TODO: Reorganize this whole class -> change 'initialize' to 'initGUI', seperate onActivate and onDeactivate to run on
	// separate threads from EDT (maybe have two versions, one on EDT other on loader thread; problem is, one on EDT might not
	// be used as often). Also, very important, make a dedicated 'Loader' thread that runs a constant loop (like EDT) and make
	// it so that different classes can push Runnable objects to it to be executed (e.g. loading a file) -> could have it run
	// by Util, but should probably make a separate class for it.
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
	 * <b>Note:</b> Called in <code>postTick()</code>, use to render scene (by controller) and perhaps other things.
	 */
	protected void update() {}
	
	protected void handleButtonClick(Button butt) {}
	
	protected void handleDropDownMenuSelect(DropDown drop) {}
	
	/**
	 * Should be responsible for initializing anything GUI related; will be called from the Swing EDT.
	 */
	protected void initialize() {
		
		container = new CustomContainer();
		
		container.addComponentListener(EventHandler.instance());// Makes sense here because only one state is showing at a time;
		// but also allows for better control than if it were bound to SuperContainer
		
		cl = new RXCardLayout();
		container.setLayout(cl);
		
		addListener(container);
		
	}
	
	/**
	 * This method is totally EDT-safe.
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
	
	/**
	 * 
	 * Checks the <code>container</state> object to see if this <code>State</code> is loaded or not.
	 * 
	 * @return Whether or not this <code>State</code> is loaded or not.
	 */
	public boolean isLoaded() {
		return container != null;
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
			
//			setIgnoreRepaint(true);
			setFocusable(false);
			
		}
		
		@Override
		protected void addImpl(Component comp, Object constraints, int index) {
			super.addImpl(comp, constraints, index);
			
			addListener(comp);
			
		}
		
	}
	
}
