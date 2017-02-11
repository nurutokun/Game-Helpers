package com.rawad.gamehelpers.client.gamestates;

public final class StateChangeRequest {
	
	private final Class<? extends State> requestedState;
	
	private StateChangeRequest(Class<? extends State> requestedState) {
		super();
		
		this.requestedState = requestedState;
		
	}
	
	/**
	 * @return the requestedState
	 */
	public Class<? extends State> getRequestedState() {
		return requestedState;
	}
	
	public static final StateChangeRequest instance(Class<? extends State> requestedState) {
		return new StateChangeRequest(requestedState);
	}
	
}
