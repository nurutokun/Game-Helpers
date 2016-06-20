package com.rawad.gamehelpers.client.gamestates;

import java.io.IOException;

import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public abstract class State {
	
	/** Each {@code State} can have its own set of {@code GameSystem} objects; order of adding them is maintained. */
	protected final ClassMap<GameSystem> gameSystems;
	
	protected StateManager sm;
	
	protected Game game;
	
	/** Represents the parent used passed to the {@code Scene} to be displayed. */
	protected StackPane root;
	
	/** Provides a default, paintable background for states to use. */
	protected Canvas canvas;
	
	protected FXMLLoader fxmlLoader;
	
	protected MasterRender masterRender;
	
	protected World world;
	
	public State(StateManager sm) {
		
		sm.addState(this);
		
		this.sm = sm;
		
		this.game = sm.getGame();
		
		gameSystems = new ClassMap<GameSystem>(true);
		
		masterRender = new MasterRender();
		
		world = new World();
		
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
		
	}
	
	protected void onActivate() {}
	
	protected void onDeactivate() {}
	
	public void render() {
		masterRender.render(canvas.getGraphicsContext2D());
	}
	
	/**
	 * For {@code State}-specific stylesheets.
	 * 
	 * @return
	 */
	public String getStyleSheet() {
		return "StyleSheet";
	}
	
	public StackPane getRoot() {
		return root;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public World getWorld() {
		return world;
	}
	
}
