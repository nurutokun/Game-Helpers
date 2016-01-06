package com.rawad.gamehelpers.utils.strings;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.BreakIterator;
import java.util.ArrayList;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.text.TextRender;
import com.rawad.gamehelpers.utils.Util;

public class RenderedString {
	
	private static final TextRender render = TextRender.instance();
	
	private String content;
	
	private ArrayList<AttributedString> lines;// LineBreakMeasurer?
	private LineBreakMeasurer lineBreaker;
	
	/** {@code Rectangle} that perfectly encapsulates this text. */
	private Rectangle boundingBox;
	
	/** Line index that the caret is on. */
	private int line;
	/** Position in line that the caret is on. */
	private int positionInLine;
	
	private HorizontalAlignment alignmentX;
	private VerticalAlignment alignmentY;
	
	private MouseEvent mouseInfo;
	
	private Font font;
	
	private Color color;
	
	private int size;
	
	private boolean hideOutOfBounds;
	
	private boolean update;
	
	public RenderedString(String content) {
		
		line = 0;
		positionInLine = 0;
		
		alignmentX = HorizontalAlignment.LEFT;
		alignmentY = VerticalAlignment.TOP;
		
		font = render.getDefaultFont();// Questionable... So is the .instance() thing.
		color = Color.BLACK;
		size = 15;
		hideOutOfBounds = false;
		
		boundingBox = new Rectangle();
		
		update = false;
		
		this.content = "";// Must set the basic variables first (font, color, etc).
		
		setContent(content);
		
	}
	
	/**
	 * Update this object's {@code mouseInfo} instance with the given {@code e} parameter.
	 * 
	 * @param e
	 */
	private void update(MouseEvent e) {
		this.mouseInfo = e;
	}
	
	/**
	 * Creates new {@code MouseEvent} to be passed to the overloaded {@code update(MouseEvent)} methodfor updating.
	 * 
	 */
	public void update(FontRenderContext frc, FontMetrics fm, int lineHeight) {
		this.update(new MouseEvent());
		
		if(update) {
			setLineBreakMeasurer(getLines(), frc);
			
			updateBoundingBox(getLineBreaker(), getContent(), lineHeight, fm);
			
			Logger.log(Logger.DEBUG, "Updated: \"" + content + "\"");
			
			update = false;
			
		}
		
	}
	
	public void setContent(String content) {
		
		if(!this.content.equals(content) || content.isEmpty()) {// Forces update with empty strings.
			
			updateLines(content);
			
			Logger.log(Logger.DEBUG, "gonna need to update: \"" + content + "\"");
			
			update = true;
			
		}
		
		this.content = content;
	}
	
	private void updateLines(String content) {
		
		int lineCount = (content.length() - content.replace(Util.NL, "").length())/Util.NL.length() + 1;
		
		lines = new ArrayList<AttributedString>(lineCount);
		
		for(int lineIndex = 0, nextLineIndex = content.indexOf(Util.NL);
				lineIndex < lineCount;// indexOf() called twice, could change with do/while loop.
				lineIndex++, nextLineIndex = content.indexOf(Util.NL)) {
			
			boolean finalLine = nextLineIndex < 0;// nextLineIndex = -1
			
			if(finalLine) {
				nextLineIndex = content.length();// If there is nothing on this line, just make it a safe 0 index.
			}
			
			String line = content.substring(0, nextLineIndex);
			
			AttributedString attLine = new AttributedString(line);
			
			if(!(line.length() == 0)) {
				attLine.addAttribute(TextAttribute.FONT, getFont());// Can further define sections in line with indices.
				attLine.addAttribute(TextAttribute.FOREGROUND, getColor());
				attLine.addAttribute(TextAttribute.SIZE, getSize());
			}
			
			lines.add(lineIndex, attLine);
			
 			if(lineCount > 1 && !finalLine) {// Ensures there's atleast one NL character and we're not hitting the final
 				// line which has no NL character.
				nextLineIndex += Util.NL.length();
			}
			
			content = content.substring(nextLineIndex);
			
		}
		
	}
	
