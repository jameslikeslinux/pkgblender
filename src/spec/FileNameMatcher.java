package spec;

import java.io.File;
import java.io.FilenameFilter;

public abstract class FileNameMatcher implements FilenameFilter {
	public abstract boolean matches(String packageName, String fileName);
	public abstract String getMatchHelp(String packageName);
	
	private String packageName;
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public boolean accept(File dir, String name) {
		return matches(packageName, name);
	}
}
