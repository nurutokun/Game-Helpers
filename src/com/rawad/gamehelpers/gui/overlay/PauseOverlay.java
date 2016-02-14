package com.rawad.gamehelpers.gui.overlay;

import java.awt.AWTKeyStroke;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.KeyStroke;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.rawad.gamehelpers.gui.Button;

public class PauseOverlay extends Overlay {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 2065458353079328026L;
	
	private Button btnResume;
	private Button btnMainMenu;
	
	public PauseOverlay() {
		super("Pause Overlay");
		
		initialize();
		
	}
	
	private void initialize() {

		btnResume = new Button("Resume");
		btnMainMenu = new Button("Main Menu");
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("pref:grow"),
				FormFactory.PREF_COLSPEC,
				ColumnSpec.decode("pref:grow"),},
			new RowSpec[] {
				RowSpec.decode("pref:grow"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("5dlu"),
				FormFactory.PREF_ROWSPEC,
				RowSpec.decode("pref:grow"),}));
		
		add(btnResume, "2, 2, fill, fill");
		add(btnMainMenu, "2, 4, fill, fill");
		
		Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>();
		Set<AWTKeyStroke> backwardTraversalKeys = new HashSet<AWTKeyStroke>();
		
		forwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false));
		backwardTraversalKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false));
		
		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardTraversalKeys);
		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardTraversalKeys);
		
	}
	
}
