package com.thestaticvoid.blender.domain;

public interface RepositoryDao {
	public Repository getByName(String name);
	public void store(Repository repository);
}