package com.thestaticvoid.blender.web;

import org.springframework.web.multipart.MultipartFile;

public class UploadForm {
	private MultipartFile[] files;

	public int getNumFiles() {
		return files.length;
	}
	
	public void setNumFiles(int numFiles) {
		files = new MultipartFile[numFiles];
	}
	
	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}
}
