package com.thestaticvoid.blender.service;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl implements PackageService {
	public void createNewPackage(File file) {
		SpecFile specFile = new SpecFile(file);
		Logger.getLogger(getClass()).info("NAME: " + specFile.getPackageName());
	}
}