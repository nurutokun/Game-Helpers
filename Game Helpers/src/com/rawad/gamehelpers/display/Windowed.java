package com.rawad.gamehelpers.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.renderengine.MasterRender;

public class Windowed extends DisplayMode {
	
	private JFrame frame;
	private JPanel panel;
	
	public Windowed() {
		super();
	}
	
	@Override
	public void create(MasterRender render) {
		super.create(render);
		
		Game game = GameManager.instance().getCurrentGame();
		
		frame = new JFrame(game.toString());
		panel = new JPanel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7964464010671011714L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				int displayWidth = DisplayManager.getDisplayWidth() == 0? 1:DisplayManager.getDisplayWidth();
				int displayHeight = DisplayManager.getDisplayHeight() == 0? 1:DisplayManager.getDisplayHeight();
				
				g.setClip(0, 0, displayWidth, displayHeight);
				
				BufferedImage buffer = Windowed.this.render.getBuffer();
				
				g.drawImage(buffer.getScaledInstance(displayWidth, displayHeight, BufferedImage.SCALE_FAST),
						0, 0, null);
				
				g.dispose();
				
//				Windowed.this.render.clearBuffer();// Why this can't be done here, I don't know...
				
			}
			
		};
		
		panel.setPreferredSize(new Dimension(Game.SCREEN_WIDTH, Game.SCREEN_HEIGHT));
		
		frame.add(panel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		frame.addKeyListener(l);
		frame.addWindowListener(l);
		
		panel.addMouseListener(l);
		panel.addMouseMotionListener(l);
		panel.addMouseWheelListener(l);
		panel.addComponentListener(l);
		
		panel.setIgnoreRepaint(true);
		frame.setIgnoreRepaint(true);
		
		panel.setFocusTraversalKeysEnabled(false);// Important for the whole custom text components stuff.
		frame.setFocusTraversalKeysEnabled(false);
		
		frame.setIconImage(game.getIcon());
		frame.pack();
//		frame.setLocationRelativeTo(null);// maybe...
		frame.setVisible(true);
		
	}
	
	@Override
	public void destroy() {
		
		frame.dispose();
		
		frame = null;
		panel = null;
		
	}
	
	@Override
	public synchronized void repaint() {
		
		panel.repaint();
		
	}
	
	@Override
	public Component getCurrentWindow() {
		return panel;
	}
	
}
