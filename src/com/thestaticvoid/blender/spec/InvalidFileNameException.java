package com.thestaticvoid.blender.spec;

public class InvalidFileNameException extends Exception {
	private FileType fileType;
	private String invalidFileName, fileNameHelp;
	
	public InvalidFileNameException(FileType fileType, String invalidFileName, String fileNameHelp) {
		super("Invalid file name '" + invalidFileName + "' for the type " + fileType);
		
		this.fileType = fileType;
		this.invalidFileName = invalidFileName;
		this.fileNameHelp = fileNameHelp;
	}
	
	public FileType getFileType() {
		return fileType;
	}

	public String getInvalidFileName() {
		return invalidFileName;
	}

	public String getFileNameHelp() {
		return fileNameHelp;
	}
}
