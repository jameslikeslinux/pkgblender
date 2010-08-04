package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.thestaticvoid.blender.spec.AdditionalFilesRequiredException;
import com.thestaticvoid.blender.spec.FileType;
import com.thestaticvoid.blender.spec.InvalidFileNameException;
import com.thestaticvoid.blender.spec.Spec;
import com.thestaticvoid.blender.spec.SpecFileException;

@Service
public class PackageServiceImpl implements PackageService {
	public void createNewPackage(String packageName, File file) throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException {
		Spec spec = Spec.newSpec(packageName);
		spec.addFile(file, FileType.SPEC);
		spec.check();
	}

	public void addFilesToPackage(String packageName, Map<File, FileType> files) {
		
	}
}