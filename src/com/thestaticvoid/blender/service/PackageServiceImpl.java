package com.thestaticvoid.blender.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thestaticvoid.blender.domain.Package;
import com.thestaticvoid.blender.domain.PackageDao;
import com.thestaticvoid.blender.spec.Spec;

@Service
public class PackageServiceImpl implements PackageService {
	@Autowired
	PackageDao packageDao;
	
	public boolean packageExists(String packageName) {
		return packageDao.getByPackageName(packageName) != null;
	}

	public void createNewPackage(Spec spec) {
		Package pkg = new Package();
		pkg.setName(spec.getPackageName());
		pkg.setMaintainer(Utils.getCachedUser());
		packageDao.store(pkg);
	}
}
