package com.thestaticvoid.blender.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpecFile {
	private File specFile;
	
	public SpecFile(File specFile) {
		this.specFile = specFile;
	}

	public String getPackageName() {
		try {
			Process process = Runtime.getRuntime().exec("/usr/bin/spectool get_package_names " + specFile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String packageName = reader.readLine();
			reader.close();
			
			return packageName;
		} catch (IOException io) {
			return null;
		}
	}
}