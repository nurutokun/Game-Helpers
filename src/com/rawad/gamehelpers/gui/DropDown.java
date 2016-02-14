package com.rawad.gamehelpers.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonModel;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIDefaults;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxUI;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.resources.GameHelpersLoader;
import com.rawad.gamehelpers.resources.ResourceManager;

public class DropDown extends JComboBox<String> {
	
	/**
	 * Generated serial version UID.
	 */
	private static final long serialVersionUID = 8420334095211221221L;
	
	private static final int EDITOR_BACKGROUND_LOCATION;
	private static final int EDITOR_FOREGROUND_LOCATION;
	private static final int EDITOR_ONCLICK_LOCATION;
	private static final int EDITOR_DISABLED_LOCATION;
	
	private static final int BUTTON_BACKGROUND_LOCATION;
	private static final int BUTTON_FOREGROUND_LOCATION;
	private static final int BUTTON_ONCLICK_LOCATION;
	private static final int BUTTON_DISABLED_LOCATION;
	
	private final String id;
	
	private ComboBoxRenderer comboBoxRenderer;
	
	private int backgroundTexture;
	private int foregroundTexture;
	private int onclickTexture;
	private int disabledTexture;
	
	private int buttonBackgroundTexture;
	private int buttonForegroundTexture;
	private int buttonOnclickTexture;
	private int buttonDisabledTexture;
	
	public DropDown(String id, String defaultOption, String... options) {
		super(options);
		
		this.id = id;
		
		setSelectedItem(defaultOption);
		setEditable(true);
		setOpaque(false);
		setFocusable(false);// TODO: Should try to fix how the focusing works on dropdowns.
		
		setBackgroundTexture(EDITOR_BACKGROUND_LOCATION);
		setForegroundTexture(EDITOR_FOREGROUND_LOCATION);
		setOnclickTexture(EDITOR_ONCLICK_LOCATION);
		setDisabledTexture(EDITOR_DISABLED_LOCATION);
		
		setButtonBackgroundTexture(BUTTON_BACKGROUND_LOCATION);
		setButtonForegroundTexture(BUTTON_FOREGROUND_LOCATION);
		setButtonOnclickTexture(BUTTON_ONCLICK_LOCATION);
		setButtonDisabledTexture(BUTTON_DISABLED_LOCATION);
		
		comboBoxRenderer = new ComboBoxRenderer(this);
		
		setRenderer(comboBoxRenderer);
		setEditor(comboBoxRenderer);
		
		setUI(comboBoxRenderer);
		
		UIDefaults comboBoxDefaults = new UIDefaults();
		
		comboBoxDefaults.put("ComboBox.backgroundPainter", comboBoxRenderer);
		comboBoxDefaults.put("ComboBox.foregroundPainter", comboBoxRenderer);
		
		putClientProperty("Nimbus.Overrides", comboBoxDefaults);
		putClientProperty("Nimbus.Overrides.InheritDefaults", false);
		
//		http://www.codejava.net/java-se/swing/create-custom-gui-for-jcombobox
		
	}
	
	static {
		
		String dropdownBase = ResourceManager.getString("DropDown.base");
		
		String editorBase = ResourceManager.getString("DropDown.editor");
		String buttonBase = ResourceManager.getString("DropDown.button");
		
		String background = ResourceManager.getString("Gui.background");
		String foreground = ResourceManager.getString("Gui.foreground");
		String onclick = ResourceManager.getString("Gui.onclick");
		String disabled = ResourceManager.getString("Gui.disabled");
		
		GameHelpersLoader loader = GameManager.instance().getCurrentGame().getLoader(GameHelpersLoader.class);
		
		EDITOR_BACKGROUND_LOCATION = loader.loadGuiTexture(dropdownBase, editorBase, background);
		EDITOR_FOREGROUND_LOCATION = loader.loadGuiTexture(dropdownBase, editorBase, foreground);
		EDITOR_ONCLICK_LOCATION = loader.loadGuiTexture(dropdownBase, editorBase, onclick);
		EDITOR_DISABLED_LOCATION = loader.loadGuiTexture(dropdownBase, editorBase, disabled);
		
		BUTTON_BACKGROUND_LOCATION = loader.loadGuiTexture(dropdownBase, buttonBase, background);
		BUTTON_FOREGROUND_LOCATION = loader.loadGuiTexture(dropdownBase, buttonBase, foreground);
		BUTTON_ONCLICK_LOCATION = loader.loadGuiTexture(dropdownBase, buttonBase, onclick);
		BUTTON_DISABLED_LOCATION = loader.loadGuiTexture(dropdownBase, buttonBase, disabled);
		
		
	}
	