	private void setLineBreakMeasurer(ArrayList<AttributedString> lines, FontRenderContext frc) {
		
//		String string = content.equals("")? Util.NL:content;// In case it's empty...
		
		AttributedString temp = new AttributedString("");
		
		lineBreaker = new LineBreakMeasurer(temp.getIterator(),// TODO: Figure out indexing of lineBreaker.
				BreakIterator.getWordInstance(), frc);
		
		lineBreaker.setPosition(temp.getIterator().getBeginIndex());
		
		lineBreaker.deleteChar(temp.getIterator(), lineBreaker.getPosition());
		
		int i = 0;
		
		for(AttributedString line: lines) {
			
			AttributedCharacterIterator it = line.getIterator();
			
			while(it.current() != AttributedCharacterIterator.DONE) {
				
				char attChar = it.next();
				
			}
			
			if(it.getEndIndex() > 0) {// Empty String otherwise. getEndIndex() gets length of String.
				lineBreaker.insertChar(it, i);
				
			}
			
			i++;
			
		}
		
	}
	
	private void updateBoundingBox(LineBreakMeasurer lineBreaker, String content, int lineHeight, FontMetrics fm) {
		
		int width = 0;
		int height = 0;
		
		int length = content.length();
		
		int prevLine = 0;
		
		while(lineBreaker.getPosition() < length) {
			
			int nextLine = content.indexOf(Util.NL, prevLine);
			
			if(nextLine <= -1) {
				nextLine = length;
			} else {
				nextLine += Util.NL.length();
			}
			
			TextLayout layout = lineBreaker.nextLayout(Game.SCREEN_WIDTH, nextLine, false);
			
			double advance = layout.getAdvance();
			
//			Logger.log(Logger.DEBUG, "advance (" + advance + "), and width (" + width + ") for string: \n\""
//					+ content.substring(prevLine, nextLine) + "\", actual advance: "
//					+ fm.stringWidth(content.substring(prevLine, nextLine) + "."));
			
			if(advance > width) {
				width = (int) Math.round(advance);
			}
			
			prevLine = nextLine;
			
			height +=  lineHeight;// Assume each layout is a line because that is the point.
			// TODO: Change based on alignment.
			
		}
		
		boundingBox.setBounds(boundingBox.x, boundingBox.y, width, height);// Use same x,y they're irrelevant for now.
		
	}
	
	public String getContent() {
		return content;
	}
	
	public ArrayList<AttributedString> getLines() {
		return lines;
	}
	
	/**
	 * @return the boundingBox
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	
	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}
	
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * @return the hideOutOfBounds
	 */
	public boolean shouldHideOutOfBounds() {
		return hideOutOfBounds;
	}
	
	/**
	 * @param hideOutOfBounds the hideOutOfBounds to set
	 */
	public void setHideOutOfBounds(boolean hideOutOfBounds) {
		this.hideOutOfBounds = hideOutOfBounds;
	}
	
	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * @param line the line to set
	 */
	public void setLine(int line) {
		this.line = line;
	}
	
	/**
	 * @return the positionInLine
	 */
	public int getPositionInLine() {
		return positionInLine;
	}
	
	/**
	 * @param positionInLine the positionInLine to set
	 */
	public void setPositionInLine(int positionInLine) {
		this.positionInLine = positionInLine;
	}
	
	/**
	 * @return the alignment
	 */
	public HorizontalAlignment getHorizontalAlignment() {
		return alignmentX;
	}
	
	/**
	 * @param alignment the alignment to set
	 */
	public void setHorizontalAlignment(HorizontalAlignment alignment) {
		this.alignmentX = alignment;
	}
	
	/**
	 * @return the alignmentY
	 */
	public VerticalAlignment getVerticalAlignment() {
		return alignmentY;
	}

	/**
	 * @param alignment the alignment to set
	 */
	public void setVerticalAlignment(VerticalAlignment alignment) {
		this.alignmentY = alignment;
	}
	
	/**
	 * @return the mouseInfo
	 */
	public MouseEvent getMouseInfo() {
		return mouseInfo;
	}
	
	public LineBreakMeasurer getLineBreaker() {
		lineBreaker.setPosition(0);
		
		return lineBreaker;
	}
	
	public static enum HorizontalAlignment {
		
		RIGHT,
		CENTER,
		LEFT;
		
	}
	
	public static enum VerticalAlignment {
		
		TOP,
		CENTER;
		// Bottom?
	}
	
	public void setCaretPosition(int line, int positionInLine) {}
	
	public void moveCaretUp() {}
	public void moveCaretDown() {}
	public void moveCaretRight() {}
	public void moveCaretLeft() {}
	
	public void newLine() {}
	public void add(String contentToAdd) {}
	
}
