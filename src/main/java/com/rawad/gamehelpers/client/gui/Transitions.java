package com.rawad.gamehelpers.client.gui;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public final class Transitions {
	
	public static final ParallelTransition EMPTY = parallel(null, null);
	
	public static final Duration DEFAULT_DURATION = Duration.millis(500);
	
	public static final double SLIDE_RIGHT = 1;
	public static final double SLIDE_LEFT = -1;
	
	/** Alpha value indicating an opaque object. */
	public static final double SHOWING = 1.0d;
	/** Alpha value indicating an object that is mostly hidden. */
	public static final double HIDDEN = 0.1d;
	
	private Transitions() {}
	
	public static final Transition slideHorizontally(Duration duration, double from, double direction) {
		Transition slide = new Transition() {
			
			{
				setCycleDuration(duration);
			}
			
			@Override
			protected void interpolate(double frac) {
				
				try {
					
					Region region = (Region) getParentTargetNode();
					region.setTranslateX(direction * region.getWidth() * frac);
					
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}
		};
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
		ParallelTransition transition = new ParallelTransition(target, transitions);
		transition.setOnFinished(onFinish);
		return transition;
	}
	
	public static final SequentialTransition sequence(Node target, EventHandler<ActionEvent> onFinish, 
			Transition... transitions) {
		SequentialTransition transition = new SequentialTransition(target, transitions);
		transition.setOnFinished(onFinish);
		return transition;
	}
	
}