	@Override
	public Dimension getPreferredSize() {
		
		BufferedImage texture = ResourceManager.getTexture(backgroundTexture);
		
		return new Dimension(texture.getWidth(), texture.getHeight());
	}
	
	/**
	 * Takes care of all the rendering regarding the combobox except for the arrow button.
	 * 
	 * @author Rawad
	 *
	 */
	private class ComboBoxRenderer extends BasicComboBoxUI implements ComboBoxEditor, ListCellRenderer<String> {
		
		private final String prefix;

		private DropDown parent;
		
		private Button button;
		
		private TextLabel ed;
		private JLabel label;
		
		private String selectedItem;
		
		public ComboBoxRenderer(DropDown parent) {
			
			this.parent = parent;
			this.prefix = parent.getId();
			
			ed = new TextLabel(prefix);
			ed.setBackgroundTexture(backgroundTexture);
			
			label = new JLabel();
			label.setOpaque(true);
			label.setFocusable(false);
			
		}
		
		@Override
		protected JButton createArrowButton() {
			
			button = new Button("", "Arrow Button For: \"" + parent.getId() + "\"");
			
			button.setFocusable(false);
			
			button.setBackgroundTexture(parent.getButtonBackgroundTexture());
			button.setForegroundTexture(parent.getButtonForegroundTexture());
			button.setOnclickTexture(parent.getButtonOnclickTexture());
			button.setDisabledTexture(parent.getButtonDisabledTexture());
			
			button.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					
					ed.setBackgroundTexture(backgroundTexture);
					
					ButtonModel model = button.getModel();
					
					if(model.isEnabled()) {
						
						if(model.isRollover() || model.isSelected()) {
							ed.setBackgroundTexture(foregroundTexture);
						}
						
						if(model.isArmed()) {
							ed.setBackgroundTexture(onclickTexture);
						}
						
					} else {
						
						ed.setBackgroundTexture(disabledTexture);
						
					}
					
				}
				
			});
			
			button.addFocusListener(new FocusListener() {
				
				@Override
				public void focusGained(FocusEvent e) {
					button.getModel().setRollover(true);
				}
				
				@Override
				public void focusLost(FocusEvent e) {
					button.getModel().setRollover(false);
				}
				
			});
			
			return button;
			
		}
		
		@Override
		public Component getEditorComponent() {
			return ed;
		}
		
		@Override
		public Object getItem() {
			return selectedItem;
		}
		
		@Override
		public void setItem(Object item) {
			
			this.selectedItem = item.toString();
			ed.setText(""
					+ "<html>"
					+ "<div style=\"" + "text-align: center;" + "\">"
					+ prefix + ": " + "<br/>" + selectedItem
					+ "<div/>"
					+ "</html>"
					);
			
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {
			
			label.setText(value);
			
			if(isSelected) {
				label.setBackground(Color.GRAY);
				label.setForeground(Color.WHITE);
			} else {
				label.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
			}
			
			return label;
		}
		
		@Override
		public void selectAll() {}
		
		@Override
		public void addActionListener(ActionListener l) {}
		
		@Override
		public void removeActionListener(ActionListener l) {}
		
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
	
	/**
	 * @return the buttonBackgroundTexture
	 */
	public int getButtonBackgroundTexture() {
		return buttonBackgroundTexture;
	}
	
	/**
	 * @param buttonBackgroundTexture the buttonBackgroundTexture to set
	 */
	public void setButtonBackgroundTexture(int buttonBackgroundTexture) {
		this.buttonBackgroundTexture = buttonBackgroundTexture;
	}
	
	/**
	 * @return the buttonForegroundTexture
	 */
	public int getButtonForegroundTexture() {
		return buttonForegroundTexture;
	}
	
	/**
	 * @param buttonForegroundTexture the buttonForegroundTexture to set
	 */
	public void setButtonForegroundTexture(int buttonForegroundTexture) {
		this.buttonForegroundTexture = buttonForegroundTexture;
	}
	
	/**
	 * @return the buttonOnclickTexture
	 */
	public int getButtonOnclickTexture() {
		return buttonOnclickTexture;
	}
	
	/**
	 * @param buttonOnclickTexture the buttonOnclickTexture to set
	 */
	public void setButtonOnclickTexture(int buttonOnclickTexture) {
		this.buttonOnclickTexture = buttonOnclickTexture;
	}
	
	/**
	 * @return the buttonDisabledTexture
	 */
	public int getButtonDisabledTexture() {
		return buttonDisabledTexture;
	}
	
	/**
	 * @param buttonDisabledTexture the buttonDisabledTexture to set
	 */
	public void setButtonDisabledTexture(int buttonDisabledTexture) {
		this.buttonDisabledTexture = buttonDisabledTexture;
	}
	
}
