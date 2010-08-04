package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.thestaticvoid.blender.service.PackageService;
import com.thestaticvoid.blender.service.Utils;
import com.thestaticvoid.blender.spec.AdditionalFilesRequiredException;
import com.thestaticvoid.blender.spec.InvalidFileNameException;
import com.thestaticvoid.blender.spec.SpecFileException;

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
		String specFileName = specFile.getOriginalFilename();
		if (!specFileName.endsWith(".spec")) {
			model.addAttribute("invalidFilename", ".spec");
			return "upload";
		}

		String packageName = specFileName.substring(0, specFileName.length() - ".spec".length());
		File tmpFile = Utils.getTmpFile();
		specFile.transferTo(tmpFile);
		
		try {
			packageService.createNewPackage(packageName, tmpFile);
		} catch (InvalidFileNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AdditionalFilesRequiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SpecFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "upload";
	}
	
	@RequestMapping(method = RequestMethod.POST, params = "packageName")
	public String upload(@RequestParam String packageName, MultipartHttpServletRequest request, Model model) throws IOException {		
		
		return "upload";
	}
	
	@RequestMapping(value = "/foo", method = RequestMethod.GET)
	public String foo(Model model) {
		UploadForm uploadForm = new UploadForm();
		uploadForm.setNumFiles(2);
		model.addAttribute(uploadForm);
		return "uploadMulti";
	}
	
	@RequestMapping(value = "/foo", method = RequestMethod.POST)
	public String foo(UploadForm uploadForm, BindingResult result, Model model) {
		for (MultipartFile file : uploadForm.getFiles())
			Logger.getLogger(getClass()).info(file.getOriginalFilename());
		
		result.rejectValue("files[0]", "blah");
		
		return "uploadMulti";
	}
}