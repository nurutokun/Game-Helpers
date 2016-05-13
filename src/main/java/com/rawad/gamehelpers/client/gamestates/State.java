package com.rawad.gamehelpers.client.gamestates;

import java.io.IOException;
import java.util.ArrayList;

import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.client.renderengine.Camera;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.resources.Loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public abstract class State implements IClientController {
	
	/** Each {@code State} can have its own set of {@code GameSystem} objects; order of adding them is maintained. */
	protected final ArrayList<GameSystem> gameSystems;
	
	protected StateManager sm;
	
	/** Represents the parent used passed to the {@code Scene} to be displayed. */
	protected StackPane root;
	
	/** Provides a default, paintable background for states to use. */
	protected Canvas canvas;
	
	protected FXMLLoader fxmlLoader;
	
	protected World world;
	
	protected Camera camera;
	
	public State() {
		
		gameSystems = new ArrayList<GameSystem>();
		
		world = new World();
		
		camera = new Camera();
		
	}
	
	public void initGui() {
		
		root = new StackPane();
		root.getStylesheets().add(Loader.getStyleSheetLocation(getClass(), getStyleSheet()));
		
		fxmlLoader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(root);
		
		try {
			fxmlLoader.load();
		} catch(IOException ex) {
			ex.printStackTrace();
			
			root.getChildren().add(new Label("Error initializing this state " + getClass().toString() + "; "
					+ ex.getMessage()));
		}
		
		canvas = new Canvas();
		root.getChildren().add(0, canvas);
		
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());
		
		camera.getCameraBounds().widthProperty().bind(root.widthProperty());
		camera.getCameraBounds().heightProperty().bind(root.heightProperty());
		
	}
	
	protected void onActivate() {}
	
	protected void onDeactivate() {}
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	@Override
	public void tick() {}
	
	/**
	 * For {@code State}-specific stylesheets.
	 * 
	 * @return
	 */
	public String getStyleSheet() {
		return "StyleSheet";
	}
	
	public Parent getRoot() {
		return root;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
}
