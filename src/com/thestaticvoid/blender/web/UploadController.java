package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
	public String upload(@RequestParam MultipartFile specFile, Model model) throws IOException {
		Logger.getLogger(getClass()).info("foo");
		String specFileName = specFile.getOriginalFilename();
		if (!specFileName.endsWith(".spec")) {
			model.addAttribute("invalidFilename", ".spec");
			return "upload";
		}

		String packageName = specFileName.substring(0, specFileName.length() - ".spec".length());
		File tmpFile = Utils.getTmpFile();
		specFile.transferTo(tmpFile);

		return "upload";
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "packageName")
	public String upload(@RequestParam String packageName, MultipartHttpServletRequest request, Model model) throws IOException {
		Logger.getLogger(getClass()).info("bar");
		return "upload";
	}
}