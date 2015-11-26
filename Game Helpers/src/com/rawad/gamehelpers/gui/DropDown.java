package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.gamehelpers.utils.strings.DrawableString;

public class DropDown extends Button {
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	private Menu menu;
	
	private String[] options;
	
	private String currentSelected;
	
	private int widthSection;
	
	private boolean menuDown;
	
	public DropDown(String id, int x, int y, int width, int height, String defaultOption, String... options) {
		super(id, x, y, width, height);
		
		this.options = options;
		
		widthSection = width/3;// Use a third of the total bar as the arrow clicking spot
		
		hitbox.setBounds(this.x + (2 * widthSection), this.y, widthSection, height);
		
		this.menu = new Menu(this.options, this.x + 5, this.y + height - 5, widthSection * 2);
		currentSelected = defaultOption;
		
	}
	
	public DropDown(String id, int x, int y, int width, int height, String... options) {
		this(id, x, y, width, height, options[0], options);
	}
	
	public DropDown(String id, int x, int y, int defaultOption, String... options) {
		this(id, x, y, ResourceManager.getTexture(BACKGROUND_LOCATION).getWidth(),
				ResourceManager.getTexture(BACKGROUND_LOCATION).getHeight(), options[defaultOption], options);
	}
	
	static {
		
		String dropdownBase = ResourceManager.getString("DropDown.base");
		
		BACKGROUND_LOCATION = loadTexture(dropdownBase, ResourceManager.getString("Gui.background"));
		FOREGROUND_LOCATION = loadTexture(dropdownBase, ResourceManager.getString("Gui.foreground"));
		ONCLICK_LOCATION = loadTexture(dropdownBase, ResourceManager.getString("Gui.onclick"));
		DISABLED_LOCATION = loadTexture(dropdownBase, ResourceManager.getString("Gui.disabled"));
		
	}
	
	@Override
	public void update(MouseEvent me, KeyboardEvent ke) {
		super.update(me, ke);
		
		int x = me.getX();
		int y = me.getY();
		
		boolean mouseButtonDown = me.isLeftButtonDown();
		
		if(isMenuDown()) {
			hitbox.setBounds(menu.getX(), menu.getY(), menu.getWidth(), menu.getHeight());
			
			if(highlighted) {
				menu.mouseHover(y);
			}
			
			// Or could set highlighted false.
			
		} else {
			hitbox.setBounds(this.x + (2 * widthSection), this.y, widthSection, height);
		}
		
		if(isMenuDown() && !intersects(x, y) && !mouseDragged && mouseButtonDown) {
			setMenuDown(false);
			
			me.consume();// Makes sense. Still doesn't really work in world editor...
		}
		
		text.setContent(getId() + ":" + Util.NL + currentSelected);
		
	}
	
	@Override
	public void render(Graphics2D g) {
		super.drawBase(g, BACKGROUND_LOCATION, FOREGROUND_LOCATION, ONCLICK_LOCATION, DISABLED_LOCATION);
		
		if(isMenuDown()) {
			menu.render(g);
		}
		
		this.text.render(g, Color.WHITE, Util.TRANSPARENT, Util.TRANSPARENT, new Rectangle(x, y, width - (width/3), height), centerText, 
				hideOutOfBoundsText);
		
		super.drawHitbox(g);
		
	}
	/*/
	@Override
	protected void drawText(Graphics2D g, Color textColor, String text, int width, int height) {
		
		Font f = g.getFont();
		FontMetrics fm = g.getFontMetrics(f);
		
		int stringWidth = fm.stringWidth(text);
		int stringHeight = fm.getHeight();
		
		int widthSection = 2*(width/3);// Use only two thirds of the bar; .
		int heightSections = height/stringHeight;
		
		int heightSection = height/heightSections;
		
		int stringX = x + ((widthSection - stringWidth)/2);
		int stringY = y + (heightSection * 2);
		
		g.setColor(textColor);
		g.drawString(text, stringX, stringY - fm.getHeight());
		
		stringWidth = fm.stringWidth(currentSelected);
		
		stringX = x + ((widthSection - stringWidth)/2);
		stringY = y + (heightSection * 3) - stringHeight;
		
		g.drawString(currentSelected, stringX, stringY);
		
	}/**/
	
	@Override
	public void mouseExited() {
		super.mouseExited();
		
		menu.mouseExited();
		
	}
	
	public void calculateSelectedItem(int y) {
		
		currentSelected = menu.getItemAt(y);
		
	}
	
	public void setCurrentSelectedItem(String selection) {
		
		for(String item: menu.getItems()) {
			
			if(item.equals(selection)) {
				currentSelected = selection;// Just to make sure it can be selected.
				break;
			}
			
		}
		
	}
	
	public String getCurrentSelectedItem() {
		return currentSelected;
	}
	
	public void setMenuDown(boolean menuDown) {
		this.menuDown = menuDown;
	}
	
	public boolean isMenuDown() {
		return menuDown;
	}
	
	private class Menu {
		
		private static final int STRING_HEIGHT = 20;
		
		private DrawableString drawableItems;// TODO: Properly implement this.
		private String[] items;
		
		private int x;
		private int y;
		
		private int width;
		private int height;
		
		private int highlighted;
		
		private boolean hovered;
		
		public Menu(String[] items, int x, int y, int width) {
			this.items = items;
			drawableItems = new DrawableString();
			
			for(int i = 0; i < items.length; i++) {
				drawableItems.setContent(drawableItems.getContent() + items[i]);
				
				if(!(i >= items.length - 1)) {
					drawableItems.setContent(drawableItems.getContent() + Util.NL);
				}
				
			}
			
			this.x = x;
			this.y = y;
			
			this.width = width;
			this.height = items.length * STRING_HEIGHT;// each item is 20 pixels
			
		}
		
		public void render(Graphics2D g) {
			
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(x + 1, y + 1, width - 1, height - 1);
			
			g.setColor(Color.GRAY);
			
			if(hovered) {
				g.fillRect(x + 1, y + highlighted * STRING_HEIGHT + 1, width - 1, STRING_HEIGHT - 1);
			}
			
			g.setColor(Color.BLACK);
			g.drawRect(x, y, width, height);
			
//			drawableItems.render(g, Color.BLACK, TRANSPARENT, TRANSPARENT, hitbox, false, false);
			
			for(int i = 0; i < items.length; i++) {
				g.drawString(items[i], x + 5, y + (height/items.length * i) + STRING_HEIGHT - 5);
			}
			
		}
		
		public void mouseHover(int y) {
			
			highlighted = (y - this.y)/STRING_HEIGHT;
			hovered = true;
			
		}
		
		public void mouseExited() {
			
			hovered = false;
			
		}
		
		public String getItemAt(int y) {
			
			int yIndex = y - this.y;
			
			return items[yIndex/STRING_HEIGHT];
			
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		public int getWidth() {
			return width;
		}
		
		public int getHeight() {
			return height;
		}
		
		public String[] getItems() {
			return items;
		}
		
	}
	
}
