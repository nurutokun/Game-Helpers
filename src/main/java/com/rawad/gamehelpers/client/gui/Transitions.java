package com.rawad.gamehelpers.client.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public final class Transitions {
	
	public static final Transition EMPTY = parallel(null, null);
	
	public static final Duration DEFAULT_DURATION = Duration.millis(500);
	
	/** Alpha value indicating an opaque object. */
	public static final double OPAQUE = 1.0d;
	/** Alpha value indicating an object that is mostly hidden. */
	public static final double HIDDEN = 0.1d;
	
	private Transitions() {}
	
	public static final TranslateTransition slideHorizontally(Duration duration, double from, double to) {
		TranslateTransition slide = new TranslateTransition(duration);
		slide.setFromX(from);
		slide.setToX(to);
		return slide;
	}
	
	public static final FadeTransition fade(Duration duration, double from, double to) {
		FadeTransition fade = new FadeTransition(duration);
		fade.setFromValue(from);
		fade.setToValue(to);
		return fade;
	}
	
	public static final ParallelTransition parallel(Node target, EventHandler<ActionEvent> onFinish, 
			Transition... transitions) {
		ParallelTransition transition = new ParallelTransition(target);
		transition.setOnFinished(onFinish);
		transition.getChildren().addAll(transitions);
		return transition;
	}
	
}
