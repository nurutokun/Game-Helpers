package com.rawad.gamehelpers.renderengine.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;

import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.renderengine.Render;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.strings.RenderedString;
import com.rawad.gamehelpers.utils.strings.RenderedString.HorizontalAlignment;
import com.rawad.gamehelpers.utils.strings.RenderedString.VerticalAlignment;

public class TextRender extends Render {
	
	private static final String XENOTRON = "Xenotron";
	
	private static TextRender instance;
	
	private Font font;
	
	private TextRender() {
		
		GameHelpersLoader loader = GameManager.instance().getCurrentGame().getLoader(GameHelpersLoader.BASE);
		
		initFont(loader);
		
	}
	
	private void initFont(Loader loader) {
		
		try {
			
			font = Font.createFont(Font.TRUETYPE_FONT, loader.loadFontFile(XENOTRON));
			
			font = font.deriveFont(12f);// Makes size of font 12f; float so it is interpreted as size and not style.
			
		} catch (FontFormatException | IOException ex) {
			
			font = Font.getFont(Font.SERIF);
			
			Logger.log(Logger.WARNING, "Couldn't load font \"" + XENOTRON + "\" in the text render.");
			ex.printStackTrace();
			
		}
		
	}
	
	/**
	 * 
	 * @param g
	 * @param string Should include information on how the {@code String} should be rendered.
	 * @param boundingBox
	 */
	public void render(Graphics2D g, RenderedString string, Rectangle boundingBox) {
		
		final int lineHeight = g.getFontMetrics().getHeight();
		
		string.update(g.getFontRenderContext(), g.getFontMetrics(), lineHeight);// Prepare string for rendering.
		
		float y = boundingBox.y;
		
		ArrayList<AttributedString> lines = string.getLines();
		
		for(int i = 0; i < lines.size(); i++) {
			
			AttributedString line = lines.get(i);
			
			if(!(line.getIterator().getEndIndex() <= 0)) {// String is empty
				Rectangle hitbox = renderLine(g, string, line, i, boundingBox.x, y, boundingBox.width, boundingBox.height);
				
				if(GameManager.instance().getCurrentGame().isDebug()) {
					g.setColor(Color.CYAN);
					g.draw(hitbox);
				}
				
			} else {
				Logger.log(Logger.DEBUG, "\"" + string.getContent() + "\" has empty line at index: " + i);
			}
			
			y += lineHeight;
			
		}
		
	}
	
	// TODO: Caret rendering. Mouse clicking (+ highlight) and keyboard movement.
	private Rectangle renderLine(Graphics2D g, RenderedString string, AttributedString attLine, int index, float x, 
			float y, int width, int height) {
		
		Rectangle hitbox = new Rectangle();
		hitbox.x = (int) x + width;// maximum, ensures that the check below can be true.
		
		LineBreakMeasurer lineBreaker = string.getLineBreaker();
		
		int lineHeight = g.getFontMetrics().getHeight();
		
		float drawPosY = getPosY(string.getVerticalAlignment(), height,
				string.getBoundingBox().height, y + (lineHeight/4));// Offset initial y by lineHeight/4 because reasons.
		
		hitbox.y = (int) drawPosY;
		
		while(lineBreaker.getPosition() < attLine.getIterator().getEndIndex()) {
			
			TextLayout textLayout = lineBreaker.nextLayout(width);
			
			if(textLayout.getAdvance() > hitbox.width) {
				hitbox.width = (int) textLayout.getAdvance();
			}
			
			float drawPosX = getPosX(string.getHorizontalAlignment(), width, x, textLayout.getAdvance());
			drawPosY += textLayout.getAscent();
			
			if(drawPosX < hitbox.x) {
				hitbox.x = (int) drawPosX;
			}
			
			textLayout.draw(g, drawPosX, drawPosY);
			
			if(index == string.getLine()) {
				
				TextHitInfo hit = TextHitInfo.beforeOffset(string.getPositionInLine());
				
				Shape caretShape = textLayout.getCaretShape(hit);
				
				g.setColor(Color.GREEN);
				g.draw(caretShape);
				
			}
			
			drawPosY += textLayout.getDescent() + textLayout.getLeading();
			hitbox.height += textLayout.getDescent() + textLayout.getAscent() + textLayout.getLeading();
			
			if(string.shouldHideOutOfBounds()) {// Hide out-of-bounds text by only rendering the first layout that fits.
				break;
			}
			
		}
		
		return hitbox;
		
	}
	
	private float getPosX(HorizontalAlignment alignment, int width, float x, float advance) {
		
		switch(alignment) {
		
		case RIGHT:
			
			return x + width - advance;
			
		case CENTER:
			
			return x + (width/2 - (advance/2));
			
		case LEFT:
			default:
			
			return x;
			
		}
	}
	
	private float getPosY(VerticalAlignment alignment, int height, int linesHeight, float y) {
		
		switch(alignment) {
		
		case CENTER:
			
			return y + (height/2 - (linesHeight/2));
			
		case TOP:
			default:
			return y;
			
		}
		
	}
	
	public Font getDefaultFont() {
		return font;
	}
	
	public static TextRender instance() {
		
		if(instance == null) {
			instance = new TextRender();
		}
		
		return instance;
		
	}
	
}
