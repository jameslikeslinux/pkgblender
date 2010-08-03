package com.thestaticvoid.blender.service;

public class AdditionalFileRequiredException extends Exception {
	private String packageName, requiredFile;
	
	public AdditionalFileRequiredException(String packageName, String requiredFile) {
		this.packageName = packageName;
		this.requiredFile = requiredFile;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getRequiredFile() {
		return requiredFile;
	}
}
