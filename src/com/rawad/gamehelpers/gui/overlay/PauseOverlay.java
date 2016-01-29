package com.rawad.gamehelpers.gui.overlay;

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
		
	}
	
}
