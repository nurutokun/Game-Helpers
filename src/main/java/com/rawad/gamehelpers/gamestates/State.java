package com.rawad.gamehelpers.gamestates;

import java.io.IOException;

import com.rawad.gamehelpers.client.IClientController;
import com.rawad.gamehelpers.resources.Loader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public abstract class State implements IClientController {
	
	protected StateManager sm;
	
//	protected Scene scene;
	
	/** Represents the parent used passed to the <code>Scene</code> to be displayed. */
	protected StackPane root;
	
	/** Provides a default, paintable background for states to use. */
	protected Canvas canvas;
	
	protected FXMLLoader fxmlLoader;
	
	public void initGui() {
		
		root = new StackPane();
		root.getStylesheets().add(Loader.getStyleSheetLocation(getClass(), "StyleSheet"));
		
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
	
	protected void setStateManager(StateManager sm) {
		this.sm = sm;
	}
	
	@Override
	public void tick() {}
	
	@Override
	public void render() {}
	
	protected IClientController getController() {
		return this;
	}
	
	public Parent getRoot() {
		return root;
	}
	
}
