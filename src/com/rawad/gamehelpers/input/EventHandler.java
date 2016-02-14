package com.rawad.gamehelpers.input;

import java.awt.Container;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.rawad.gamehelpers.display.DisplayManager;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameManager;

public class EventHandler implements MouseMotionListener, MouseListener, MouseWheelListener,
		KeyListener, ComponentListener, WindowListener {
	
	private static EventHandler instance;
	
	private EventHandler() {}
	
	public static EventHandler instance() {
		
		if(instance == null) {
			instance = new EventHandler();
		}
		
		return instance;
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		KeyboardInput.setKeyDown(e.getKeyCode(), true);
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		KeyboardInput.setKeyDown(e.getKeyCode(), false);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		char c = e.getKeyChar();
		
		if((int) c == KeyEvent.VK_ESCAPE) {// Stupid escape. Also, this works while e.getKeyCode() doesn't...
			return;
		}
		
		KeyboardInput.addTypedKey(c);
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Mouse.setMouseWheelPosition(e.getUnitsToScroll());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.isAltDown()) {
			Mouse.setButtonClicked(Mouse.MIDDLE_MOUSE_BUTTON, true);
			
		} else if(e.isMetaDown()) {
			Mouse.setButtonClicked(Mouse.RIGHT_MOUSE_BUTTON, true);
			
		} else {
			Mouse.setButtonClicked(Mouse.LEFT_MOUSE_BUTTON, true);
			
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.isAltDown()) {
			Mouse.setButtonDown(Mouse.MIDDLE_MOUSE_BUTTON, true);
		
		} else if(e.isMetaDown()) {
			Mouse.setButtonDown(Mouse.RIGHT_MOUSE_BUTTON, true);
		
		} else {
			Mouse.setButtonDown(Mouse.LEFT_MOUSE_BUTTON, true);
		
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.isAltDown()) {
			Mouse.setButtonDown(Mouse.MIDDLE_MOUSE_BUTTON, false);
			
		} else if(e.isMetaDown()) {
			Mouse.setButtonDown(Mouse.RIGHT_MOUSE_BUTTON, false);
			
		} else {
			Mouse.setButtonDown(Mouse.LEFT_MOUSE_BUTTON, false);
			
		}
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
		double xScale = (double) Game.SCREEN_WIDTH/(double) e.getComponent().getWidth();
		double yScale = (double) Game.SCREEN_HEIGHT/(double) e.getComponent().getHeight();
		
		Mouse.setX(e.getX());
		Mouse.setY(e.getY());
		
		Mouse.setScaleX(xScale);
		Mouse.setScaleY(yScale);;
		
		Mouse.setButtonClicked(Mouse.MIDDLE_MOUSE_BUTTON, false);
		Mouse.setButtonClicked(Mouse.RIGHT_MOUSE_BUTTON, false);
		Mouse.setButtonClicked(Mouse.LEFT_MOUSE_BUTTON, false);
		
	}
	
	@Override
	public void componentHidden(ComponentEvent e) {}
	
	@Override
	public void componentMoved(ComponentEvent e) {}
	
	@Override
	public void componentResized(ComponentEvent e) {
		
		DisplayManager.setDisplayWidth(e.getComponent().getWidth());
		DisplayManager.setDisplayHeight(e.getComponent().getHeight());
		
	}
	
	@Override
	public void componentShown(ComponentEvent e) {}
	
	@Override
	public void windowActivated(WindowEvent e) {
		requestFocus(e.getSource());
	}
	
	@Override
	public void windowClosed(WindowEvent e) {}
	
	@Override
	public void windowClosing(WindowEvent e) {
		GameManager.instance().getCurrentGame().requestStop();
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	@Override
	public void windowDeiconified(WindowEvent e) {}
	
	@Override
	public void windowIconified(WindowEvent e) {}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	
	private void requestFocus(Object source) {
		
		if(source instanceof Container) {
			((Container) source).requestFocusInWindow();// For KeyListener to work.
		}
		
	}
	
}
