package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;

public interface PackageService {
	public void createNewPackage(String packageName, File specFile);
}