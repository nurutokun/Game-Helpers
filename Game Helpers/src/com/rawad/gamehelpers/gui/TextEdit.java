package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import com.rawad.gamehelpers.display.Cursors;
import com.rawad.gamehelpers.input.KeyboardInput;
import com.rawad.gamehelpers.input.MouseInput;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;

public class TextEdit extends TextContainer {
	
	private static final int CURSOR_BLINK_SPEED = 25;
	
	private Font font;
	
	private int counter;
	
	private boolean focused;
	private boolean newLineOnEnter;
	private boolean increasing;
	private boolean prevEdited;
	
	public TextEdit(String id, String text, int x, int y, int width, int height) {
		super(id, text, x, y, width, height);
		
		font = new Font("Arial", Font.PLAIN, 14);
		
		increasing = true;
		
		this.text.setCaretPosition(0, 0);
		
		centerText = false;
		hideOutOfBoundsText = true;
		focused = false;
		newLineOnEnter = true;
		prevEdited = false;
		
	}
	
	public TextEdit(String id, int x, int y, int width, int height) {
		this(id, id, x, y, width, height);
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		if(isHovered() && !me.isConsumed()) {
			MouseInput.setCursor(Cursors.TEXT);
		} else {
			
			if(me.isButtonDown()) {// Mouse pressed outside of component
				focused = false;
			}
			
		}
		
		if(focused) {
			
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
			
			if(!ke.isConsumed() ) {
				
				handleKeyInput(ke.getTypedKeys());
				
				highlightByKeyboard();
				
				ke.consume();
				
			}
			
			if(!me.isConsumed()) {
				highlightByMouse(me);
				
			}
			
		} else {
			cursorColor = new Color(cursorColor.getRed(), cursorColor.getGreen(), cursorColor.getBlue(), 0);// Make cursor (caret) 
			// invisible when this component isn't highlighted.
		}
		
		super.update(me, ke);
		
	}
	
	private void highlightByKeyboard() {
		
		boolean up = KeyboardInput.isKeyDown(KeyEvent.VK_UP);
		boolean down = KeyboardInput.isKeyDown(KeyEvent.VK_DOWN);
		boolean right = KeyboardInput.isKeyDown(KeyEvent.VK_RIGHT);
		boolean left = KeyboardInput.isKeyDown(KeyEvent.VK_LEFT);
		
		if(up || down || right || left) {
			
			boolean highlight = KeyboardInput.isKeyDown(KeyEvent.VK_SHIFT, false);
			
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
				
				e.consume();
			}
		}
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		
//		text.setCaretPositionByCoordinates(e.getX(), e.getY());
		
		boolean highlight = KeyboardInput.isKeyDown(KeyEvent.VK_SHIFT, false);
		
		if(highlight) {
			
//			text.updateHighlights(Color.BLUE);
			
		} else {
//			text.setMarkedPosition(text.getCaretPosition());
//			text.clearHighlights();
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		focused = true;
		
	}
	
	@Override
	public void render(Graphics2D g) {
		
		Font prevFont = g.getFont();
		
		g.setFont(font);
		
		g.setColor(Color.WHITE);
		g.fill(hitbox);
		
		g.setColor(Color.LIGHT_GRAY);
		g.drawRect(hitbox.x, hitbox.y, hitbox.width - 1, hitbox.height - 1);
		
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
			
			if(newLineOnEnter) {
				text.newLine();
			}
			
			break;
		
		case "\f":
		case "\t":
			break;
			
		default:
			text.add(typedKey);
			break;
		
		}
		
		counter = CURSOR_BLINK_SPEED;
		increasing = false;
		
		prevEdited = true;
		
	}
	
	/**
	 * Sets whether or not a new line should be created after pressing the enter button.
	 * 
	 * @param newLineOnEnter
	 */
	public void setNewLineOnEnter(boolean newLineOnEnter) {
		this.newLineOnEnter = newLineOnEnter;
	}
	
	public boolean isFocused() {
		return focused;
	}
	
	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public boolean wasPrevEdited() {
		
		boolean re = prevEdited;
		
		prevEdited = false;
		
		return re;
		
	}
	
}
