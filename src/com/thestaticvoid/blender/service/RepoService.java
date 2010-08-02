package com.thestaticvoid.blender.service;

import java.io.File;

public interface RepoService {
	public File hashToFile(String hash);
	public int openTransaction(String repo, String fmri);
}
