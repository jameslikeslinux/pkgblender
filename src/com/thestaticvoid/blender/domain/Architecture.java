package com.thestaticvoid.blender.domain;

public enum Architecture {
	X86("x86"), SPARC("SPARC");
	
	private String displayName;
	
	private Architecture(String displayName) {
		this.displayName = displayName;
	}
	
	public String toString() {
		return displayName;
	}
}
