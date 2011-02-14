package com.thestaticvoid.blender.spec;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
		return Utils.SPECS_DIR;
	}
	
	private String getSpecFile() {
		return getSpecDir() + "/" + packageName + ".spec";
	}
	
	public static Spec newSpec(String packageName) {
		return new Spec(packageName);
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
		
		File destDir = new File(getSpecDir() + fileType.getBaseDir());
		if (!destDir.exists()) {
			destDir.mkdir();
			
			try {
				if (Runtime.getRuntime().exec("/usr/bin/svn add " + destDir).waitFor() != 0)
					throw new Exception();
			} catch (Exception e) {
				destDir.delete();
				throw new IOException("Could not add " + destDir + " to the repository.");
			}
		}
		
		File destFile = new File(destDir + "/" + file.getName());
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
	
	public Map<File, FileType> getFiles() {
		return files;
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public Set<String> getDependencies() {
		Set<String> dependencies = new HashSet<String>();
		
		try {
			Process specTool = runSpecTool("get_buildrequires");
			BufferedReader reader = new BufferedReader(new InputStreamReader(specTool.getInputStream()));
			
			String line;
			while ((line = reader.readLine()) != null)
				dependencies.add(line);
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dependencies;
	}
	
	public void commit() {
		commit(packageName + ": Initial upload.");
	}
	
	public void commit(String message) {
		try {
			Runtime.getRuntime().exec(new String[]{"/usr/bin/svn", "commit", "-m", message, getSpecDir()}).waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void check() throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException {
		checkForErrors();
		checkPackageName();
		String copyrightFile = checkForCopyright();
		checkForFiles(copyrightFile);
	}
	
	private void checkForErrors() throws IOException, InvalidFileNameException, AdditionalFilesRequiredException, SpecFileException {
		Process specTool = runSpecTool("get_error");
		BufferedReader reader = new BufferedReader(new InputStreamReader(specTool.getInputStream()));
		String error = reader.readLine();
		
		int exitStatus;
		try {
			exitStatus = specTool.waitFor();
		} catch (InterruptedException e) {
			exitStatus = 1;
		}
		
		// check if there are errors
		if (exitStatus != 0) {
			// check if we need an include
			Matcher matcher = COULD_NOT_OPEN_INCLUDE.matcher(error);
			if (matcher.matches()) {
				String requiredInclude = new File(matcher.group(1)).getName();
				
				// check if the requested include is a valid file name
				FileType.INCLUDE.checkValidFileName(packageName, requiredInclude);
				
				// otherwise throw an exception containing the required file
				throw new AdditionalFilesRequiredException(requiredInclude, FileType.INCLUDE);
			}
			
			// check if we need some other file
			matcher = COULD_NOT_OPEN_FILE.matcher(error);
			if (matcher.matches()) {
				String requiredFile = new File(matcher.group(1)).getName();
				
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
	
	private void checkPackageName() throws IOException, SpecFileException {
		Process specTool = runSpecTool("get_package_names");
		BufferedReader reader = new BufferedReader(new InputStreamReader(specTool.getInputStream()));
		String result = reader.readLine();
		
		// XXX assumes the package name is the first package defined
		if (!result.equals(packageName))
			throw new SpecFileException("The spec file does not define the package " + packageName);
	}
	
	private String checkForCopyright() throws IOException, SpecFileException, InvalidFileNameException {
		String copyrightTag = "%sunw_copyright";
		Process specTool = runSpecTool("eval " + copyrightTag);
		BufferedReader reader = new BufferedReader(new InputStreamReader(specTool.getInputStream()));
		String result = reader.readLine();
		
		if (result.equals(copyrightTag))
			throw new SpecFileException("The spec file requires a 'SUNW_Copyright' tag.");
		
		FileType.COPYRIGHT.checkValidFileName(packageName, result);
		
		return result;
	}
	
	private void checkForFiles(String copyrightFile) throws IOException, InvalidFileNameException, AdditionalFilesRequiredException {
		AdditionalFilesRequiredException addlFiles = new AdditionalFilesRequiredException();
		
		if (!new File(getSpecDir() + FileType.COPYRIGHT.getBaseDir() + copyrightFile).exists())
			addlFiles.addFile(copyrightFile, FileType.COPYRIGHT);
		
		checkAddlFiles("sources", FileType.EXTSOURCE, addlFiles);
		checkAddlFiles("patches", FileType.PATCH, addlFiles);
		
		if (!addlFiles.getFiles().isEmpty())
			throw addlFiles;
	}
	
	private void checkAddlFiles(String type, FileType fileType, AdditionalFilesRequiredException addlFiles) throws IOException, InvalidFileNameException {
		Process specTool = runSpecTool("get_" + type);
		BufferedReader reader = new BufferedReader(new InputStreamReader(specTool.getInputStream()));
		
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("ftp://") || line.startsWith("http://"))
				continue;
			
			// check that the patch file name is in the right format
			fileType.checkValidFileName(packageName, line);
			
			if (!new File(getSpecDir() + fileType.getBaseDir() + line).exists())
				addlFiles.addFile(line, fileType);
		}
	}
	
	private Process runSpecTool(String subCommand) throws IOException {
		return Runtime.getRuntime().exec("/usr/bin/spectool --rcfile=" + getSpecDir() + "/.pkgtoolrc " + subCommand + " " + getSpecFile());
	}
}