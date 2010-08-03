package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PackageServiceImpl implements PackageService {
	public void createNewPackage(String packageName, File file) throws IOException, SpecFileException, AdditionalFileRequiredException, PackageExistsException {
		SpecFile specFile = new SpecFile(packageName, file);
	}
}