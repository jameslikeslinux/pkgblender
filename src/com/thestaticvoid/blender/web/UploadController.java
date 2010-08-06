package com.thestaticvoid.blender.web;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.thestaticvoid.blender.service.Utils;
import com.thestaticvoid.blender.spec.AdditionalFilesRequiredException;
import com.thestaticvoid.blender.spec.FileType;
import com.thestaticvoid.blender.spec.InvalidFileNameException;
import com.thestaticvoid.blender.spec.Spec;
import com.thestaticvoid.blender.spec.SpecFileException;

@Controller
@RequestMapping("/upload")
public class UploadController {	
	@RequestMapping(method = RequestMethod.GET)
	public void upload(Model model) {
		UploadForm uploadForm = new UploadForm();
		uploadForm.addFile("Spec File", FileType.SPEC);
		model.addAttribute(uploadForm);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String upload(UploadForm uploadForm, BindingResult result, Model model) throws IOException {
		MultipartFile[] multipartFiles = uploadForm.getFiles();
		Spec spec;
		
		if (uploadForm.getPackageName().isEmpty()) {
			if (multipartFiles.length != 1 || multipartFiles[0].isEmpty()) {
				result.rejectValue("files[0]", "field.required");
				return "upload";
			}
			
			MultipartFile specFile = multipartFiles[0];
			if (!specFile.getOriginalFilename().endsWith(".spec")) {
				result.rejectValue("files[0]", "upload.bad.filename", new Object[]{"*.spec"}, "Bad filename.");
				return "upload";
			}
			
			String packageName = specFile.getOriginalFilename().substring(0, specFile.getOriginalFilename().length() - ".spec".length());
			spec = Spec.newSpec(packageName);
		} else
			spec = Spec.loadSpec(uploadForm.getPackageName());
		
		for (int i = 0; i < multipartFiles.length; i++) {
			MultipartFile file = multipartFiles[i];
			
			if (file.isEmpty()) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "field.required");
				continue;
			}
			
			// if the file is not a spec file and the filename doesn't match what was requested, reject it.
			if (!uploadForm.getFileTypes().get(i).equals(FileType.SPEC) && !file.getOriginalFilename().equals(uploadForm.getFileNames().get(i))) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "upload.filename.must.be", new Object[]{uploadForm.getFileNames().get(i)}, "The filename must be " + uploadForm.getFileNames().get(i) + ".");
				continue;
			}
			
			File tmpFile = Utils.getTmpFile(file.getOriginalFilename());
			file.transferTo(tmpFile);
			
			try {
				spec.addFile(tmpFile, uploadForm.getFileTypes().get(i));
			} catch (InvalidFileNameException e) {
				result.rejectValue("files[" + result.getErrorCount() + "]", "upload.bad.filename", new Object[]{e.getFileNameHelp()}, "Bad filename.");
				continue;
			}
			
			uploadForm.removeFile(i);
		}
		
		if (result.hasErrors())
			return "upload";
		
		try {
			spec.check();
		} catch (InvalidFileNameException e) {
			uploadForm.clear();
			uploadForm.addFile("Spec File", FileType.SPEC);
			result.reject("upload.spec.bad.filename.reference", new Object[]{e.getFileType(), e.getInvalidFileName(), e.getFileNameHelp()}, "Spec references invalid file name.");
			return "upload";
		} catch (AdditionalFilesRequiredException e) {
			uploadForm.clear();
			uploadForm.setPackageName(spec.getPackageName());
			
			for (String fileName : e.getFiles().keySet())
				uploadForm.addFile(fileName, e.getFiles().get(fileName));
			
			return "upload";
		} catch (SpecFileException e) {
			uploadForm.clear();
			uploadForm.addFile("Spec File", FileType.SPEC);
			result.reject(null, null, e.getMessage());
			return "upload";
		}

		return "upload";
	}
}