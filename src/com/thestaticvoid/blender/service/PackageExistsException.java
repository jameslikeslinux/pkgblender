package com.thestaticvoid.blender.service;

public class PackageExistsException extends Exception {
	public PackageExistsException(String packageName) {
		super(packageName);
	}
}
