package com.rawad.gamehelpers.display;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import com.rawad.gamehelpers.input.EventHandler;

public class Windowed extends DisplayMode {
	
	private JFrame frame;
	
	public Windowed() {
		super();
	}
	
	@Override
	public void create(String displayTitle) {
		
		frame = new JFrame(displayTitle) {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 1814414469332125035L;
			
			@Override
			public boolean requestFocusInWindow() {
				return DisplayManager.getContainer().requestFocusInWindow();
				// Called by EventHandler whenever the window is activated; this passes it to the game container.
			}
			
		};
		
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		EventHandler l = EventHandler.instance();
		
		frame.addWindowListener(l);
		
		frame.setIgnoreRepaint(true);
		
	}
	
	@Override
	public void show() {
		
		frame.add(DisplayManager.getContainer(), BorderLayout.CENTER);
		
		setIcon(icon);// If it's null (when game first starts) nothing happens, otherwise it will be set.
		
		frame.pack();
//		frame.setLocationRelativeTo(null);// maybe...
		frame.setVisible(true);
		
	}
	
	@Override
	public void setIcon(BufferedImage icon) {
		super.setIcon(icon);
		
		if(icon != null && frame != null) {
			frame.setIconImage(icon);
			frame.revalidate();
		}
		
	}
	
	@Override
	public void destroy() {
		
		frame.dispose();
		
		frame = null;
		
	}
	
}
