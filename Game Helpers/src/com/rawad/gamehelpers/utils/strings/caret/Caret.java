package com.rawad.gamehelpers.utils.strings.caret;

import java.util.ArrayList;

import com.rawad.gamehelpers.utils.strings.Line;

public class Caret {
	
	/** Doesn't move during mouse drags. */
	private int anchorEnd;
	/** Point of text insertion; moves when mouse is dragged. */
	private int activeEnd;
	
	/** Index of the lin in this block of text. */
	private int line;
	/** Index of character in the specified line. */
	private int positionInLine;
	
	public Caret() {
		
		anchorEnd = 0;
		activeEnd = 0;
		
		line = 0;
		positionInLine = 0;
		
	}
	
	public void moveCaret(ArrayList<Line> lines, Direction direction) {
		this.moveCaret(lines, direction.getDX(), direction.getDY());
	}
	
	/**
	 * 
	 * @param lines
	 * @param dirX + is right, - is left
	 * @param dirY + is down, - is up
	 */
	private void moveCaret(ArrayList<Line> lines, int dirX, int dirY) {
		
		int lineLength = lines.get(line).toString().length();
		
		int newPositionInLine = positionInLine + dirX;
		
		if(newPositionInLine > lineLength) {// Calculated first because it may change the line itself
			
			line++;
			newPositionInLine = 0;
			
		} else if(newPositionInLine < 0) {
			
			line--;// Will be corrected below if negative
			
			try {// Moves caret to end of previous line when moving left, if possible
				newPositionInLine = lines.get(line - 1).toString().length() - 1;
			} catch(ArrayIndexOutOfBoundsException ex) {
				newPositionInLine = 0;
			}
			
		}
		
		int newLine = line + dirY;
		
		if(newLine > lines.size()) {
			
			newLine = line;// Moves caret to end if moving down at bottom line
			newPositionInLine = lines.get(newLine).toString().length() - 1;
			
		} else if(newLine < 0) {
			
			newLine = 0;
			
		}
		
		line = newLine;
		positionInLine = newPositionInLine;
		
	}
	
	public int getInTextIndex(ArrayList<Line> lines) {
		
		int length = 0;// Total length of string from beginning up to caret's position.
		
		for(int i = 0; i < line; i++) {
			
			length += lines.get(i).toString().length();
			
		}
		
		length += lines.get(line).toString().substring(0, positionInLine).length();
		
		return length - 1;
		
	}
	
	public void setLine(int line) {
		this.line = line;
	}

	public int getLine() {
		return line;
	}
	
	public void setPositionInLine(int positionInLine) {
		this.positionInLine = positionInLine;
	}
	
	public int getPositionInLine() {
		return positionInLine;
	}
	
}
