package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.resources.ResourceManager;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.gamehelpers.utils.strings.RenderedString.HorizontalAlignment;
import com.rawad.gamehelpers.utils.strings.RenderedString.VerticalAlignment;

public class DropDown extends Button {
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	private Menu menu;
	
	private Rectangle textBoundBox;
	
	private int widthSection;
	
	public DropDown(String id, int x, int y, int width, int height, String defaultOption, String... options) {
		super(id, x, y, width, height);
		
		widthSection = width/3;// Use a third of the total bar as the arrow clicking spot
		
		hitbox.setBounds(this.x + (2 * widthSection), this.y, widthSection, height);
		
		textBoundBox = new Rectangle(this.x, this.y, widthSection * 2, height);
		
		menu = new Menu(this, options, this.x + 5, this.y + height - 5, widthSection * 2);
		menu.setSelectedItem(defaultOption);
		
		setSelectedItem(menu.getSelectedItem());
		
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
	public void render(Graphics2D g) {
		super.drawBase(g, BACKGROUND_LOCATION, FOREGROUND_LOCATION, ONCLICK_LOCATION, DISABLED_LOCATION);
		
		// TODO: More inherited string rendering here.
//		this.text.render(g, Color.WHITE, Util.TRANSPARENT, Util.TRANSPARENT, new Rectangle(x, y, width - (width/3), 
//				height), centerText, hideOutOfBoundsText);
		
		super.drawHitbox(g);
		
	}
	
	@Override
	protected void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		setUpdate(menu.isDown());
		// Only update the menu; it will then tell its parent to start updating again when menu is up.
		
		menu.setDown(!menu.isDown());
		
	}
	
	private void setSelectedItem(String selectedItem) {
		
		text.setContent(getId() + ":" + Util.NL + selectedItem);
		
	}
	
	public void setCurrentSelectedItem(String selection) {
		
		for(String item: menu.getItems()) {
			
			if(item.equals(selection)) {
				menu.setSelectedItem(selection);// Just to make sure it can be selected.
				break;
			}
			
		}
		
	}
	
	public String getCurrentSelectedItem() {
		return menu.getSelectedItem();
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	@Override
	public void onAdd(GuiManager manager) {
		super.onAdd(manager);
		
		manager.addComponent(getMenu());// Could also be put behind drop down with (manager.getComponents().size() - 1).
		
	}
	
	@Override
	public Rectangle getTextBoundBox() {
		return textBoundBox;
	}
	
	public class Menu extends Button {
		
		private static final int STRING_HEIGHT = 20;
		
		private DropDown parent;
		
		private String[] items;
		
		private String currentSelected;
		
		private int highlighted;
		
		private int totalHeight;
		
		public Menu(DropDown parent, String[] items, int x, int y, int width) {
			super("Menu", x, y, width, items.length * STRING_HEIGHT);
			
			this.parent = parent;
			
			this.items = items;
			
//			text.setFont(Font.getFont(Font.SERIF));// Can't use this because the graphics can't handle it...
			text.setHorizontalAlignment(HorizontalAlignment.LEFT);
			text.setVerticalAlignment(VerticalAlignment.TOP);
			
			String content = Util.getStringFromLines(items, Util.NL, false);
			text.setContent(content);
			
			totalHeight = 0;
			
			setDown(false);
			
		}
		
		@Override
		public void update(MouseEvent me, KeyboardEvent ke) {
			super.update(me, ke);
			
			if(!intersects(me.getX(), me.getY()) && me.isLeftButtonDown() && !mouseDragged) {// Don't have to check down
				
				setDown(false);
				
				me.consume();
				
			}
			
			Rectangle textBoundingBox = text.getBoundingBox();
			
			if(isHovered()) {
				highlighted = (me.getY() - this.y) / textBoundingBox.height / items.length;
				// Set text.line highlighted with this.
			}
				
			hitbox.setBounds(x, y, width, textBoundingBox.height);
			
		}
		
		public void setDown(boolean menuDown) {
			
			this.setUpdate(menuDown);
			this.setRender(menuDown);
			
			parent.setUpdate(!menuDown);
			
		}
		
		public boolean isDown() {
			return this.shouldUpdate();// Could add (&& shouldRender).
		}
		
		@Override
		public void render(Graphics2D g) {
			// Don't render all the Button stuff with super.render();
			
			g.setColor(Color.LIGHT_GRAY);
			g.fill(getTextBoundBox());
			
			if(isHovered()) {// TODO: Should be done by RenderedString.
				g.setColor(Color.GRAY);
				g.fillRect(x + 1, y + (highlighted * totalHeight / items.length) + 1, width - 2, 
						totalHeight / items.length - 2);
			}
			
			g.setColor(Color.RED);
			g.drawRect(x, y, text.getBoundingBox().width, text.getBoundingBox().height);
			
			drawHitbox(g);
			
		}
		
		@Override
		protected void mouseClicked(MouseEvent e) {
			super.mouseClicked(e);
			
			setSelectedItem(getItemAt(e.getY()));
			
			setDown(false);
			
		}
		
		public String getItemAt(int y) {
			
			int yIndex = y - this.y;// TODO: Should be done by RenderedString.
			
			return items[yIndex/STRING_HEIGHT];
			
		}
		
		public void setSelectedItem(String currentSelected) {
			this.currentSelected = currentSelected;
			
			parent.setSelectedItem(currentSelected);
			
		}
		
		public String getSelectedItem() {
			return currentSelected;
		}
		
		@Override
		public void setX(int x) {
			this.x = x;
		}
		
		@Override
		public void setY(int y) {
			this.y = y;
		}
		
		public String[] getItems() {
			return items;
		}
		
	}
	
}
