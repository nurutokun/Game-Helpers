package com.rawad.gamehelpers.display;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.input.EventHandler;

public class Windowed extends DisplayMode {
	
	private JFrame frame;
	
	public Windowed() {
		super();
	}
	
	public void create(Game game) {
		super.create(game);
		
		frame = new JFrame(game.toString()) {
			
			/**
			 * Generated serial version UID.
			 */
			private static final long serialVersionUID = 1814414469332125035L;
			
			@Override
			public boolean requestFocusInWindow() {
				return Windowed.this.game.getContainer().requestFocusInWindow();
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
		
		frame.add(game.getContainer());
		
		frame.setIconImage(game.getIcon());
		frame.pack();
//		frame.setLocationRelativeTo(null);// maybe...
		frame.setVisible(true);
		
		frame.repaint();// Just as an initial thing; could simply be replaced by the first state repainting initially.
		
	}
	
	@Override
	public void destroy() {
		
		frame.dispose();
		
		frame = null;
		
	}
	
}
