package com.rawad.gamehelpers.utils.strings;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;

public class DrawableString {
	
	public static final String DEL = "\b";
	
	/**
	 * Used to put vertical padding between seperate lines of text.
	 * 
	 * @see #render
	 */
	private static final int VERTICAL_PADDING = 7;
	
	private static final int CARET_WIDTH = 1;// 2 is too wide and 1 is just thin enough...
	
	private String content;
	
	private String[] lines;
	
	private HashMap<Integer, HashMap<Integer, Color>> backgroundColors;// First Integer value is the line, second is the position-in-line
	/** A {@code null} value shows that the character at the given index is not selected. */
	private HashMap<Integer, HashMap<Integer, Color>> foregroundColors;
	
	private ArrayList<Rectangle> hitboxes;
	
	/** Really don't like this... But we need it to wrap text. */
	private FontMetrics fm;
	
	private int caretPosition;
	private int lineIndexCaretIsOn;
	/** Mainly used for highlighting text. */
	private int markedPosition;
	/** For making {@code String}s that are too long for their bounding box be rendered better. */
	private int minimumPositionToDrawFrom;
	private int maximumPositionToDrawTo;
	
	public DrawableString(String content) {
		
		setContent(content);
		
		backgroundColors = new HashMap<Integer, HashMap<Integer, Color>>();
		foregroundColors = new HashMap<Integer, HashMap<Integer, Color>>();
		
		hitboxes = new ArrayList<Rectangle>();
		
		caretPosition = -1;// Don't show Caret by default.
		lineIndexCaretIsOn = -1;
		markedPosition = -1;
		
		minimumPositionToDrawFrom = 0;
		maximumPositionToDrawTo = 0;
		
	}
	
	public DrawableString() {
		this("");
	}
	
	public void render(Graphics2D g, Color textColor, Color backgroundColor, Color caretColor, Rectangle boundingBox, 
			boolean center, boolean hideOutOfBounds) {
		
		FontMetrics fm = g.getFontMetrics();
		
		this.fm = fm;
		
		Rectangle prevClip = g.getClipBounds();
		
		if(hideOutOfBounds) {
			g.setClip(boundingBox);
		}
		
		int sections = lines.length <= 1? 2:lines.length;// For single-lines texts, it keeps it centered this way.
		
		int stringX = boundingBox.x;
		int stringY = boundingBox.y;
		
		int stringHeight = fm.getHeight();
		
		int totalHeightOfLines = lines.length * stringHeight;
		
		if(center) {
			stringY += (boundingBox.height/sections) + (stringHeight/4);
			
		} else {
			stringY += (stringHeight*3/4);
			
			if(totalHeightOfLines >= boundingBox.height) {
				stringY -= (totalHeightOfLines - boundingBox.height)*3/4;
			}
			
		}
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i];
			
			int stringWidth = fm.stringWidth(line);
			
			int y = stringY + ((i) * (stringHeight*3/4));
			
			if(center) {
				stringX = boundingBox.x + (boundingBox.width/2) - (stringWidth/2);
				y += (i * VERTICAL_PADDING);
			}
			
