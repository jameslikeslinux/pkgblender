package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.thestaticvoid.blender.service.AdditionalFileRequiredException;
import com.thestaticvoid.blender.service.PackageExistsException;
import com.thestaticvoid.blender.service.PackageService;
import com.thestaticvoid.blender.service.SpecFileException;
import com.thestaticvoid.blender.service.Utils;

@Controller
@RequestMapping("/upload")
public class UploadController {
	@Autowired
	private PackageService packageService;
	
	@RequestMapping(method = RequestMethod.GET)
	public void upload() {
		
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void upload(@RequestParam("specfile") MultipartFile specFile, Model model) throws IOException {
		if (!specFile.getOriginalFilename().endsWith(".spec")) {
			model.addAttribute("formatError", ".spec");
			return;
		}
		
		String packageName = specFile.getOriginalFilename().replace(".spec", "");
		
		File tmpFile = Utils.getTmpFile();
		specFile.transferTo(tmpFile);
		
		try {
			packageService.createNewPackage(packageName, tmpFile);
		} catch (SpecFileException e) {
			model.addAttribute("specError", e.getMessage());
			return;
		} catch (AdditionalFileRequiredException e) {
			model.addAttribute("packageName", e.getPackageName());
			model.addAttribute("requiredFile", e.getRequiredFile());
			return;
		} catch (PackageExistsException e) {
			model.addAttribute("packageExists", e.getMessage());
		}
	}
}