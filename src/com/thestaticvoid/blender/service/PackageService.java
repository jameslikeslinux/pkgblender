package com.thestaticvoid.blender.service;

import com.thestaticvoid.blender.spec.Spec;

public interface PackageService {
	public boolean packageExists(String packageName);
	public void createNewPackage(Spec spec);
}
