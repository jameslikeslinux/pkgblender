package com.thestaticvoid.blender.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thestaticvoid.blender.domain.Branch;
import com.thestaticvoid.blender.domain.GenericDao;
import com.thestaticvoid.blender.domain.Os;
import com.thestaticvoid.blender.domain.Package;
import com.thestaticvoid.blender.domain.PackageDao;
import com.thestaticvoid.blender.domain.Role;
import com.thestaticvoid.blender.domain.User;
import com.thestaticvoid.blender.spec.Spec;

@Service
public class PackageService {
	@Autowired
	private GenericDao genericDao;
	
	@Autowired
	private PackageDao packageDao;
	
	@Transactional(readOnly = true)
	public Package getPackage(String packageName) {
		return genericDao.getByColumn(Package.class, "name", packageName);
	}
	
	@Transactional(readOnly = true)
	public boolean packageExists(String packageName) {
		return getPackage(packageName) != null;
	}
	
	@Transactional(readOnly = true)
	public Branch getBranch(String packageName, String branchName) {
		return packageDao.getBranch(packageName, branchName);
	}

	@Transactional
	public void createNewPackage(Spec spec) {
		Package pkg = new Package();
		pkg.setName(spec.getPackageName());
		pkg.setMaintainer(Utils.getCachedUser());
		
		Branch branch = new Branch();
		branch.setName("trunk");
		branch.setDependencies(spec.getDependencies());
		branch.setPkg(pkg);
		pkg.addBranch(branch);
		
		genericDao.store(pkg);
	}
	
	@Transactional(readOnly = true)
	public Set<PackageDependency> getDependencies(Branch branch) {
		Set<PackageDependency> dependencies = new TreeSet<PackageDependency>();
		
		for (String dependency : branch.getDependencies()) {
			List<Os> branchOses = new ArrayList<Os>(branch.getOses());
			List<Os> depInOses = packageDao.getOsesContainingLegacy(dependency);
			
			for (int i = 0; i < 2; i++) {
				Logger.getLogger(getClass()).info("BRANCH: " + branchOses.get(i).getId());
				Logger.getLogger(getClass()).info("OS    : " + depInOses.get(i).getId());
			}
			
			branchOses.removeAll(depInOses);
			
			for (int i = 0; i < 2; i++) {
				Logger.getLogger(getClass()).info("BRANCH: " + branchOses.get(i).getId());
				Logger.getLogger(getClass()).info("OS    : " + depInOses.get(i).getId());
			}
			
			if (!branch.getOses().isEmpty() && branchOses.isEmpty())
				continue;
			
			Package pkg = packageDao.getPackageContainingLegacy(dependency);
			PackageDependency pkgDep = new PackageDependency();
			pkgDep.setName(dependency);
			
			if (pkg == null)
				pkgDep.setStatus(PackageDependency.Status.MISSING);
			else {
				pkgDep.setStatus(PackageDependency.Status.OK);
				pkgDep.setName(pkg.getName());
			}
			
			dependencies.add(pkgDep);
		}
		
		return dependencies;
	}
	
	public static boolean canModifyPackage(Package pkg) {
		User user = Utils.getCachedUser();
		return user != null && (user.getAuthorities().contains(Role.ROLE_ADMIN) || pkg.getMaintainer().getUsername().equals(user.getUsername()));
	}
	
	@Transactional
	public void setBranchOses(String packageName, String branchName, List<String> osSlugs) {
		Branch branch = getBranch(packageName, branchName);
		
		if (branch == null)
			return;
		
		Set<Os> oses = new HashSet<Os>();
		
		if (osSlugs != null)
			for (String osSlug : osSlugs) {
				Os os = genericDao.getByColumn(Os.class, "slug", osSlug);
				Logger.getLogger(getClass()).info("Adding " + os.getName());
				if (os != null)
					oses.add(os);
			}
		
		branch.setOses(oses);
	}
}
