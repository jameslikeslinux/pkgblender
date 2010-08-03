package spec;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.thestaticvoid.blender.service.Utils;

public class Spec {
	private String packageName;
	private Map<File, FileType> files;
	
	private Spec(String packageName) {
		this.packageName = packageName;
		files = new HashMap<File, FileType>();
	}
	
	private String getSpecDir() {
		return Utils.SPECS_DIR + "/" + packageName;
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
	
	public void check() {
		
	}
}