package com.thestaticvoid.blender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.GenericDao;
import com.thestaticvoid.blender.domain.Package;
import com.thestaticvoid.blender.spec.Spec;

@Service
public class PackageService {
	@Autowired
	private GenericDao genericDao;
	
	public boolean packageExists(String packageName) {
		return genericDao.getByColumn(Package.class, "name", packageName) != null;
	}

	@Transactional
	public void createNewPackage(Spec spec) {
		Package pkg = new Package();
		pkg.setName(spec.getPackageName());
		pkg.setMaintainer(Utils.getCachedUser());
		genericDao.store(pkg);
	}
}
