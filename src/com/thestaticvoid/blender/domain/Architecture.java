package com.thestaticvoid.blender.domain;

public enum Architecture {
	X86("x86", "i386"), SPARC("SPARC", "sparc");
	
	private String displayName;
	private String catalogName;
	
	private Architecture(String displayName, String catalogName) {
		this.displayName = displayName;
		this.catalogName = catalogName;
	}
	
	public String toString() {
		return displayName;
	}
	
	public String getCatalogName() {
		return catalogName;
	}
}
