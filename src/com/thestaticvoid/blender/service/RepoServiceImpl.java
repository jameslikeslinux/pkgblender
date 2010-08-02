package com.thestaticvoid.blender.service;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.Repository;
import com.thestaticvoid.blender.domain.RepositoryDao;

@Service
public class RepoServiceImpl implements RepoService {
	@Autowired
	private RepositoryDao repositoryDao;
	
	private static final Pattern FMRI_PATTERN = Pattern.compile("^(pkg:/)?(.*)@(.*):?(.*)?$");
	
	public File hashToFile(String hash) {
		return new File(Utils.FILE_DIR + "/" + hash.substring(0, 2) + "/" + hash.substring(2, 8) + "/" + hash);
	}
	
	@Transactional
	public int openTransaction(String repo, String fmri) {
		Repository repository = repositoryDao.getByName(repo);
		Matcher fmriMatcher = FMRI_PATTERN.matcher(fmri);
		String name = fmriMatcher.group(2);
		String version = fmriMatcher.group(3);
		
		return -1;
	}
}
