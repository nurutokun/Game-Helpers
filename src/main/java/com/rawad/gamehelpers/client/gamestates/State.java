package com.rawad.gamehelpers.client.gamestates;

import java.io.IOException;

import com.rawad.gamehelpers.client.renderengine.MasterRender;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.world.World;
import com.rawad.gamehelpers.resources.Loader;
import com.rawad.gamehelpers.utils.ClassMap;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public abstract class State {
	
	/** Each {@code State} can have its own set of {@code GameSystem} objects; order of adding them is maintained. */
	protected final ClassMap<GameSystem> gameSystems;
	
	protected StateManager sm;
	
	protected Game game;
	
	/** Represents the parent used passed to the {@code Scene} to be displayed. */
	protected StackPane root;
	
	/** Provides a default, paintable background for states to use. */
	protected Canvas canvas;
	/** Holds all nodes. */
	protected StackPane guiContainer;
	
	protected FXMLLoader fxmlLoader;
	
	protected MasterRender masterRender;
	
	protected World world;
	
	/** Duration of the transition from this state to another. */
	protected Duration transitionDuration;
	
	public State(StateManager sm) {
		
		sm.addState(this);
		
		this.sm = sm;
		
		this.game = sm.getGame();
		
		gameSystems = new ClassMap<GameSystem>(true);
		
		masterRender = new MasterRender();
		
		world = new World();
		
		transitionDuration = Duration.millis(500);
		
	}
	
	public void initGui() {
		
		root = new StackPane();
		
		canvas = new Canvas();
		guiContainer = new StackPane();
		
		canvas.widthProperty().bind(root.widthProperty());
		canvas.heightProperty().bind(root.heightProperty());
		
		guiContainer.getStylesheets().add(Loader.getStyleSheetLocation(getClass(), getStyleSheet()));
		
		fxmlLoader = new FXMLLoader(Loader.getFxmlLocation(getClass()));
		fxmlLoader.setController(this);
		fxmlLoader.setRoot(guiContainer);
		
		try {
			fxmlLoader.load();
		} catch(IOException ex) {
			ex.printStackTrace();
			
			root.getChildren().add(new Label("Error initializing this state " + getClass().toString() + "; "
					+ ex.getMessage()));
		}
		
		root.getChildren().add(canvas);
		root.getChildren().add(guiContainer);
		
	}
	
	protected void onActivate() {}
	
	protected void onDeactivate() {}
	
	public void render() {
		masterRender.render(canvas.getGraphicsContext2D());
	}
	
	public Transition getTransition() {
		TranslateTransition slide = getSlideTransition();
		FadeTransition fadeOut = getFadeOutTransition();
		
		ParallelTransition transition = new ParallelTransition(guiContainer, slide, fadeOut);
		
		slide.setOnFinished(e -> transition.getNode().setTranslateX(0));
		fadeOut.setOnFinished(e -> transition.getNode().setOpacity(1.0d));
		
		return transition;
	}
	
	public TranslateTransition getSlideTransition() {
		TranslateTransition slide = new TranslateTransition(transitionDuration);
		slide.setFromX(0);
		slide.setToX(guiContainer.getWidth());
		return slide;
	}
	
	public FadeTransition getFadeOutTransition() {
		FadeTransition fade = new FadeTransition(transitionDuration);
		fade.setFromValue(1.0d);
		fade.setToValue(0.1d);
		return fade;
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
