package com.rawad.gamehelpers.display;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public enum Cursors {
	
	DEFAULT(new Cursor(Cursor.DEFAULT_CURSOR)),
	BLANK(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), 
			"Blank Cursor")),
	TEXT(new Cursor(Cursor.TEXT_CURSOR));
	
	private final Cursor cursor;
	
	private Cursors(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public Cursor getValue() {
		return cursor;
	}
	
}
