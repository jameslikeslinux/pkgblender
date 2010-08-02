package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.thestaticvoid.blender.service.PackageService;
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
	public void upload(@RequestParam("specfile") MultipartFile specFile) throws IOException {
		File tmpFile = Utils.getTmpFile();
		specFile.transferTo(tmpFile);
		
		packageService.createNewPackage(tmpFile);
	}
}