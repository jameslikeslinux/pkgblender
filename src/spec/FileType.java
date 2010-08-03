package spec;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum FileType {
	SPEC("/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.matches(packageName + "\\.spec");
		}

		public String getMatchHelp(String packageName) {
			return packageName + ".spec";
		}
	}),
	
	INCLUDE("/include/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.matches(".*\\.inc");
		}
		
		public String getMatchHelp(String packageName) {
			return "*.inc";
		}
	}),
	
	BASESPEC("/base-specs/", new FileNameMatcher() {
		public boolean matches(String packageName, String fileName) {
			return fileName.matches(packageName + "-base\\.spec");
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
			return fileName.matches(packageName + "\\.copyright");
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
			throw new InvalidFileNameException(fileName, fileNameMatcher.getMatchHelp(packageName));
	}
	
	public List<File> getFiles(String packageName, String dir) {
		List<File> files = new ArrayList<File>();
		
		if (fileNameMatcher == null)
			Collections.addAll(files, new File(dir + baseDir).listFiles());
		else {
			fileNameMatcher.setPackageName(packageName);
			Collections.addAll(files, new File(dir + baseDir).listFiles(fileNameMatcher));
		}
		
		return files;
	}
}
