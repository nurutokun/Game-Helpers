package com.rawad.gamehelpers.client.input;

public abstract class AInput {
	
	protected abstract String getName();
	
	protected abstract boolean equals(AInput otherAInput);
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AInput) return this.equals((AInput) obj);
		return false;// 44 calls are obj == null.
	}
	
}
