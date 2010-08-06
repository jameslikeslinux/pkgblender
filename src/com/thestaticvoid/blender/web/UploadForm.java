package com.thestaticvoid.blender.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.thestaticvoid.blender.spec.FileType;

public class UploadForm {
	private MultipartFile[] files;
	private List<String> fileNames;
	private List<FileType> fileTypes;
	private String packageName;
	
	public UploadForm() {
		clear();
	}

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
	
	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}

	public List<FileType> getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(List<FileType> fileTypes) {
		this.fileTypes = fileTypes;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void addFile(String fileName, FileType fileType) {
		fileNames.add(fileName);
		fileTypes.add(fileType);
		files = new MultipartFile[files.length + 1];
	}
	
	public void removeFile(int index) {
		fileNames.remove(index);
		fileTypes.remove(index);
		files = new MultipartFile[files.length - 1];
	}
	
	public void clear() {
		files = new MultipartFile[0];
		fileNames = new ArrayList<String>();
		fileTypes = new ArrayList<FileType>();
		packageName = "";
	}
}
