package com.thestaticvoid.blender.web;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.thestaticvoid.blender.domain.Branch;
import com.thestaticvoid.blender.domain.Os;
import com.thestaticvoid.blender.domain.Package;
import com.thestaticvoid.blender.service.AdministrationService;
import com.thestaticvoid.blender.service.PackageService;
import com.thestaticvoid.blender.spec.FileType;
import com.thestaticvoid.blender.spec.Spec;

@Controller
public class PackageController {
	@Autowired
	private PackageService packageService;
	
	@Autowired
	private AdministrationService administrationService;
	
	private void preparePackageInfoModel(Package pkg, Model model) {
		model.addAttribute("pkg", pkg);
		
		Set<File> files =  Spec.loadSpec(pkg.getName()).getFiles().keySet();
		model.addAttribute("files", files);
		
		// XXX assumes there is only one branch
		Branch branch = pkg.getBranches().iterator().next();
		model.addAttribute("branch", branch);
		
		model.addAttribute("dependencies", packageService.getDependencies(branch));
		
		boolean canModifyPackage = PackageService.canModifyPackage(pkg);
		model.addAttribute("canModifyPackage", canModifyPackage);
		
		if (canModifyPackage) {
			OsesForm osesForm = new OsesForm();
			osesForm.setAvailableOses(administrationService.getOkOses());
			for (Os os : branch.getOses())
				osesForm.addOs(os.getSlug());
			
			model.addAttribute("osesForm", osesForm);
		}
		
		model.addAttribute("commentForm", new CommentForm());
	}
	
	@RequestMapping(value = "/packages/{packageName}", method = RequestMethod.GET)
	public String packageInfo(@PathVariable String packageName, Model model) {
		Package pkg = packageService.getPackage(packageName);
		
		if (pkg == null) {
			model.addAttribute("packageName", packageName);
			return "packageNotFound";
		}
		
		preparePackageInfoModel(pkg, model);
		return "package";
	}
	
	@RequestMapping(value = "/packages/{packageName}/branches/{branchName}/changeOses", method = RequestMethod.POST)
	public String changeOses(@PathVariable String packageName, @PathVariable String branchName, OsesForm osesForm) {
		packageService.setBranchOses(packageName, branchName, osesForm.getOses());
		return "redirect:/packages/" + packageName;
	}
	
	@RequestMapping(value = "/packages/{packageName}/addComment", method = RequestMethod.POST)
	public String addComment(@PathVariable String packageName, @RequestParam String comment) {
		packageService.addComment(packageName, comment);
		return "redirect:/packages/" + packageName;
	}
}