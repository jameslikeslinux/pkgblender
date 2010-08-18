package com.thestaticvoid.blender.domain;

import java.util.List;

public interface PackageDao {
	public List<Os> getOsesContainingLegacy(String packageName);
	public Package getPackageContainingLegacy(String packageName);
	public Branch getBranch(String packageName, String branchName);
}
