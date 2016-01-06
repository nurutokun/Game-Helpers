package com.rawad.gamehelpers.utils.strings;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class StringRenderingTest {
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				StringRenderingTest foo = new StringRenderingTest();
				
				foo.init();
			}
			
		});
		
	}
	
	private static final String NL = System.lineSeparator();
	
	private JFrame frame;
	private JPanel panel;
	
	private Thread t;
	
	private String text = NL;/*
			"First Line" + NL + 
			"Second Line" + NL + 
			"Third reeeallly long line" + NL + 
			"5";/* + NL + 
			NL + 
			NL + 
			NL;*/
	
	private String[] lines;
	
	private int caretPosition = 0;
	
	public void init() {
		
		frame = new JFrame("String Rendering");
		
		panel = new CustomPanel();
		
		t = new Thread(new CustomThread());
		
		panel.setPreferredSize(new Dimension(500, 400));
		
		frame.add(panel);
		
		frame.pack();
		
		frame.addKeyListener(new EventHandler());
		
		frame.setIgnoreRepaint(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		t.start();
		
	}
	
	private class CustomThread implements Runnable {
		
		@Override
		public void run() {
			
			boolean foo = true;
			
			while(true) {
				
				if(lines != null) {
					
					if(foo) {
						
						for(String s: lines) {
							System.out.println(s);
						}
						
						foo = false;
					}
					
					int totalLength = text.length() - (NL.length()/2 * (lines.length - 1)) + 1;
					
					if(caretPosition < 0) {
						caretPosition = totalLength - 1;
					}
					
					caretPosition %= totalLength;
				}
				
				frame.repaint();
				
				try {
					Thread.sleep(200);
				} catch(Exception ex) {
					break;
				}
			}
			
		}
			
	}
	
	private class CustomPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -883208226596255996L;
		
		public CustomPanel() {
			
			setBackground(Color.WHITE);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if(g instanceof Graphics2D) {
				renderString((Graphics2D) g, text);
			}
			
			g.dispose();
		}
		
		private void renderString(Graphics2D g, String text) {
			
			lines = properlySplit(text, NL);
			
			FontMetrics fm = g.getFontMetrics();
			
			int stringHeight = fm.getHeight();
			
			int stringX = 0;
			int stringY = stringHeight;
			
			int lineIndexOffset = 0;
			
			g.setColor(Color.BLUE);
			g.drawString("# of lines: " + lines.length + ", caret position: " + caretPosition + ", text length:" + text.length(), 
					getWidth()/2, stringHeight);
			
			for(int i = 0; i < lines.length; i++) {
				
				String line = lines[i];
				
				int newCaretPosition = caretPosition - (NL.length()/2 * i);
				
				drawLine(g, line, stringX, stringY, i, lineIndexOffset, newCaretPosition);
				
				g.setColor(Color.RED);
				g.drawString("Offset Caret Position: " + newCaretPosition, stringX + (line.length() * 10), stringY);
				
				lineIndexOffset += line.length();
				stringY += stringHeight;
				
			}
			
		}
		
		private void drawLine(Graphics2D g, String line, int startX, int y, int lineIndex, int lineIndexOffset, int caretPosition) {
			
			FontMetrics fm = g.getFontMetrics();
			
			for(int i = 0; i < line.length(); i++) {
				
				String character = String.valueOf(line.charAt(i));
				
				int stringWidth = fm.stringWidth(character);
				
				drawCharacter(g, character, Color.BLACK, startX, y, lineIndexOffset + i, caretPosition);
				
				startX += stringWidth;
				
			}
			
			drawCharacter(g, lineIndexOffset + "", Color.RED, startX, y, lineIndexOffset + line.length(),// - (NL.length() * lineIndex), 
					caretPosition);
			
		}
		
		private void drawCharacter(Graphics2D g, String character, Color color, int x, int y, int lineIndexOffset, int caretPosition) {
			
			FontMetrics fm = g.getFontMetrics();
			
			int height = fm.getHeight();
			
			g.setColor(color);
			g.drawString(character, x, y);
			
			if(caretPosition == lineIndexOffset) {
				g.setColor(Color.BLACK);
				g.fillRect(x, y - height, 2, height);
			}
			
		}
		
		private String[] properlySplit(String original, String regex) {
			
			ArrayList<String> holder = new ArrayList<String>();
			
			int regexCount = 0;
			int previousMark = 0;
			
			for(int i = 0; i < original.length() - regex.length() + 1; i++) {// +1 for reasons.
				
				String currentlyChecking = original.substring(i, i + regex.length());// Something with ubstrnig
				
				if(currentlyChecking.equals(regex)) {
					holder.add(original.substring(previousMark, i));
					regexCount++;
					previousMark = i;
				}
				
			}
			
			if(!original.endsWith(regex)) {
				holder.add(original.substring(previousMark, original.length()));
				regexCount++;
			}
			
			String[] re = holder.toArray(new String[regexCount]);
			
			String[] splitString = original.split(regex);
			
			for(int i = 0; i < re.length; i++) {
				
				try {
					re[i] = splitString[i];
				} catch(Exception ex) {}
				
			}
			
			return re;
			
		}
		
	}
	
	private class EventHandler implements KeyListener {
		
		@Override
		public void keyTyped(KeyEvent e) {
			
		}
		
		@Override
		public void keyPressed(KeyEvent e) {
			int keyCode = e.getKeyCode();
			
			switch(keyCode) {
			
			case KeyEvent.VK_RIGHT:
				caretPosition += 1;
				break;
				
			case KeyEvent.VK_LEFT:
				caretPosition -= 1;
				break;
			
			case KeyEvent.VK_SPACE:
				System.out.println(text.charAt(caretPosition));
				break;
				
			}
			
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
		}
		
	}
	
	/*/
	public void render(Graphics2D g, Color textColor, Color backgroundColor, Color caretColor, Rectangle boundingBox, boolean center) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int sections = lines.length <= 1? 2:lines.length;// For single-lines texts, it keeps it centered this way.
		
		int stringX = boundingBox.x;
		int stringY = boundingBox.y + (boundingBox.height/sections);
		
		int stringHeight = fm.getHeight();
		
		int lineIndexOffset = 0;// To keep track of proper character index for multiple lines.
		
		if(lines.length <= 0) {
//			drawLine(g, textColor, backgroundColor, caretColor, "\r", stringX, stringY + (stringHeight/4), lineIndexOffset);
		}
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i];// + (i+1 >= lines.length? "":UNIVERSAL_LINE_SEPERATOR);// Add the "\r" character only at the end of 
			// lines that need it.
			
			int stringWidth = fm.stringWidth(line);
			
			if(center) {
				stringX = boundingBox.x + (boundingBox.width/2) - (stringWidth/2);
			}
			
			int offsetCaretPosition = caretPosition - (UNIVERSAL_LINE_SEPERATOR.length()/2 * i);
			// "U_L_S.length()/2 seems to only work with a length of 2
			
			drawLine(g, textColor, backgroundColor, caretColor, line, stringX, 
					stringY + (stringHeight/4 * (i+1)) + (i * VERTICAL_PADDING), i, lineIndexOffset, offsetCaretPosition);
			
			lineIndexOffset += line.length();
			
		}
		
	}
	
	/**
	 * 
	 * @param g
	 * @param textColor
	 * @param backgroundColor
	 * @param line
	 * @param startX
	 * @param y
	 * @param colorOffset Since each line is drawn seperately, this is added to the index of each character in this line to get its index
	 * from the {@code ArrayList} Objects to get the proper colors.
	 *
	public void drawLine(Graphics2D g, Color textColor, Color highlightColor, Color caretColor, String line, 
			int startX, int y, int lineIndex, int lineIndexOffset, int caretPosition) {
		
		FontMetrics fm = g.getFontMetrics();
		
		int height = fm.getHeight();
		
		for(int i = 0; i < line.length(); i++) {
			 
			String letter = getCharacterAt(line, i);
			int letterWidth = getCharacterWidth(letter, fm);
			
			int totalIndexOffset = lineIndexOffset + i;
			
			Color actualHighlightColor = highlightColor;
			
			Color definedColor = backgroundColors.get(totalIndexOffset);
			
			if(definedColor != null) {
				actualHighlightColor = definedColor;
				definedColor = null;
//				Logger.log(Logger.DEBUG, "Defined Background Color found at: " + colorIndex + " with letter: \"" + letter + "\"");
			}
			
			Color actualTextColor = textColor;
			
			definedColor = foregroundColors.get(totalIndexOffset);
			
			if(definedColor != null) {
				actualTextColor = definedColor;
				definedColor = null;
//				Logger.log(Logger.DEBUG, "Defined Text/Foreground Color found at: " + colorIndex + " with letter: \"" + letter + "\"");
			}
			
			drawSingleString(g, letter, actualTextColor, actualHighlightColor, caretColor, startX, y, 
					letterWidth, height, lineIndex, totalIndexOffset, caretPosition);
			
			Rectangle hitbox = new Rectangle(startX - (letterWidth/2)/*For convenience*, y - height, letterWidth, height);
			
			hitboxes.add(totalIndexOffset, hitbox);
			
//			g.drawString("" + colorIndex, startX, y - height);
			
			startX += letterWidth;// Shifts x-value over for next letter.
			
		}
		
		drawSingleString(g, UNIVERSAL_LINE_SEPERATOR, textColor, highlightColor, caretColor, startX, y, 
				getCharacterWidth(UNIVERSAL_LINE_SEPERATOR, fm), height, lineIndex, lineIndexOffset + line.length(), caretPosition);
		
		g.setColor(Color.RED);
		g.drawString(line.length() + "", startX + getCharacterWidth(line.length() + "", fm), y);
		
	}
	
	private void drawSingleString(Graphics2D g, String character, Color textColor, Color highlightColor, Color caretColor, int x, int y, 
			int width, int height, int lineIndex, int indexOffset, int caretPosition) {
		
		g.setColor(highlightColor);
		g.fillRect(x, y - (height*2/3) + 1, width, height*2/3);
		
		g.setColor(textColor);
		g.drawString(character, x, y);
		
		if(caretPosition == (indexOffset)) {// + (lineIndex * UNIVERSAL_LINE_SEPERATOR.length()))) {
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
		
		case UNIVERSAL_LINE_SEPERATOR:
			width = fm.stringWidth(" ");// Use width of space character
			break;
			
		default:
			width = fm.stringWidth(character);
			break;
			
		}
		
		return width;
		
	}
	
	public void updateHighlights(Color highlightColor) {
		
		clearHighlights();// In case some already highlighted area is being unhighlighted.
		
		if(caretPosition == markedPosition) {
			// The two are the same, no need for anything more.
			return;
		}
		
		int start = 0;
		int end = 0;
		
		if(caretPosition > markedPosition) {
			
			start = markedPosition;
			end = caretPosition;
			
		} else {// No need for ==, already checked above.
			
			start = caretPosition;
			end = markedPosition;
			
		}
		
		for(int i = start; i < end; i++) {
			backgroundColors.put(i, highlightColor);
		}
		
	}
	
	public void clearHighlights() {
		
		for(int i = 0; i < content.length(); i++) {
			backgroundColors.put(i, null);
		}
		
	}
	
	public void moveCaretUp() {
		
		moveCaretVertically(-1);
		
	}
	
	public void moveCaretDown() {
		
		moveCaretVertically(1);
		
	}
	
	/**
	 * It gets real meaty down here.
	 * 
	 * @param direction hould be 1/-1 for now. 1 v | -1 ^. Probably works with magnitudes > 1 as well.
	 *
	private void moveCaretVertically(int direction) {
		
		if(lines.length <= 1) {
			
			if(direction < 0) {
				setCaretPosition(0);
			} else if(direction > 0) {
				setCaretPosition(getContent().length() - 1);
			}
			
		} else {
			
			int lineCaretIsOn = getLineCaretIsOn(lines, caretPosition);
			
			int lineIndexToMoveCaretTo = lineCaretIsOn + direction;
			
			if(lineIndexToMoveCaretTo < 0) {
				setCaretPosition(0);
				return;
			} else if(lineIndexToMoveCaretTo >= lines.length) {
				setCaretPosition(getContent().length() - 1);
				return;
			}
			
			int lineRelativeCaretPosition = getCaretPositionRelativeToLine(lines, caretPosition, lineCaretIsOn);
			
			String lineToMoveCaretTo = lines[lineIndexToMoveCaretTo];
			
			int newCaretPosition = 0;
			
			int positionOffset = 0;
			
			for(int i = 0; i < lineIndexToMoveCaretTo; i++) {
				positionOffset += lines[i].length();
			}
			
			if(lineRelativeCaretPosition < 0 || lineRelativeCaretPosition >= lineToMoveCaretTo.length()) {
				newCaretPosition = positionOffset + lineToMoveCaretTo.length() - 1;
//				Logger.log(Logger.DEBUG, "Trying to move caret to line: \"" + lineToMoveCaretTo + "\" with line relative caret position:" 
//						+ lineRelativeCaretPosition);
				
//			} else if(lineToMoveCaretTo.length() - 1 < 0 || lineToMoveCaretTo.length() > lines.length) {// Could even remove this.
//				newCaretPosition = positionOffset + lineRelativeCaretPosition;
				
			} else {
				newCaretPosition = positionOffset + lineRelativeCaretPosition + direction;// + direction for some offset.
				
			}
			
			setCaretPosition(newCaretPosition);
			
		}
		
	}
	
	public void moveCaretRight() {
		
		setCaretPosition(caretPosition + 1);
		
	}
	
	public void moveCaretLeft() {
		
		setCaretPosition(caretPosition - 1);
		
	}
	
	public void setCaretPositionByCoordinates(int x, int y) {
		
		for(int i = 0; i < content.length(); i++) {
			
			Rectangle charHitbox = hitboxes.get(i);
			
			if(charHitbox.contains(x, y)) {
				
				setCaretPosition(i);
				break;
			}
			
		}
		
	}
	
	/**
	 * @param lines
	 * @param caretPosition
	 * @param lineIndex
	 * @return Line-relative position of caret based on the line it is current on.
	 *
	public int getCaretPositionRelativeToLine(String[] lines, int caretPosition, int lineIndex) {
		
		try {
			lines[lineIndex].length();
		} catch(ArrayIndexOutOfBoundsException ex) {
			return caretPosition;
		}
		
		if(lineIndex <= 0 || lineIndex > lines.length - 1 || lines[lineIndex].length() <= 0) {
			return caretPosition;
		}
		
		int lengthOfLinesBeforeCaret = 0;
		
		for(int i = 0; i < lineIndex; i++) {
			lengthOfLinesBeforeCaret += lines[i].length();
		}
		
		return caretPosition - lengthOfLinesBeforeCaret;
		
	}
	
	/**
	 * 
	 * @return Index of the line that the Caret is on.
	 *
	public int getLineCaretIsOn(String[] lines, int caretPosition) {
		
		if(caretPosition <= -1) {
			return 0;
		}
		
		int indexOffset = 0;
		int lineCaretIsOn = 0;
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i] + " ";// Blank spot for new line.
			
			try {
				line.charAt(caretPosition - indexOffset);
				lineCaretIsOn = i;
				break;
			} catch(Exception ex) {
				indexOffset += line.length();
			}
			
		}
		
		return lineCaretIsOn;
	}
		
	public String getCharacterAt(int index) {
		return getCharacterAt(getContent(), index);
	}
	
	/**
	 * Convenience method for converting {@code char} to {@code String} automatically. Only gets used once so maybe we don't need the two.
	 * 
	 * @param s
	 * @param index
	 * @return
	 *
	public String getCharacterAt(String s, int index) {
		try {
			return String.valueOf(s.charAt(index));
		} catch(StringIndexOutOfBoundsException ex) {
			return "";
		}
	}
	
	/**
	 * Should probably convert from proper caret position to viewable caret position here.
	 * 
	 * @param caretPosition
	 * @see #renderableCaretPosition
	 *
	public void setCaretPosition(int caretPosition) {
		
		String content = getContent();
		
		int offsetLength = content.length() - (getLineCaretIsOn(lines, caretPosition) * lines.length) - 1;
		
		if(caretPosition > offsetLength) {
			
			caretPosition = offsetLength;
			
		}
		
		if(caretPosition < 0) {// Just in case the one up top messes up; temporary test for empty Strings.
			caretPosition = 0;
			
		}
		
		this.caretPosition = caretPosition;
		
	}
	
	public int getCaretPosition() {
		return caretPosition;
	}
	
	public void setForgroundColor(int index, Color color) {
		foregroundColors.put(index, color);
	}
	
	public void setBackgroundColor(int index, Color color) {
		backgroundColors.put(index, color);
	}
	
	/**
	 * Adds {@code s} at index {@code caretPosition}
	 * 
	 * 
	 * @param s
	 * @see #add(String, int)
	 *
	public void add(String s) {
		add(s, caretPosition);
	}
	
	/**
	 * Adds {@code s} at index {@code i} for the current object's {@code content}; conveniently inserts text.
	 * 
	 * @param s
	 * @param i
	 *
	public void add(String s, int i) {
		
		if(i >= content.length()) {
			append(s);
			
		} else if(i < 0) {
			setContent(s + getContent());
			
		} else {
			
			String content = getContent();
			
			String firstHalf = content.substring(0, i);
			String secondHalf = content.substring(i);
			
			setContent(firstHalf + s + secondHalf);
			
		}
		
	}
	
	public void append(String s) {
		
//		int i = content.length() <= 0? 0:content.length() - 1;
		
//		setContent(getContent().substring(0, i) + s);
		
		setContent(getContent() + s);
	}
	
	public void delete(int index) {
		
		String content = getContent();
		
		if(index < 0 || index >= content.length()) {
			return;
		}
		
		String firstHalf = content.substring(0, index);
		String secondHalf = content.substring(index + 1);
		
		setContent(firstHalf + secondHalf);
		
	}
	
	/**
	 * Uses {@code LINE_SEPERATOR}, the current System's line seperator, to split {@code s} into lines, adding 
	 * {@code UNIVERSAL_LINE_SEPERATOR} at the end of every line. Should only actually be used in the setContent(String) method.
	 * 
	 * @param s {@code String} object to be turned into lines.
	 * @return An array of {@code String}s in which each element represents a line.
	 *
	private String[] getLines(String s, String regex) {
		
		ArrayList<String> holder = new ArrayList<String>();
		
		int lineCounter = 0;
		int previousMark = 0;
		
		for(int i = 0; i < s.length() - regex.length() + 1; i++) {
			
			String currentlyChecked = s.substring(i, i + regex.length());
			
			if(currentlyChecked.equals(regex)) {
				previousMark = i;
				lineCounter++;
			}
			
		}
		
		if(!s.endsWith(regex)) {
			holder.add(s.substring(previousMark, s.length()));
			lineCounter++;
		}
		
		String[] lines = holder.toArray(new String[lineCounter]);
		
		String[] splitLines = s.split(regex);
		
		for(int i = 0; i < lineCounter; i++) {
			
			try {
				lines[i] = splitLines[i];
			} catch(Exception ex) {
				lines[i] = "";
			}
			
		}
		
		return lines;
		
	}
	
	public String[] getLines() {
		return lines;
	}
	
	public void setContent(String content) {
		String[] lines = getLines(content, UNIVERSAL_LINE_SEPERATOR);
		
		this.lines = lines;
		
		this.content = content;
		
	}
	
	public String getContent() {
		return content;
	}
	
	public void setMarkedPosition(int markedPosition) {
		this.markedPosition = markedPosition;
	}
	
	public String getSelectedContent() {
		
		String selectedContent = "";
		
		for(int i = 0; i < content.length(); i++) {
			
			if(backgroundColors.get(i) != null) {
				selectedContent += getCharacterAt(content, i);
			}
			
		}
		
		return selectedContent;
		
	}// I want to be wit chu forever Dru. and hold chu close and never have to let go. <3
	/**/
	
}
