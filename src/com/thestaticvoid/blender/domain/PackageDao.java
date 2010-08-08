package com.thestaticvoid.blender.domain;

public interface PackageDao {
	public Package getByPackageName(String packageName);
	public void store(Package pkg);
}
