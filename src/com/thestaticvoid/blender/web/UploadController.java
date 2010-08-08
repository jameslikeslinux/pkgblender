package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.thestaticvoid.blender.service.PackageService;
import com.thestaticvoid.blender.service.Utils;
import com.thestaticvoid.blender.spec.AdditionalFilesRequiredException;
import com.thestaticvoid.blender.spec.FileType;
import com.thestaticvoid.blender.spec.InvalidFileNameException;
import com.thestaticvoid.blender.spec.Spec;
import com.thestaticvoid.blender.spec.SpecFileException;

@Controller
@RequestMapping("/upload")
public class UploadController {	
	@Autowired
	private PackageService packageService;
	
	@RequestMapping(method = RequestMethod.GET)
	public void upload(Model model) {
		UploadForm uploadForm = new UploadForm();
		uploadForm.addFile("Spec File", FileType.SPEC);
		model.addAttribute(uploadForm);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String upload(UploadForm uploadForm, BindingResult result) throws IOException {
		MultipartFile[] multipartFiles = uploadForm.getFiles();
		Spec spec;
		
		// check for whether we're creating a new package or adding files to an existing one
		if (uploadForm.getPackageName().isEmpty()) {
			// ensure the user uploaded a file
			if (multipartFiles.length != 1 || multipartFiles[0].isEmpty()) {
				result.rejectValue("files[0]", "field.required");
				return "upload";
			}
			
			// ensure the file is a spec file
			MultipartFile specFile = multipartFiles[0];
			if (!specFile.getOriginalFilename().endsWith(".spec")) {
				result.rejectValue("files[0]", "upload.bad.filename", new Object[]{"*.spec"}, "The filename should be of the form '*.spec'.");
				return "upload";
			}
			
			// get the package name from the spec file name and create a new spec dir on the fs
			String packageName = specFile.getOriginalFilename().substring(0, specFile.getOriginalFilename().length() - ".spec".length());
			
			// make sure the package doesn't already exist
			if (packageService.packageExists(packageName)) {
				result.rejectValue("files[0]", "upload.package.exists", new Object[]{packageName}, "A package called '" + packageName + "' already exists.");
				return "upload";
			}
			
			spec = Spec.newSpec(packageName);
		} else
			// otherwise, load the spec definition
			spec = Spec.loadSpec(uploadForm.getPackageName());
		
		// loop through all the files that were uploaded
		for (int i = 0; i < multipartFiles.length; i++) {
			MultipartFile file = multipartFiles[i];
			String fileName = uploadForm.getFileNames().get(i);
			FileType fileType = uploadForm.getFileTypes().get(i);
			
			// reject empty/non-present uploads
			if (file.isEmpty()) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "field.required");
				continue;
			}
			
			// if the file is not a spec file and the filename doesn't match what was requested, reject it.
			if (!fileType.equals(FileType.SPEC) && !file.getOriginalFilename().equals(fileName)) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "upload.filename.must.be", new Object[]{fileName}, "The filename must be " + fileName + ".");
				continue;
			}
			
			// copy the upload to a temporary file
			File tmpFile = Utils.getTmpFile(file.getOriginalFilename());
			file.transferTo(tmpFile);
			
			// the add it to the spec dir
			try {
				spec.addFile(tmpFile, fileType);
			} catch (InvalidFileNameException e) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "upload.bad.filename", new Object[]{e.getFileNameHelp()}, "Bad filename.");
				continue;
			}
			
			uploadForm.removeFile(i);
		}
		
		if (result.hasErrors())
			return "upload";
		
		// perform the spec file validations and check for error conditions
		try {
			spec.check();
		} catch (InvalidFileNameException e) {
			// if the spec file references a file with an improper name
			// ask the user to fix the problem and start from the beginning
			uploadForm.clear();
			uploadForm.addFile("Spec File", FileType.SPEC);
			result.reject("upload.spec.bad.filename.reference", new Object[]{e.getFileType(), e.getInvalidFileName(), e.getFileNameHelp()}, "Spec references invalid file name.");
			return "upload";
		} catch (AdditionalFilesRequiredException e) {
			// if the spec file requires additional files in order to be processed
			// such as missing include files, base specs, patches, etc,
			// request them from the user
			uploadForm.clear();
			uploadForm.setPackageName(spec.getPackageName());
			for (String fileName : e.getFiles().keySet())
				uploadForm.addFile(fileName, e.getFiles().get(fileName));
			return "upload";
		} catch (SpecFileException e) {
			// in the event of any other miscellaneous spec file error
			// such as a syntax error, show the error to the user and
			// start over
			uploadForm.clear();
			uploadForm.addFile("Spec File", FileType.SPEC);
			result.reject(null, null, e.getMessage());
			return "upload";
		}

		// if we've made it this far the spec file is completely uploaded and valid
		spec.commit();
		packageService.createNewPackage(spec);
		
		return "redirect:/";
	}
}