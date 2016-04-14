package com.rawad.gamehelpers.input;

import javafx.scene.Cursor;

public enum Cursors {
	
	DEFAULT(Cursor.DEFAULT),
	BLANK(Cursor.NONE);
	
	private final Cursor cursor;
	
	private Cursors(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public Cursor getValue() {
		return cursor;
	}
	
}