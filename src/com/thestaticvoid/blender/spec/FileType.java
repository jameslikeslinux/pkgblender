package com.thestaticvoid.blender.spec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FileType {
	SPEC("/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.equals(packageName + ".spec") && !fileName.endsWith("-base.spec");
		}

		public String getMatchHelp(String packageName) {
			if (packageName.endsWith("-base"))
				return packageName.substring(0, packageName.length() - "-base".length()) + ".spec";
			
			return packageName + ".spec";
		}
	}),
	
	INCLUDE("/include/", new FileNameMatcher() {
		private final String[] IGNORE_INCLUDES = new String[]{
			"arch64.inc",
			"base.inc",
			"default-depend.inc",
			"options.inc",
			"prod.inc",
			"Solaris.inc"
		};
		
		public boolean matches(String packageName, String fileName) {
			boolean ignore = false;
			for (String ignoreInclude : IGNORE_INCLUDES) {
				if (fileName.equals(ignoreInclude))
					ignore = true;
			}
			
			return !ignore && fileName.endsWith(".inc");
		}
		
		public String getMatchHelp(String packageName) {
			return "*.inc";
		}
	}),
	
	BASESPEC("/base-specs/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.equals(packageName + "-base.spec");
		}
		
		public String getMatchHelp(String packageName) {
			return packageName + "-base.spec";
		}
	}),
	
	EXTSOURCE("/ext-sources/"),
	
	PATCH("/patches/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.matches(packageName + "-\\d{2}-.*\\.diff");
		}
		
		public String getMatchHelp(String packageName) {
			return packageName + "-##-<description>.diff";
		}
	}),
	
	COPYRIGHT("/copyright/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.equals(packageName + ".copyright");
		}
		
		public String getMatchHelp(String packageName) {
			return packageName + ".copyright";
		}
	});
	
	private String baseDir;
	private FileNameMatcher fileNameMatcher;
	
	private FileType(String baseDir) {
		this.baseDir = baseDir;
	}
	
	private FileType(String baseDir, FileNameMatcher fileNameMatcher) {
		this(baseDir);
		this.fileNameMatcher = fileNameMatcher;
	}
	
	public String getBaseDir() {
		return baseDir;
	}
	
	public void checkValidFileName(String packageName, String fileName) throws InvalidFileNameException {
		if (fileNameMatcher == null)
			return;
		
		if (!fileNameMatcher.matches(packageName, fileName))
			throw new InvalidFileNameException(this, fileName, fileNameMatcher.getMatchHelp(packageName));
	}
	
	public List<File> getFiles(String packageName, String dir) {
		List<File> files = new ArrayList<File>();
		
		File fileDir = new File(dir + baseDir);
		
		if (fileDir.exists())
			if (fileNameMatcher == null)
				Collections.addAll(files, fileDir.listFiles());
			else {
				fileNameMatcher.setPackageName(packageName);
				Collections.addAll(files, fileDir.listFiles(fileNameMatcher));
			}
		
		return files;
	}
}
