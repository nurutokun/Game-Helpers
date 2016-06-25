package com.rawad.gamehelpers.client.gamestates;

import com.rawad.gamehelpers.client.gui.Transitions;
import com.rawad.gamehelpers.utils.Util;

import javafx.animation.Transition;

public final class StateChangeRequest {
	
	private final Class<? extends State> requestedState;
	
	private final Transition onRequestedStateActivate;
	private final Transition onOldStateDeactivate;
	
	private StateChangeRequest(Class<? extends State> requestedState, Transition onRequestedStateActivate,
			Transition onOldStateDeactivate) {
		super();
		
		this.requestedState = requestedState;
		
		this.onRequestedStateActivate = onRequestedStateActivate;
		this.onOldStateDeactivate = onOldStateDeactivate;
		
	}
	
	/**
	 * @return the requestedState
	 */
	public Class<? extends State> getRequestedState() {
		return requestedState;
	}
	
	/**
	 * @return the onRequestedStateActivate
	 */
	public Transition getOnRequestedStateActivate() {
		return onRequestedStateActivate;
	}
	
	/**
	 * @return the onOldStateDeactivate
	 */
	public Transition getOnOldStateDeactivate() {
		return onOldStateDeactivate;
	}
	
	public static final StateChangeRequest instance(Class<? extends State> requestedState, Transition 
			onRequestedStateActivate, Transition onOldStateDeactivate) {
		return new StateChangeRequest(requestedState, Util.getNullSafe(onRequestedStateActivate, Transitions.EMPTY), 
				Util.getNullSafe(onOldStateDeactivate, Transitions.EMPTY));
	}
	
	public static final StateChangeRequest instance(Class<? extends State> requestedState) {
		return instance(requestedState, Transitions.EMPTY, Transitions.EMPTY);
	}
	
}
