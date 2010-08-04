package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.thestaticvoid.blender.spec.AdditionalFilesRequiredException;
import com.thestaticvoid.blender.spec.FileType;
import com.thestaticvoid.blender.spec.InvalidFileNameException;
import com.thestaticvoid.blender.spec.SpecFileException;

public interface PackageService {
	public void createNewPackage(String packageName, File specFile) throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException;
	public void addFilesToPackage(String packageName, Map<File, FileType> files);
}