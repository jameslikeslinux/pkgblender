package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.security.core.context.SecurityContextHolder;

import com.thestaticvoid.blender.domain.User;

public class Utils {
	public static final String FILE_DIR = "/blender";
	
	public static <T> Map<String, String> validate(Validator validator, T object) {
		Map<String, String> errors = new HashMap<String, String>();
		Set<ConstraintViolation<T>> violations = validator.validate(object);
		
		for (ConstraintViolation<T> violation : violations) {
			String path = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			errors.put(path, message);
		}
		
		return errors;
	}
	
	public static User getCachedUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	public static String byteArrayToHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xff & bytes[i]);
			
			if (hex.length() == 1)
				buffer.append("0");
			
			buffer.append(hex);
		}
		
		return buffer.toString();
	}
	
	public static File getTmpFile() {
		File tmpDir = new File(FILE_DIR +  "/tmp");
		
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		
		try {
			return File.createTempFile("blender-", "", tmpDir);
		} catch (IOException e) {
			// XXX meh
			return null;
		}
	}
}
