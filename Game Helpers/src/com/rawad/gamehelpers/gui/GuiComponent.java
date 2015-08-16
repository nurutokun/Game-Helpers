package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.input.MouseEvent;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.resources.ResourceManager;

public abstract class GuiComponent {
	
	protected static final String BASE_FOLDER_PATH = ResourceManager.getString("GameHelpers.res") + ResourceManager.getString("Gui.base");
	protected static final String TEXTURE_FILE_EXTENSION = ResourceManager.getString("GameHelpers.ext");
	
	protected String id;
	
	protected Rectangle hitbox;// For hit-detection with mouse; dynamic
	
	// Used for drawing, exclusively
	protected int x;
	protected int y;
	
	protected int width;
	protected int height;
	//
	
	protected int prevMouseX;
	protected int prevMouseY;
	
	protected boolean mouseDragged;
	
	/** Previously pressed. */
	private boolean pressed;
	private boolean hovered;
	private boolean focused;
	
	public GuiComponent(String id, int x, int y, int width, int height) {
		
		this.id = id;
		
		this.width = width;
		this.height = height;
		
		setX(x);
		setY(y);
		
		this.hitbox = new Rectangle();
		updateHitbox();
		
		pressed = false;
		hovered = false;
		focused = false;
		
	}
	
	/**
	 * Update position and/or highlighted-status and/or typing cursor etc.
	 * 
	 */
	public void update(MouseEvent e) {
		
		complexUpdate(e);
		
//		simpleUpdate(e);
		
	}
	
	@SuppressWarnings("unused") @Deprecated
	private void simpleUpdate(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		boolean mouseButtonDown = e.isButtonDown();
		
		if(intersects(x, y) && mouseButtonDown) {
			
			mouseClicked(e);
			
			e.consume();
		}
		
	}
	
	private void complexUpdate(MouseEvent e) {
		
		int x = e.getX();
		int y = e.getY();
		
		boolean mouseButtonDown = e.isButtonDown();
		
		if(!intersects(x, y) && mouseButtonDown && prevMouseX != x && prevMouseY != y) {// Mouse dragged outside component
			mouseDragged = true;
		}
		
		if(intersects(x, y) && !e.isConsumed()) {
			
			if(mouseButtonDown && hovered) {
				
				if(!pressed && !mouseDragged) {// If not already pressed
					mousePressed(e);
					
				}
				
				pressed = true;
				
			} else {
				
				if(pressed) {
					mouseReleased(e);
					
					if(intersects(prevMouseX, prevMouseY) && !mouseDragged) {
						mouseClicked(e);
						
					}
					
					mouseDragged = false;
					pressed = false;
					
				}
				
				
			}
			
			if(!hovered) {
				mouseEntered();
				
				hovered = true;
				
			}
			
			e.consume();// Yes.
			
		} else {
			
			if(hovered) {
				mouseExited();
				
				hovered = false;
			}
			
			if(!mouseButtonDown) {
				mouseDragged = false;
			}
			
			pressed = false;
			
		}
		
		prevMouseX = x;
		prevMouseY = y;
		
	}
	
	public void render(Graphics2D g) {
		
		if(GameManager.getGame().isDebug()) {
			drawHitbox(g);
		}
		
	}
	
	protected void drawHitbox(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(hitbox.x - 1, hitbox.y - 1, hitbox.width + 1, hitbox.height + 1);
	}
	
	protected void mouseClicked(MouseEvent e) {}// MouseEvent mainly to get mouse position, for now.
	
	protected void mousePressed(MouseEvent e) {
		focused = true;
	}
	
	protected void mouseReleased(MouseEvent e) {}
	
	protected void mouseEntered() {}
	
	protected void mouseExited() {}
	
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
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void updateHitbox() {
		hitbox.setBounds(x, y, width, height);
	}
	
	public boolean isFocused() {
		return focused;
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
	
	protected static int loadTexture(String textureFolder, String textureFileName) {
		
		String filePath = BASE_FOLDER_PATH + textureFolder + textureFileName + TEXTURE_FILE_EXTENSION;
		
		return ResourceManager.loadTexture(filePath);
	}
	
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
	
	protected static BufferedImage[] loadTextures(String textureFolder) {
		return loadTextures(textureFolder, new String[]{ResourceManager.getString("Gui.background"),
				ResourceManager.getString("Gui.foreground"), ResourceManager.getString("Gui.onclick")});
	}
	
}
