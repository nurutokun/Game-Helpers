package com.rawad.gamehelpers.util.strings;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DrawableString {
	
	public static final String NL = System.lineSeparator();
	
	public static final String DEL = "\b";
	
//	public static final String UNIVERSAL_LINE_SEPERATOR = "\n\r";// try with "\n\r", just "\r" deletes when theres a bunch of them before 
	// any other character and that character is deleted (all of the "\r" delete).
	
	/**
	 * Used to put vertical padding between seperate lines of text.
	 * 
	 * @see #render
	 */
	private static final int VERTICAL_PADDING = 7;
	
	private static final int CARET_WIDTH = 2;
	
	private String content;
	
	private String[] lines;
	
	private HashMap<Integer, HashMap<Integer, Color>> backgroundColors;// First Integer value is the line, second is the position-in-line
	/** A {@code null} value shows that the character at the given index is not selected. */
	private HashMap<Integer, HashMap<Integer, Color>> foregroundColors;
	
	private ArrayList<Rectangle> hitboxes;
	
	private int caretPosition;
	private int lineIndexCaretIsOn;
	/** Mainly used for highlighting text. */
	private int markedPosition;
	
	public DrawableString(String content) {
		
		setContent(content);
		
		backgroundColors = new HashMap<Integer, HashMap<Integer, Color>>();
		foregroundColors = new HashMap<Integer, HashMap<Integer, Color>>();
		
		hitboxes = new ArrayList<Rectangle>();
		
		caretPosition = -1;// Don't show Caret by default.
		lineIndexCaretIsOn = -1;
		markedPosition = -1;
		
	}
	
	public DrawableString() {
		this("");
	}
	
	public void render(Graphics2D g, Color textColor, Color backgroundColor, Color caretColor, 
			Rectangle boundingBox, boolean center) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int sections = lines.length <= 1? 2:lines.length;// For single-lines texts, it keeps it centered this way.
		
		int stringX = boundingBox.x;
		int stringY = boundingBox.y + (boundingBox.height/sections);
		
		int stringHeight = fm.getHeight();
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i];// + (i+1 >= lines.length? "":UNIVERSAL_LINE_SEPERATOR);// Add the "\r" character only at the end of 
			// lines that need it.
			
			int stringWidth = fm.stringWidth(line);
			
			if(center) {
				stringX = boundingBox.x + (boundingBox.width/2) - (stringWidth/2);
			}
			
			drawLine(g, textColor, backgroundColor, caretColor, line, stringX, 
					stringY + (stringHeight/4 * (i+1)) + (i * VERTICAL_PADDING), caretPosition, lineIndexCaretIsOn, i);
			
		}
		
	}
	
	public void drawLine(Graphics2D g, Color textColor, Color backgroundColor, Color caretColor, String line, int x, 
			int y, int caretPosition, int lineCaretIsOn, int lineIndex) {
		
		FontMetrics fm = g.getFontMetrics();
		
		for(int i = 0; i < line.length(); i++) {
			
			String character = getCharacterAsString(i, lineIndex);
			
			int characterWidth = getCharacterWidth(character, fm);
			
			drawCharacter(g, textColor, backgroundColor, caretColor, character, x, y, caretPosition, lineCaretIsOn, i, lineIndex);
			
			x += characterWidth;
			
		}
		
		drawCharacter(g, textColor, backgroundColor, caretColor, NL, x, y, caretPosition, lineCaretIsOn, line.length(), lineIndex);
		
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
	}
	
	public void moveCaretLeft() {
		moveCaretHorizontally(Direction.LEFT);
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
		this.add(NL, caretPosition, lineIndexCaretIsOn);
		
		// Resets caret position to beginning of line.
		setCaretPosition(0, lineIndexCaretIsOn);// This one first so that it fixes any problems with this manual thing.
		
	}
	
	public void add(String contentToAdd, int positionInLine, int line) {
		
		DirectionHolder directionToMoveCaret = new DirectionHolder(null);
		
		String[] lines = this.lines;
		
		ArrayList<String> tempLines = new ArrayList<String>(Arrays.asList(lines));
		
		String lineCaretIsOn = lines[line];
		
		if(contentToAdd.equals(NL)) {
			
			String newLineContent = addNewLine(tempLines, lines, lineCaretIsOn, positionInLine, line, directionToMoveCaret);
			
			tempLines.add(line + 1, newLineContent);
			
		} else if(contentToAdd.equals(DEL)) {
			
			int newLineCount = addDelete(tempLines, lines, lineCaretIsOn, positionInLine, line, directionToMoveCaret);
			lines = new String[newLineCount];
			
			moveCaretLeft();
			
		} else {
			
			String newLineContent = addContent(tempLines, lines, lineCaretIsOn, contentToAdd, positionInLine, line, directionToMoveCaret);
			
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
	
	public void setContent(String content) {
		lines = parseLines(content, NL);
		
		this.content = content;
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
			content += lines[i] + NL;
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
	public int toContentIndex(int caretPositon, int lineCaretIsOn) {
		
		return -1;
		
	}
	
	public String getCharacterAsString(int positionOnLine, int line) {
		return getCharacterAsString(lines, positionOnLine, line);
	}
	
	public String getCharacterAsString(String[] lines, int positionOnLine, int line) {
		return String.valueOf(lines[line].charAt(positionOnLine));
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
