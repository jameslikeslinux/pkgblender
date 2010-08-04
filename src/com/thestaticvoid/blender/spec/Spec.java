package com.thestaticvoid.blender.spec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thestaticvoid.blender.service.Utils;

public class Spec {
	private String packageName;
	private Map<File, FileType> files;
	
	private static final Pattern COULD_NOT_OPEN_INCLUDE = Pattern.compile(".*Could not open %include file (.*) for reading$");
	private static final Pattern COULD_NOT_OPEN_FILE = Pattern.compile(".*Could not open file (.*)$");
	
	private Spec(String packageName) {
		this.packageName = packageName;
		files = new HashMap<File, FileType>();
	}
	
	private String getSpecDir() {
		return Utils.SPECS_DIR + "/" + packageName;
	}
	
	private String getSpecFile() {
		return getSpecDir() + "/" + packageName + ".spec";
	}
	
	public static Spec newSpec(String packageName) throws IOException {
		Spec spec = new Spec(packageName);
		
		if (new File(spec.getSpecDir()).exists())
			try {
				if (Runtime.getRuntime().exec("/usr/bin/svn rm --force " + spec.getSpecDir()).waitFor() != 0)
					throw new Exception();
			} catch (Exception e) {
				throw new IOException("Could not remove " + spec.getSpecDir());
			}
		
		try {
			if (Runtime.getRuntime().exec("/usr/bin/svn cp " + Utils.SPECS_DIR + "/TEMPLATE " + spec.getSpecDir()).waitFor() != 0)
				throw new Exception();
		} catch (Exception e) {
			throw new IOException("Could not copy template spec dir.");
		}
		
		return spec;
	}
	
	public static Spec loadSpec(String packageName) {
		Spec spec = new Spec(packageName);
		
		for (FileType fileType : FileType.values())
			for (File file : fileType.getFiles(packageName, spec.getSpecDir()))
				spec.files.put(file, fileType);
		
		return spec;
	}
	
	public void addFile(File file, FileType fileType) throws InvalidFileNameException, IOException {
		fileType.checkValidFileName(packageName, file.getName());
		File destFile = new File(getSpecDir() + fileType.getBaseDir() + file.getName());
		file.renameTo(destFile);
		
		try {
			if (Runtime.getRuntime().exec("/usr/bin/svn add " + destFile).waitFor() != 0)
				throw new Exception();
		} catch (Exception e) {
			destFile.delete();
			throw new IOException("Could not add " + destFile.getName() + " to the repository.");
		}
		
		files.put(file, fileType);
	}
	
	public void check() throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException {
		checkForErrors();
	}
	
	private void checkForErrors() throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException {
		Process errorCheckProcess = Runtime.getRuntime().exec("/usr/bin/spectool --rcfile=" + getSpecDir() + "/.pkgtoolrc get_error " + getSpecFile());
		BufferedReader reader = new BufferedReader(new InputStreamReader(errorCheckProcess.getInputStream()));
		String error = reader.readLine();
		
		int exitStatus;
		try {
			exitStatus = errorCheckProcess.waitFor();
		} catch (InterruptedException e) {
			exitStatus = 1;
		}
		
		// check if there are errors
		if (exitStatus != 0) {
			// check if we need an include
			Matcher matcher = COULD_NOT_OPEN_INCLUDE.matcher(error);
			if (matcher.matches()) {
				String requiredInclude = matcher.group(1);
				
				// check if the requested include is a valid file name
				FileType.INCLUDE.checkValidFileName(packageName, requiredInclude);
				
				// otherwise throw an exception containing the required file
				throw new AdditionalFilesRequiredException(requiredInclude, FileType.INCLUDE);
			}
			
			// check if we need some other file
			matcher = COULD_NOT_OPEN_FILE.matcher(error);
			if (matcher.matches()) {
				String requiredFile = matcher.group(1);
				
				// check if the requested include is a valid file name
				// XXX this assumes the missing file is a base-spec...what else is there?
				FileType.BASESPEC.checkValidFileName(packageName, requiredFile);
				
				// otherwise throw an exception containing the required file
				throw new AdditionalFilesRequiredException(requiredFile, FileType.BASESPEC);
			}
			
			// otherwise, unknown error can be sent to the user
			throw new SpecFileException(error);
		}
	}
}