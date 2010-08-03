package com.thestaticvoid.blender.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class SpecFile {
	private File specFile;
	
	private static final Pattern couldNotOpenPattern = Pattern.compile(".*Could not open file (.*)$");
	
	public SpecFile(String packageName, File tmpSpecFile) throws IOException, SpecFileException, AdditionalFileRequiredException, PackageExistsException {
		String specDir = Utils.SPECS_DIR + "/" + packageName;
		
		if (new File(specDir).exists())
			throw new PackageExistsException(packageName);
			
		try {
			Runtime.getRuntime().exec("/usr/bin/svn cp " + Utils.SPECS_DIR + "/TEMPLATE " + specDir).waitFor();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File specFile = new File(specDir + "/" + packageName + ".spec");
		tmpSpecFile.renameTo(specFile);
		
		Process errorCheckProcess = Runtime.getRuntime().exec("/usr/bin/spectool --rcfile=" + specDir + "/.pkgtoolrc get_error " + specFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(errorCheckProcess.getInputStream()));
		
		String error = reader.readLine();
		int exitStatus;
		try {
			exitStatus = errorCheckProcess.waitFor();
		} catch (InterruptedException e) {
			exitStatus = 1;
		}
		
		if (exitStatus != 0) {
			Matcher couldNotOpenMatcher = couldNotOpenPattern.matcher(error);
			if (couldNotOpenMatcher.matches()) {
				File requiredFile = new File(couldNotOpenMatcher.group(1));
				throw new AdditionalFileRequiredException(specFile.getName(), requiredFile.getName());
			}
			
			// cleanup
			Runtime.getRuntime().exec("/usr/bin/svn rm --force " + specDir);
			throw new SpecFileException(error);
		}
		
		this.specFile = tmpSpecFile;
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