package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.gamehelpers.gamemanager.Game;
import com.rawad.gamehelpers.gamemanager.GameManager;
import com.rawad.gamehelpers.input.event.KeyboardEvent;
import com.rawad.gamehelpers.input.event.MouseEvent;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

public abstract class GuiComponent {
	
	private static final String BASE_FOLDER_PATH = "Game Helpers/" + ResourceManager.getString("GameHelpers.res") + 
			ResourceManager.getString("Gui.base");// Both should be accessed through the load texture method only.
	private static final String TEXTURE_FILE_EXTENSION = ResourceManager.getString("GameHelpers.ext");
	
	protected String id;
	
	protected Rectangle hitbox;// For hit-detection with mouse; dynamic
	
	protected int texture;
	
	// Used for drawing, exclusively
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	//
	
	protected int prevMouseX;
	protected int prevMouseY;
	
	protected boolean mouseDragged;
	
	private boolean prevPressed;
	private boolean hovered;
	
	private boolean update;
	private boolean render;
	
	public GuiComponent(String id, int x, int y, int width, int height) {
		
		this.id = id;
		
		this.width = width;
		this.height = height;
		
		setX(x);
		setY(y);
		
		this.hitbox = new Rectangle();
		updateHitbox();
		
		prevPressed = false;
		hovered = false;
		
		update = true;
		render = true;
		
		texture = ResourceManager.loadTexture("");// Loads unknown texture.
		
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
	 * 
	 */
	public void update(MouseEvent me, KeyboardEvent ke) {
		
		complexUpdate(me, ke);
		
//		simpleUpdate(me);
		
	}
	
	@SuppressWarnings("unused") @Deprecated
	private void simpleUpdate(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		boolean mouseButtonDown = e.isLeftButtonDown();
		
		if(intersects(x, y) && mouseButtonDown) {
			
			mouseClicked(e);
			
			e.consume();
		}
		
	}
	
	private void complexUpdate(MouseEvent me, KeyboardEvent ke) {
		
		int x = me.getX();
		int y = me.getY();
		
		boolean mouseButtonDown = me.isLeftButtonDown();
		
		if(!intersects(x, y) && mouseButtonDown && prevMouseX != x && prevMouseY != y) {// Mouse dragged outside component
			mouseDragged = true;
		}
		
		if(intersects(x, y) && !me.isConsumed()) {
			
			if(mouseButtonDown && hovered) {
				
				if(!prevPressed && !mouseDragged) {// If not already pressed
					mousePressed(me);
					
				}
				
				prevPressed = true;
				
			} else {
				
				if(prevPressed) {
					mouseReleased(me);
					
					if(intersects(prevMouseX, prevMouseY) && !mouseDragged) {
						mouseClicked(me);
						
					}
					
					mouseDragged = false;
					prevPressed = false;
					
				}
				
				
			}
			
			if(!hovered) {
				mouseEntered();
				
				hovered = true;
				
			}
			
			me.consume();// Yes.
			
		} else {
			
			if(hovered) {
				mouseExited();
				
				hovered = false;
			}
			
			if(!mouseButtonDown) {
				mouseDragged = false;
			}
			
			prevPressed = false;
			
		}
		
		prevMouseX = x;
		prevMouseY = y;
		
	}
	
	public void render(Graphics2D g) {
		
		drawHitbox(g);
		
	}
	
	protected void drawHitbox(Graphics2D g) {
		
		Game game = GameManager.instance().getCurrentGame();
		
		if(game.isDebug()) {
			g.setColor(Color.BLACK);
			g.drawRect(hitbox.x - 1, hitbox.y - 1, hitbox.width + 1, hitbox.height + 1);
		}
		
	}
	
	protected void mouseClicked(MouseEvent e) {}// MouseEvent mainly to get mouse position, for now.
	
	protected void mousePressed(MouseEvent e) {}
	
	protected void mouseReleased(MouseEvent e) {}
	
	protected void mouseEntered() {}
	
	protected void mouseExited() {}
	
	/**
	 * Called right after this {@code GuiComponent} is added to the given {@code manager} object.
	 * 
	 * @param manager
	 */
	public void onAdd(GuiManager manager) {}
	
	public boolean intersects(int x, int y) {
		
		return hitbox.contains(x, y);
		
	}
	
	public String getId() {
		return id;
	}
	
	public void setX(int x) {
		this.x = x - (width/2);
	}
	
	public void setY(int y) {
		this.y = y - (height/2);
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setUpdate(boolean update) {
		this.update = update;
	}
	
	public boolean shouldUpdate() {
		return update;
	}
	
	public void setRender(boolean render) {
		this.render = render;
	}
	
	public boolean shouldRender() {
		return render;
	}
	
	/**
	 * Temporary. Mainly used for TextLabel right now.
	 * 
	 * @param texture
	 */
	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	public void updateHitbox() {
		hitbox.setBounds(x, y, width, height);
	}
	
	public boolean isHovered() {
		return hovered;
	}
	
	@Deprecated
	protected static BufferedImage getScaledImage(BufferedImage original, int width, int height) {
		
		Image scaled = original.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		return toBufferedImage(scaled);
		
	}
	
	@Deprecated
	private static BufferedImage toBufferedImage(Image image) {
		
		if(image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D bGr = bImage.createGraphics();
		
		bGr.drawImage(image, 0, 0, null);
		bGr.dispose();
		
		return bImage;
		
	}
	
	protected static BufferedImage getMonotoneImage(Color color, int width, int height) {
		
		BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int i = 0; i < temp.getWidth(); i++) {
			for(int j = 0; j < temp.getHeight(); j++) {
				
				temp.setRGB(i, j, color.getRGB());
				
			}
		}
		
		return temp;
		
	}
	
	public static int loadTexture(String textureFolder, String textureFileName) {
		
		String filePath = BASE_FOLDER_PATH + textureFolder + textureFileName + TEXTURE_FILE_EXTENSION;
		
		return ResourceManager.loadTexture(filePath);
	}
	
	@Deprecated
	protected static BufferedImage[] loadTextures(String textureFolder, String[] textureFileNames) {
		
		BufferedImage[] loadedImages = new BufferedImage[textureFileNames.length];
		
		for(int i = 0; i < loadedImages.length; i++) {
			
			BufferedImage temp = null;
			
			try {
				
				temp = ImageIO.read(new File(BASE_FOLDER_PATH + textureFolder + textureFileNames[i] + TEXTURE_FILE_EXTENSION));
				
			} catch(Exception ex) {
				
				Logger.log(Logger.WARNING, ex.getLocalizedMessage() + "; couldn't load image file.");
				
			} finally {
				loadedImages[i] = temp;
			}
			
		}
		
		return loadedImages;
	}
	
	@Deprecated
	protected static BufferedImage[] loadTextures(String textureFolder) {
		return loadTextures(textureFolder, new String[]{ResourceManager.getString("Gui.background"),
				ResourceManager.getString("Gui.foreground"), ResourceManager.getString("Gui.onclick")});
	}
	
}
