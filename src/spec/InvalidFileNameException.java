package spec;

public class InvalidFileNameException extends Exception {
	private String invalidFileName, fileNameHelp;
	
	public InvalidFileNameException(String invalidFileName, String fileNameHelp) {
		super("Invalid file name for the specified type.");
		
		this.invalidFileName = invalidFileName;
		this.fileNameHelp = fileNameHelp;
	}

	public String getInvalidFileName() {
		return invalidFileName;
	}

	public String getFileNameHelp() {
		return fileNameHelp;
	}
}
