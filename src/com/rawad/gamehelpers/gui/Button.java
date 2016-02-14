package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Painter;
import javax.swing.UIDefaults;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;

public class Button extends JButton implements Painter<Button> {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = -555939131830923181L;
	
	private static final int BACKGROUND_LOCATION;
	private static final int FOREGROUND_LOCATION;
	private static final int ONCLICK_LOCATION;
	private static final int DISABLED_LOCATION;
	
	private final String id;

	private int backgroundTexture;
	private int foregroundTexture;
	private int onclickTexture;
	private int disabledTexture;
	
	public Button(String text, String id) {
		super(text);

		this.id = id;
		
		this.registerKeyboardAction(getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false)), 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), JComponent.WHEN_FOCUSED);
		this.registerKeyboardAction(getActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true)), 
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), JComponent.WHEN_FOCUSED);
		
		UIDefaults buttonDefaults = new UIDefaults();
		
		buttonDefaults.put("Button.backgroundPainter", this);
		
		putClientProperty("Nimbus.Overrides", buttonDefaults);
		putClientProperty("Nimbus.Overrides.InheritDefaults", false);
		
		setBorderPainted(false);
		
		setBackgroundTexture(BACKGROUND_LOCATION);
		setForegroundTexture(FOREGROUND_LOCATION);
		setOnclickTexture(ONCLICK_LOCATION);
		setDisabledTexture(DISABLED_LOCATION);
		
	}
	
	public Button(String text) {
		this(text, text);
	}

	static {
		
		String buttonBase = ResourceManager.getString("Button.base");
		
		GameHelpersLoader loader = GameManager.instance().getCurrentGame().getLoader(GameHelpersLoader.class);
		
		BACKGROUND_LOCATION = loader.loadGuiTexture(buttonBase, ResourceManager.getString("Gui.background"));
		FOREGROUND_LOCATION = loader.loadGuiTexture(buttonBase, ResourceManager.getString("Gui.foreground"));
		ONCLICK_LOCATION = loader.loadGuiTexture(buttonBase, ResourceManager.getString("Gui.onclick"));
		DISABLED_LOCATION = loader.loadGuiTexture(buttonBase, ResourceManager.getString("Gui.disabled"));
		
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * @param backgroundTexture the backgroundTexture to set
	 */
	public void setBackgroundTexture(int backgroundTexture) {
		this.backgroundTexture = backgroundTexture;
	}
	
	/**
	 * @param foregroundTexture the foregroundTexture to set
	 */
	public void setForegroundTexture(int foregroundTexture) {
		this.foregroundTexture = foregroundTexture;
	}
	
	/**
	 * @param onclickTexture the onclickTexture to set
	 */
	public void setOnclickTexture(int onclickTexture) {
		this.onclickTexture = onclickTexture;
	}
	
	/**
	 * @param disabledTexture the disabledTexture to set
	 */
	public void setDisabledTexture(int disabledTexture) {
		this.disabledTexture = disabledTexture;
	}
	
	@Override
	public void paint(Graphics2D g, Button button, int width, int height) {
		
		ButtonModel model = button.getModel();
		
		BufferedImage image = ResourceManager.getTexture(backgroundTexture);// Default if not enabled.
		
		if(model.isEnabled()) {
			
			if(model.isRollover() || model.isSelected() || button.isFocusOwner()) {
				image = ResourceManager.getTexture(foregroundTexture);
			}
			
			if(model.isArmed()) {
				image = ResourceManager.getTexture(onclickTexture);
			}
			
		} else {

			image = ResourceManager.getTexture(disabledTexture);
			
		}
		
		g.drawImage(image, 0, 0, width, height, null);// x,y relative to button bounds already.
		
		if(model.isSelected()) {
			
			Insets inset = button.getInsets();
			
			g.setColor(Color.BLACK);
			g.drawRect(inset.left, inset.top, width - inset.right, height - inset.bottom);
			
		}
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		
		BufferedImage image = ResourceManager.getTexture(backgroundTexture);
		
		return new Dimension(image.getWidth(), image.getHeight());
	}
	
}
