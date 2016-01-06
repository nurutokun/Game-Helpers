package com.rawad.gamehelpers.utils.strings;

import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * ...?
 * 
 * @author Rawad
 */
public class PositionableString {
	
	private String content;
	
	private String[] lines;
	
	/** Each {@code Point} corresponds to its maching character in the content object. */
	private ArrayList<Point> positions;
	
	public PositionableString(String content) {
		
		this.content = content;
		
		positions = new ArrayList<Point>();
		
	}
	
	public void positionText(String[] lines, FontMetrics fm, ArrayList<Point> positions, Rectangle boundingBox, 
			PositioningHint howToPosition) {
		
		for(int i = 0; i < lines.length; i++) {
			
			String line = lines[i];
			
			int x = boundingBox.x;
			int y = boundingBox.y + (fm.getHeight() * i);
			
			for(int j = 0; j < line.length(); j++) {
				
				
				
			}
			
		}
		
	}
	
	public void positionText(FontMetrics fm, Rectangle boundingBox, PositioningHint howToPosition) {
		this.positionText(lines, fm, positions, boundingBox, howToPosition);
	}
	
	public static enum PositioningHint {
		
		RIGHT,
		CENTER,
		LEFT;
		
	}
	
}
