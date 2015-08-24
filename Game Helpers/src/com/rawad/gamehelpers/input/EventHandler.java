package com.rawad.gamehelpers.input;

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

public class EventHandler implements MouseMotionListener, MouseListener, MouseWheelListener,
		KeyListener, ComponentListener, WindowListener {
	
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
		
		if((int) c == KeyEvent.VK_ESCAPE) {// Stupid escape.
			return;
		}
		
		KeyboardInput.addTypedKey(c);
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		MouseInput.setMouseWheelPosition(e.getUnitsToScroll());
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonClicked(MouseInput.MIDDLE_MOUSE_BUTTON, true);
			
		} else if(e.isMetaDown()) {
			MouseInput.setButtonClicked(MouseInput.RIGHT_MOUSE_BUTTON, true);
			
		} else {
			MouseInput.setButtonClicked(MouseInput.LEFT_MOUSE_BUTTON, true);
			
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	
	@Override
	public void mouseExited(MouseEvent e) {}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonDown(MouseInput.MIDDLE_MOUSE_BUTTON, true);
		
		} else if(e.isMetaDown()) {
			MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, true);
		
		} else {
			MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, true);
		
		}
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.isAltDown()) {
			MouseInput.setButtonDown(MouseInput.MIDDLE_MOUSE_BUTTON, false);
			
		} else if(e.isMetaDown()) {
			MouseInput.setButtonDown(MouseInput.RIGHT_MOUSE_BUTTON, false);
			
		} else {
			MouseInput.setButtonDown(MouseInput.LEFT_MOUSE_BUTTON, false);
			
		}
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
		double xScale = (double) DisplayManager.getScreenWidth()/(double) e.getComponent().getWidth();
		double yScale = (double) DisplayManager.getScreenHeight()/(double) e.getComponent().getHeight();
		
		int newX = (int) (((double) e.getX()) * xScale);
		int newY = (int) (((double) e.getY()) * yScale);
		
		MouseInput.setX(newX);
		MouseInput.setY(newY);
		
		MouseInput.setButtonClicked(MouseInput.MIDDLE_MOUSE_BUTTON, false);
		MouseInput.setButtonClicked(MouseInput.RIGHT_MOUSE_BUTTON, false);
		MouseInput.setButtonClicked(MouseInput.LEFT_MOUSE_BUTTON, false);
		
//		Logger.log(Logger.DEBUG, "Mouse Moved x,y: " + e.getX() + ", " + e.getY());
		
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
	public void windowActivated(WindowEvent e) {}
	
	@Override
	public void windowClosed(WindowEvent e) {}
	
	@Override
	public void windowClosing(WindowEvent e) {
		DisplayManager.requestClose();
	}
	
	@Override
	public void windowDeactivated(WindowEvent e) {}
	
	@Override
	public void windowDeiconified(WindowEvent e) {}
	
	@Override
	public void windowIconified(WindowEvent e) {}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	
}
