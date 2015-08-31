package com.rawad.gamehelpers.display;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rawad.gamehelpers.game_manager.Game;
import com.rawad.gamehelpers.game_manager.GameManager;
import com.rawad.gamehelpers.input.KeyboardInput;

public class Windowed extends DisplayMode {
	
	private JFrame frame;
	private JPanel panel;
	
	public Windowed() {
		super();
	}
	
	@Override
	public void create(Game game) {
		super.create(game);
		
		frame = new JFrame(GameManager.instance().getCurrentGame().toString());
		panel = new JPanel() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7964464010671011714L;
			
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				BufferedImage buffer = new BufferedImage(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight(),
						BufferedImage.TYPE_INT_ARGB);
				
				Graphics2D g2 = buffer.createGraphics();
				
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(DisplayManager.DEFAULT_BACKGROUND_COLOR);
				g2.fillRect(0, 0, DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight());
				
				Windowed.this.game.render(g2);
				
				int displayWidth = DisplayManager.getDisplayWidth() == 0? 1:DisplayManager.getDisplayWidth();
				int displayHeight = DisplayManager.getDisplayHeight() == 0? 1:DisplayManager.getDisplayHeight();
				
				g.setClip(0, 0, displayWidth, displayHeight);
				
				g.drawImage(buffer.getScaledInstance(displayWidth, displayHeight, BufferedImage.SCALE_FAST), 0, 0, null);
				
				buffer = null;
				
				g.dispose();
				g2.dispose();
			}
			
		};
		
		panel.setPreferredSize(new Dimension(DisplayManager.getScreenWidth(), DisplayManager.getScreenHeight()));
		
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
		
		if(KeyboardInput.isKeyDown(KeyEvent.VK_F11, true)) {
			DisplayManager.setDisplayMode(DisplayManager.Mode.FULLSCREEN);
			
			return;
		}
		
		panel.repaint();
		
	}
	
	@Override
	public Component getCurrentWindow() {
		return panel;
	}
	
}