			drawLine(g, textColor, backgroundColor, caretColor, line, stringX, y, caretPosition, lineIndexCaretIsOn, 
					i, boundingBox.width, hideOutOfBounds);
			
		}
		
		if(hideOutOfBounds) {
			g.setClip(prevClip);
		}
		
	}
	
	public void drawLine(Graphics2D g, Color textColor, Color backgroundColor, Color caretColor, String line, int startX, 
			int y, int caretPosition, int lineCaretIsOn, int lineIndex, int maxWidth, boolean hideOutOfBounds) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int offset = 0;// Substracted from x below.
		
		String subLine = "";
		
		int indexToFindWidthUpTo = caretPosition;
		
		if(hideOutOfBounds && lineCaretIsOn == lineIndex) {
			try {
				subLine = line.substring(0, caretPosition - 1);// +1 so that the caret can also be shown
				
			} catch(Exception ex) {
				subLine = line;
				
			}
			
			if(fm.stringWidth(subLine) > maxWidth) {
				
				int cumulativeWidth = 0;
				
				for(int i = caretPosition + 1; i >= 1; i--) {// Count down the caret position, until we reach the far left of the 
					// bounding box.
					
					try {
						cumulativeWidth += fm.stringWidth(line.substring(i - 1, i));
					} catch(Exception ex) {
						cumulativeWidth += getCharacterWidth(Util.NL, fm);// Must mean we're at the end of line
					}
					
					if(cumulativeWidth > maxWidth) {
						indexToFindWidthUpTo = i;
						break;
					}
					
				}
				
				try {
					offset = fm.stringWidth(line.substring(0, indexToFindWidthUpTo));
				} catch(Exception ex) {// Should never happen, but just to be safe...
					offset = fm.stringWidth(line);
					ex.printStackTrace();
				}
				
			}
			
		}
		
		/*/
		
		int start = 0;
		int end = line.length();
		
		if(hideOutOfBounds && fm.stringWidth(line) > maxWidth) {
			
			String subLine = "";
			
			try {
				subLine = line.substring(minimumPositionToDrawFrom, maximumPositionToDrawTo);
			} catch(Exception ex) {
				
			}
			
			if(subLine.length() > maximumPositionToDrawTo - minimumPositionToDrawFrom + 1) {
				
				if(caretPosition <= minimumPositionToDrawFrom) {// Shift the "viewport" left
					minimumPositionToDrawFrom = caretPosition;
					
					maximumPositionToDrawTo = minimumPositionToDrawFrom + 
							line.substring(minimumPositionToDrawFrom, caretPosition).length() - 1;
					
				} else if(caretPosition >= maximumPositionToDrawTo) {// Shift the "viewport" right
					maximumPositionToDrawTo = caretPosition;
					
					minimumPositionToDrawFrom = maximumPositionToDrawTo - 
							line.substring(caretPosition, maximumPositionToDrawTo).length() + 1;
					
				}
				
//				if() {
//					start += 1;
//					minimumPositionToDrawFrom = maximumPositionToDrawTo - subLine.length();
//				} else if() {
//					end = subLine.length();
//				}
			}
			
		}/**/
		
		int x = startX - offset;
		
		for(int i = 0; i < line.length(); i++) {
			
			String character = getCharacterAsString(i, lineIndex);
			
			int characterWidth = getCharacterWidth(character, fm);
			
			boolean shouldRenderChar = true;
			
//			if(hideOutOfBounds) {
//				if(x + characterWidth < startX + maxWidth) {// Keep this here until we remove the "setClip" in the render method.
//					shouldRenderChar = false;
//				}
//			}
			
			if(shouldRenderChar) {
				drawCharacter(g, textColor, backgroundColor, caretColor, character, x, y, caretPosition, lineCaretIsOn, i, lineIndex);
			}
			
			x += characterWidth;
			
		}
		
		drawCharacter(g, textColor, backgroundColor, caretColor, Util.NL, x, y, caretPosition, lineCaretIsOn, line.length(), lineIndex);
		
	}
	
	public void drawCharacter(Graphics2D g, Color textColor, Color highlightColor, Color caretColor, String character, int x, int y, 
			int caretPosition, int lineCaretIsOn, int characterPosition, int lineIndex) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int width = getCharacterWidth(character, fm);
		int height = fm.getHeight();
		
		g.setColor(highlightColor);
		g.fillRect(x, y - (height*2/3) + 1, width, height*2/3);
		
		g.setColor(textColor);
		g.drawString(character, x, y);
		
		if(characterPosition == caretPosition && lineIndex == lineCaretIsOn) {
			drawCaret(g, caretColor, highlightColor, x, y, height);
		}
		
		if(characterPosition == minimumPositionToDrawFrom) {
//			drawCaret(g, Color.RED, highlightColor, x, y, height);
		}
		
		if(characterPosition == maximumPositionToDrawTo) {
//			drawCaret(g, Color.BLUE, highlightColor, x, y, height);
		}
		
	}
	
	private void drawCaret(Graphics2D g, Color color, Color backgroundColor, int x, int y, int height) {
		
//		g.setXORMode(backgroundColor);
		
		g.setColor(color);
		g.fillRect(x, y - (height*2/3) + 1, CARET_WIDTH, height*2/3);
		
//		g.setPaintMode();
		
	}
	
	private int getCharacterWidth(String character, FontMetrics fm) {
		
		int width = 0;
		
		switch(character) {
		
		case "\r":
		case "\n":
		case "\r\n":
		case "\n\r":
			width = fm.stringWidth(" ");
			break;
		
		case "\t":
			width = fm.stringWidth("    ");// 4 spaces for tab
			break;
			
		default:
			width = fm.stringWidth(character);
			break;
		}
		
		return width;
		
	}
	
	public void moveCaretUp() {
		moveCaretVertically(Direction.UP);
	}
	
	public void moveCaretDown() {
		moveCaretVertically(Direction.DOWN);
	}
	
	public void moveCaretVertically(Direction dir) {
		
		int direction = dir.getY();// Only use y-component for vertical movement
		
		this.lineIndexCaretIsOn += direction;
		
		if(lineIndexCaretIsOn >= lines.length) {
			lineIndexCaretIsOn = lines.length - 1;
			caretPosition = lines[lineIndexCaretIsOn].length();
		}
		
		if(lineIndexCaretIsOn < 0) {// In case the earlier test returns -1 or something (length of 0), same thing below.
			lineIndexCaretIsOn = 0;
			caretPosition = 0;
		}
		
		String lineCaretIsOn = lines[lineIndexCaretIsOn];
		
		if(caretPosition > lineCaretIsOn.length()) {// Only ">" because of the new line character.
			caretPosition = lineCaretIsOn.length();
		}
		
	}
	
	public void moveCaretRight() {
		
		moveCaretHorizontally(Direction.RIGHT);
		
		if(caretPosition > maximumPositionToDrawTo) {
			maximumPositionToDrawTo = caretPosition;
		}
		
	}
	
	public void moveCaretLeft() {
		
		moveCaretHorizontally(Direction.LEFT);
		
		if(caretPosition < minimumPositionToDrawFrom) {
			minimumPositionToDrawFrom = caretPosition;
		}
		
	}
	
	public void moveCaretHorizontally(Direction dir) {
		
		int direction = dir.getX();
		
		this.caretPosition += direction;
		
		String lineCaretIsOn = lines[lineIndexCaretIsOn];
		
		if(caretPosition >= lineCaretIsOn.length() + 1) {// +1 to account for the new line character
			
			if(lineIndexCaretIsOn + 1 >= lines.length) {// Is there an available new line to move to?
				
				caretPosition = lineCaretIsOn.length();
				
			} else {
				caretPosition = 0;// Tried to move below the last line.
				lineIndexCaretIsOn++;
			}
			
		}
		
		if(caretPosition < 0) {
			
			if(lineIndexCaretIsOn > 0) {// We're moving up a line that is available.
				
				caretPosition = lines[this.lineIndexCaretIsOn - 1].length() + caretPosition + 1;
				// caretPosition should be negative, so "+" should do.
				
				this.lineIndexCaretIsOn -= 1;
				
			} else {// Caret is being moved to before first line.
				caretPosition = 0;
			}
			
		}
		
	}
	
	public void moveCaret(Direction dir) {
		
		switch(dir) {
		
		case UP:
		case DOWN:
			moveCaretVertically(dir);
			break;
			
		case RIGHT:
		case LEFT:
			moveCaretHorizontally(dir);
			break;
		
		}
		
	}
	
	public void setCaretPosition(int caretPosition, int lineIndexPosition) {
		
		this.caretPosition = caretPosition;
		this.lineIndexCaretIsOn = lineIndexPosition;
		
	}
	
	public void newLine() {
		this.add(Util.NL, caretPosition, lineIndexCaretIsOn);
		
		// Resets caret position to beginning of line.
		setCaretPosition(0, lineIndexCaretIsOn);// This one first so that it fixes any problems with this manual thing.
		
	}
	
	public void add(String contentToAdd) {
		this.add(contentToAdd, this.caretPosition, this.lineIndexCaretIsOn);
	}
	
	public void add(String contentToAdd, int positionInLine, int line) {
		
		DirectionHolder directionToMoveCaret = new DirectionHolder(null);
		
		String[] lines = this.lines;
		
		ArrayList<String> tempLines = new ArrayList<String>(Arrays.asList(lines));
		
		String lineCaretIsOn = lines[line];
		
		if(contentToAdd.equals(Util.NL)) {
			
			String newLineContent = addNewLine(tempLines, lines, lineCaretIsOn, positionInLine, line, directionToMoveCaret);
			
			tempLines.add(line + 1, newLineContent);
			
		} else if(contentToAdd.equals(DEL)) {
			
			if(!getContent().equals("")) {
				int newLineCount = addDelete(tempLines, lines, lineCaretIsOn, positionInLine, line, directionToMoveCaret);
				lines = new String[newLineCount];
				
				moveCaretLeft();
			}
			
		} else {
			
			String newLineContent = addContent(tempLines, lines, lineCaretIsOn, contentToAdd, 
					positionInLine, line, directionToMoveCaret);
			
			tempLines.set(line, newLineContent);
			
		}
		
		lines = tempLines.toArray(lines);
		
		setContent(getContentFromLines(lines));
		
		if(directionToMoveCaret.getDirection() != null) {
			moveCaret(directionToMoveCaret.getDirection());
		}
		
	}
	
	/**
	 * Adds {@code contentToAdd} at the line {@code lineIndex} at the index within the line of {@code positionInLine}.
	 * 
	 * @param lines
	 * @param linesArray
	 * @param currentLine
	 * @param contentToAdd
	 * @param positionInLine
	 * @param lineIndex
	 * @return The new content of the line {@code lineIndex};
	 */
	private String addContent(ArrayList<String> lines, String[] linesArray, String currentLine, String contentToAdd, 
			int positionInLine, int lineIndex, DirectionHolder dir) {
		
		String firstHalf = currentLine.substring(0, positionInLine);
		String secondHalf = currentLine.substring(positionInLine);
		
		if(positionInLine >= currentLine.length()) {
			firstHalf = currentLine.substring(0, positionInLine);
			secondHalf = "";
		} else if(positionInLine <= 0) {
			firstHalf = "";
			secondHalf = currentLine.substring(positionInLine);
		}
		
		dir.setDirection(Direction.RIGHT);
		
		return firstHalf + contentToAdd + secondHalf;
		
	}
	
	/**
	 * Adds a new line to the given {@code lines ArrayList} object and moves around remaining text to make it look accurate.
	 * 
	 * @param lines
	 * @param linesArray
	 * @param currentLine
	 * @param positionInLine
	 * @param lineIndex
	 * @return The content of the line directly after {@code lineIndex}.
	 */
	private String addNewLine(ArrayList<String> lines, String[] linesArray, String currentLine, int positionInLine, 
			int lineIndex, DirectionHolder dir) {
		
		String newLineContent = "";
		
		if(positionInLine == 0) {
			newLineContent = linesArray[lineIndex];// Take all of the line
			lines.set(lineIndex, "");// Replace the line we just took
			
		} else if(positionInLine < currentLine.length()) {
			newLineContent = currentLine.substring(positionInLine, currentLine.length());// Take the last characters in this line
			lines.set(lineIndex, currentLine.substring(0, positionInLine));// Cuts the line we just took text from
			
		}
		
		dir.setDirection(Direction.DOWN);
		
		return newLineContent;
	}
	
	/**
	 * Deletes a character from the line {@code lineIndex} relative to the {@code linesArray} object, at the given {@code positionInLine}
	 * index within the line.
	 * 
	 * @param lines
	 * @param linesArray
	 * @param currentLine
	 * @param positionInLine
	 * @param lineIndex
	 * @return An updated number of lines in case one has been deleted by this process.
	 */
	private int addDelete(ArrayList<String> lines, String[] linesArray, String currentLine, int positionInLine, 
			int lineIndex, DirectionHolder dir) {
		
		int newLineCount = linesArray.length;
		
		if(positionInLine <= 0) {// Far left of line
			
			if(lineIndex <= 0) {// Far left and top line, so do nothing
				return newLineCount;
			}
			
			lines.set(lineIndex - 1, linesArray[lineIndex - 1] + currentLine);// Adds current line to end of previous
			lines.remove(lineIndex);// Removes the line that was appended to the end
			
			newLineCount = linesArray.length - 1;// Properly updates the size of the array later; keeps last line from being "null"
			
		} else if(positionInLine >= currentLine.length()) {// Far right of line
			
			lines.set(lineIndex, currentLine.substring(0, positionInLine - 1));
			
		} else {
			
			String firstHalf = currentLine.substring(0, positionInLine - 1);
			String secondHalf = currentLine.substring(positionInLine);
			
			lines.set(lineIndex, firstHalf + secondHalf);
			
		}
		
//		dir.setDirection(Direction.LEFT);// Causes error when at beginning of line.
		
		return newLineCount;
		
	}
	
	public void setContent(String content, int maxWidth, boolean wrapContent) {
		lines = parseLines(content, Util.NL);
		
		if(fm != null && wrapContent) {
			lines = wrapLines(lines, fm, maxWidth);
		}
		
		this.content = content;
	}
	
	public void setContent(String content) {
		this.setContent(content, 0, false);
	}
	
	public String getContent() {
		return content;
	}
	
	public int getCaretPosition() {
		return caretPosition;
	}
	
	public int getLineCaretIsOn() {
		return lineIndexCaretIsOn;
	}
	
	private String[] wrapLines(String[] lines, FontMetrics fm, int maxWidth) {
		
		ArrayList<String> newLines = new ArrayList<String>(Arrays.asList(lines));
		
		int lineOffset = 0;
		
		for(int lineIndex = 0; lineIndex < lines.length; lineIndex++) {
			
			String line = lines[lineIndex];
			String currentLongLine = line;// modified everytime a new line is created; it becomes that new line.
			
			int currentMaxLineWidth = fm.stringWidth(line);
			
			int runCount = 0;
			
			do {
				
				int cumulativeWidth = 0;
				
				for(int characterIndex = 0; characterIndex < currentLongLine.length(); characterIndex++) {
					
					String currentChar = String.valueOf(currentLongLine.charAt(characterIndex));
					
					cumulativeWidth += fm.stringWidth(currentChar);
					
					if(cumulativeWidth > maxWidth) {
						// There must be a way to to it so we don't add runCount every single time...
						newLines.add(lineOffset + runCount, "");// To prevent index out of bounds
						
						newLines.set(lineOffset + runCount, currentLongLine.substring(0, characterIndex));// Shorten previous line
						
						try {
							newLines.set(lineOffset + runCount + 1, currentLongLine.substring(characterIndex));
						} catch(Exception ex) {
							Logger.log(Logger.WARNING, "Message is too long or something...");
						}
						
						currentLongLine = currentLongLine.substring(characterIndex);
						
						currentMaxLineWidth = fm.stringWidth(currentLongLine);
						
						runCount++;
						
					}
					
				}
				
				runCount++;
				
			} while(currentMaxLineWidth > maxWidth);
			
			lineOffset += runCount;// Add to the line offset the number of lines previously added
			
		}
		
		return newLines.toArray(new String[newLines.size()]);
		
	}
	
	/**
	 * Uses {@code regex} to parse the given {@code content} into an array of lines.
	 * 
	 * @param content
	 * @param regex
	 * @return
	 */
	public String[] parseLines(String content, String regex) {
		
		ArrayList<String> holder = new ArrayList<String>();
		
		int regexCount = 0;
		int previousMark = 0;
		
		for(int i = 0; i < content.length() - regex.length() + 1; i++) {// +1 for reasons.
			
			String currentlyChecking = content.substring(i, i + regex.length());// Something with ubstrnig
			
			if(currentlyChecking.equals(regex)) {
				holder.add(content.substring(previousMark, i));
				regexCount++;
				previousMark = i;
			}
			
		}
		
		if(!content.endsWith(regex)) {
			holder.add(content.substring(previousMark, content.length()));
			regexCount++;
		}
		
		String[] re = holder.toArray(new String[regexCount]);
		
		String[] splitString = content.split(regex);
		
		for(int i = 0; i < re.length; i++) {
			
			try {
				re[i] = splitString[i];
			} catch(Exception ex) {
				re[i] = "";
			}
			
		}
		
		return re;
		
	}
	
	public String getContentFromLines(String[] lines) {
		
		String content = "";
		
		for(int i = 0; i < lines.length; i++) {
			content += lines[i] + Util.NL;
		}
		
		return content;
	}
	
	/**
	 * Returns index of caret relative to the content object. For two-length line seperators (e.g. "\n\r"), it only counts them as one to
	 * make manipulating the {@code content} object a lot simpler.
	 * 
	 * @param caretPositon
	 * @param lineCaretIsOn
	 * 
	 * @return
	 */
	private int toContentIndex(int caretPositon, int lineCaretIsOn) {
		
		return -1;
		
	}
	
	public String getCharacterAsString(int positionOnLine, int line) {
		return getCharacterAsString(lines, positionOnLine, line);
	}
	
	public String getCharacterAsString(String[] lines, int positionOnLine, int line) {
		
		try {
			return String.valueOf(lines[line].charAt(positionOnLine));
		} catch(StringIndexOutOfBoundsException ex) {
			Logger.log(Logger.WARNING, "DrawableString, getCharacterAsString(), got an index out of bounds exception");
			return "";
		}
		
	}
	
	private enum Direction {
		
		UP(0, -1),
		DOWN(0, 1),
		RIGHT(1, 0),
		LEFT(-1, 0);
		
		private final int x;// Could add "scale" int value, to multiply these by to be able to move the caret multiple positions at once.
		private final int y;
		
		private Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
	}
	
	private class DirectionHolder {
		
		private Direction dir;
		
		public DirectionHolder(Direction dir) {
			this.dir = dir;
		}
		
		public void setDirection(Direction dir) {
			this.dir = dir;
		}
		
		public Direction getDirection() {
			return dir;
		}
		
	}
	
}
