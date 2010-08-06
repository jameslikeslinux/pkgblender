package com.thestaticvoid.blender.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thestaticvoid.blender.domain.User;

public class Utils {
	public static final String TMP_DIR = "/blender/tmp";
	public static final String FILE_DIR = "/blender/files";
	public static final String SPECS_DIR = "/blender/specs";
	
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
	
	public static void setCachedUser(User user) {
		Authentication currentToken = SecurityContextHolder.getContext().getAuthentication();
		UsernamePasswordAuthenticationToken newToken = new UsernamePasswordAuthenticationToken(user, currentToken.getCredentials(), user.getAuthorities());
		newToken.setDetails(user);
		SecurityContextHolder.getContext().setAuthentication(newToken);
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
		File tmpDir = new File(TMP_DIR);
		
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		
		try {
			return File.createTempFile("blender-", "", tmpDir);
		} catch (IOException e) {
			// XXX meh
			return null;
		}
	}
	
	public static File getTmpFile(String fileName) {
		File tmpDir = new File(TMP_DIR);
		
		if (!tmpDir.exists())
			tmpDir.mkdirs();
		
		File tmpFile = new File(tmpDir + "/" + fileName);
		
		try {
			tmpFile.createNewFile();
		} catch (IOException e) {
			// XXX meh
			return null;
		}
		
		return tmpFile;
	}
}
