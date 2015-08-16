package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.gamehelpers.display.Cursors;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseEvent;
import com.rawad.gamehelpers.input.MouseInput;

public class TextEdit extends TextContainer {
	
	private static final int CURSOR_BLINK_SPEED = 25;
	
	private Font font;
	
	private int counter;
	private boolean increasing;
	
	public TextEdit(String id, String text, int x, int y, int width, int height) {
		super(id, text, x, y, width, height);
		
		font = new Font("Arial", Font.PLAIN, 14);
		
		increasing = true;
		
		this.text.setCaretPosition(0, 0);
		
		centerText = false;
		
	}
	
	public TextEdit(String id, int x, int y, int width, int height) {
		this(id, id, x, y, width, height);
		
	}
	
	@Override
	public void update(MouseEvent e) {
		
		if(isHovered()) {
			MouseInput.setCursor(Cursors.TEXT);
		}
		
		if(isFocused()) { 
			
			if(counter >= CURSOR_BLINK_SPEED) {
				increasing = false;
			} else if(counter <= 0) {
				increasing = true;
			}
			
			if(increasing) {
				counter++;
			} else {
				counter--;
			}
			
			int alpha = counter >= (CURSOR_BLINK_SPEED/2)? 255:0;
			
			cursorColor = new Color(cursorColor.getRed(), cursorColor.getGreen(), cursorColor.getBlue(), alpha);
			
			handleKeyInput(KeyboardInput.getTypedKeys());
			
			highlightByKeyboard();
			highlightByMouse(e);
			
		}
		
		super.update(e);
		
	}
	
	private void highlightByKeyboard() {
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT);
		
		if(up || down || right || left) {
			
			KeyboardInput.setConsumeAfterRequest(false);// Ew...
			
			boolean highlight = KeyboardInput.isKeyDown(KeyEvent.VK_SHIFT);
			
			KeyboardInput.setConsumeAfterRequest(true);
			
			if(up) {
				text.moveCaretUp();
			} else if(down) {
				text.moveCaretDown();
			} else if(right) {
				text.moveCaretRight();
			} else if(left) {
				text.moveCaretLeft();
			}
			
			if(highlight) {
//				text.updateHighlights(Color.BLUE);
			} else {
//				text.clearHighlights();
//				text.setMarkedPosition(text.getCaretPosition());
			}
			
			counter = CURSOR_BLINK_SPEED;
			increasing = false;
		}
		
	}
	
	private void highlightByMouse(MouseEvent e) {
		
		if(!e.isConsumed()) {
			boolean highlight = e.isButtonDown();
			
			int x = e.getX();
			int y = e.getY();
			
			if(highlight && prevMouseX != x && prevMouseY != y) {
//				text.setCaretPositionByCoordinates(e.getX(), e.getY());
//				text.updateHighlights(Color.BLUE);
			}
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
//		text.setCaretPositionByCoordinates(e.getX(), e.getY());
		
		KeyboardInput.setConsumeAfterRequest(false);
		
		boolean highlight = KeyboardInput.isKeyDown(KeyEvent.VK_SHIFT);
		
		KeyboardInput.setConsumeAfterRequest(true);
		
		if(highlight) {
			
//			text.updateHighlights(Color.BLUE);
			
		} else {
//			text.setMarkedPosition(text.getCaretPosition());
//			text.clearHighlights();
		}
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		Font prevFont = g.getFont();
		
		g.setFont(font);
		
		g.setColor(Color.WHITE);
		g.fill(hitbox);
		
		g.setColor(Color.LIGHT_GRAY);
		g.draw(hitbox);
		
		super.render(g);
		
		g.setFont(prevFont);
		
	}
	
	private void handleKeyInput(String[] typedKeys) {
		
		for(int i = 0; i < typedKeys.length; i++) {
			keyTyped(typedKeys[i]);
		}
		
	}
	
	/**
	 * Called by {@code handleKeyInput(String[])} for every key that has been typed.
	 * 
	 * @param keyTyped
	 */
	protected void keyTyped(String typedKey) {
		
		switch(typedKey) {
		
		case "\n":
		case "\r":
		case "\n\r":
		case "\r\n":
			
			text.newLine();
			
			break;
		
		case "\f":
			break;
			
		default:
			text.add(typedKey, text.getCaretPosition(), text.getLineCaretIsOn());
			break;
		
		}
		
		counter = CURSOR_BLINK_SPEED;
		increasing = false;
		
	}
	
}
