package com.thestaticvoid.blender.spec;

import java.util.HashMap;
import java.util.Map;

public class AdditionalFilesRequiredException extends Exception {
	private Map<String, FileType> files;
	
	public AdditionalFilesRequiredException() {
		files = new HashMap<String, FileType>();
	}
	
	public AdditionalFilesRequiredException(String file, FileType fileType) {
		this();
		files.put(file, fileType);
	}
	
	public void addFile(String file, FileType fileType) {
		files.put(file, fileType);
	}
	
	public Map<String, FileType> getFiles() {
		return files;
	}
}
